package dev.ftb.mods.ftbmaterials.unification;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RecipeTweaker {
    public static final Codec<RecipeTweaker> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.unboundedMap(Codec.STRING, Rule.CODEC.listOf()).fieldOf("rules").forGetter(r -> r.ruleDB)
    ).apply(builder, RecipeTweaker::new));

    public static final RecipeTweaker EMPTY = new RecipeTweaker(Map.of());

    private final Map<String, List<Rule>> ruleDB;

    private RecipeTweaker(Map<String, List<Rule>> rules) {
        this.ruleDB = rules;
    }

    public static RecipeTweaker createNew() {
        return new RecipeTweaker(new HashMap<>());  // mutable
    }

    public static RecipeTweaker load(Path path) throws IOException {
        if (Files.exists(path)) {
            JsonElement json = JsonParser.parseString(Files.readString(path));
            return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        } else {
            return EMPTY;
        }
    }

    public void save(Path path) throws IOException {
        var res = CODEC.encodeStart(JsonOps.INSTANCE, this);
        if (res.isSuccess()) {
            Files.writeString(path, res.getOrThrow().toString());
        }
    }

    public JsonElement mutateRecipe(JsonElement element, UnifierDB unifierDB) {
        if (element.isJsonObject() && element.getAsJsonObject().has("type")) {
            List<Rule> customRules = ruleDB.get(element.getAsJsonObject().get("type").getAsString());
            if (customRules == null) {
                // just autoscan
                return scanAndMutateJsonElement(element, unifierDB);
            } else {
                // apply all custom rules
                for (Rule rule : customRules) {
                    rule.apply(element.getAsJsonObject(), unifierDB);
                }
            }
        }
        return element;
    }

    public JsonElement scanAndMutateJsonElement(JsonElement element, UnifierDB unifierDB) {
        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(e -> scanAndMutateJsonElement(e, unifierDB));
        } else if (element.isJsonObject()) {
            scanAndMutateJsonObject(element.getAsJsonObject(), unifierDB);
        }
        return element;
    }

    private void scanAndMutateJsonObject(JsonObject o, UnifierDB unifierDB) {
        Map<String,String> alterations = new HashMap<>();
        Set<String> toRemove = new HashSet<>();
        for (Map.Entry<String, JsonElement> entry : o.entrySet()) {
            String key = entry.getKey();
            JsonElement val = entry.getValue();
            if (key.equals("id") || key.equals("item")) {
                if (val.isJsonPrimitive()) {
                    unifierDB.lookupItem(val.getAsString()).ifPresent(r -> alterations.put(key, r));
                }
            } else if (key.equals("tag")) {
                if (val.isJsonPrimitive()) {
                    String strVal = val.getAsString();
                    if (strVal.startsWith("c:ores/") && strVal.length() > 7) {
                        // Ore tags need a little special handling: map c:ores/<X> to ftbmaterials:ores/<X>,
                        //  assuming of course that <X> is a material that we handle. This is because there
                        //  are four different subtypes of ore (stone, deepslate, nether & end).
                        String resourceName = strVal.substring(strVal.indexOf('/') + 1);
                        if (Resource.isFTBResource(resourceName)) {
                            alterations.put("tag", "ftbmaterials:ores/" + resourceName);
                        }
                    } else {
                        unifierDB.lookupItemTag(strVal).ifPresent(r -> {
                            alterations.put("item", r);
                            toRemove.add("tag");
                        });
                    }
                }
            } else if (!val.isJsonPrimitive() && !key.startsWith("neoforge:")) {
                scanAndMutateJsonElement(val, unifierDB);
            }
        }
        alterations.forEach((property, value) -> {
            if (value != null) {
                o.addProperty(property, value);
            }
        });
        toRemove.forEach(o::remove);
    }

    public void addRule(ResourceLocation recipeType, Rule... rules) {
        ruleDB.computeIfAbsent(recipeType.toString(), k -> new ArrayList<>()).addAll(List.of(rules));
    }

    public record Rule(String path, RewriteAction action) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.STRING.fieldOf("path").forGetter(Rule::path),
                RewriteAction.CODEC.fieldOf("action").forGetter(Rule::action)
        ).apply(builder, Rule::new));

        public void apply(JsonObject recipeJson, UnifierDB unifierDB) {
            try {
                var matchedNodes = getMatchedNodes(recipeJson);
                matchedNodes.forEach(pair -> {
                    JsonObject json = pair.getFirst();
                    String fieldName = pair.getSecond();

                    String curVal = json.get(fieldName).getAsString();
                    String tagMapped = unifierDB.lookupItemTag(curVal).orElse(curVal);
                    String itemMapped = unifierDB.lookupItem(curVal).orElse(curVal);

                    if (!tagMapped.equals(curVal) || !itemMapped.equals(curVal)) {
                        String newVal = action.substitution
                                .replace("<tag_map>", tagMapped)
                                .replace("<item_map>", itemMapped);
                        json.addProperty(action.fieldName, newVal);
                        if (!fieldName.equals(action.fieldName)) {
                            json.remove(fieldName);
                        }
                    }
                });
            } catch (IllegalArgumentException e) {
                FTBMaterials.LOGGER.error("invalid rule {} for recipe {}: {}", path, recipeJson.toString(), e.getMessage());
            }
        }

        private List<Pair<JsonObject,String>> getMatchedNodes(JsonObject object) {
            String p = path;
            if (p.startsWith("/")) {
                p = p.substring(1);
            }
            String[] parts = p.split("/");
            if (parts.length == 0) {
                return List.of();
            }
            List<Pair<JsonObject,String>> res = new ArrayList<>();
            collectNodes(object, parts[0], Arrays.copyOfRange(parts, 1, parts.length), res);
            return res;
        }

        private void collectNodes(JsonElement el, String part0, String[] otherParts, List<Pair<JsonObject,String>> res) {
            if (otherParts.length == 0) {
                // leaf node; element should be an object & field should be a primitive member
                if (el.isJsonObject()) {
                    JsonObject o = el.getAsJsonObject();
                    if (o.has(part0) && o.get(part0).isJsonPrimitive()) {
                        res.add(Pair.of(o, part0));
                    }
                } else if (el.isJsonArray()) {
                    for (JsonElement arrayElement : el.getAsJsonArray()) {
                        collectNodes(arrayElement, part0, new String[0], res);
                    }
                } else {
                    throw new IllegalArgumentException("expected primitive member for leaf node '" + part0 + "'");
                }
            } else {
                // intermediate node; element should be an object or array
                String[] rest = Arrays.copyOfRange(otherParts, 1, otherParts.length);
                if (el.isJsonObject()) {
                    JsonElement field = el.getAsJsonObject().get(part0);
                    if (field != null) {
                        collectNodes(field, otherParts[0], rest, res);
                    }
                } else if (el.isJsonArray()) {
                    for (JsonElement arrayElement : el.getAsJsonArray()) {
                        collectNodes(arrayElement, otherParts[0], rest, res);
                    }
                } else {
                    throw new IllegalArgumentException("expected object or array for intermediate node " + part0);
                }
            }
        }

        public record RewriteAction(String fieldName, String substitution) {
            public static final Codec<RewriteAction> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("field").forGetter(RewriteAction::fieldName),
                    Codec.STRING.fieldOf("sub").forGetter(RewriteAction::substitution)
            ).apply(builder, RewriteAction::new));
        }
    }
}
