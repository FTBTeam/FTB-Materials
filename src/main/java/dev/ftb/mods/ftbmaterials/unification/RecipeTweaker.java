package dev.ftb.mods.ftbmaterials.unification;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class RecipeTweaker {
    private static final UnboundedMapCodec<String, List<Rule>> RULES_CODEC_RAW
            = Codec.unboundedMap(Codec.STRING, Rule.CODEC.listOf());
    public static final Codec<Map<String, List<Rule>>> RULES_CODEC
            = RULES_CODEC_RAW.xmap(HashMap::new, Function.identity());

    public static final Codec<RecipeTweaker> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            RULES_CODEC.fieldOf("rules").forGetter(r -> r.ruleDB)
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
            RecipeTweaker res = CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            UnifierManager.loadExtraJsonFiles(UnifierManager.RULES_DIR, el ->
                    res.addExtraRules(RULES_CODEC.parse(JsonOps.INSTANCE, el).getOrThrow()));
            return res;
        } else {
            return EMPTY;
        }
    }

    private void addExtraRules(Map<String, List<Rule>> ruleMap) {
        ruleMap.forEach((type, rules) ->
                ruleDB.merge(type, rules, (r1, r2) -> Stream.concat(r1.stream(), r2.stream()).toList())
        );
    }

    public void save(Path path) throws IOException {
        var res = CODEC.encodeStart(JsonOps.INSTANCE, this);
        if (res.isSuccess()) {
            var gson = new Gson().newBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            Files.writeString(path, gson.toJson(res.getOrThrow()));
        }
    }

    public JsonElement mutateRecipe(JsonElement element, UnifierDB unifierDB) {
        if (element.isJsonObject() && element.getAsJsonObject().has("type")) {
            List<Rule> customRules = ruleDB.get(element.getAsJsonObject().get("type").getAsString());
            boolean madeChange = false;
            if (customRules != null) {
                // apply all custom rules
                for (Rule rule : customRules) {
                    if (rule.apply(element.getAsJsonObject(), unifierDB)) {
                        madeChange = true;
                    }
                }
            }
            if (!madeChange) {
                // just autoscan
                return scanAndMutateJsonElement(element, unifierDB);
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
                } else {
                    scanAndMutateJsonElement(val, unifierDB);
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
        ruleDB.computeIfAbsent(recipeType.toString(), ignored -> new ArrayList<>()).addAll(List.of(rules));
    }

    private enum MappingType {
        NONE(
                (db, in) -> new Mappings(in, in),
                (in, mapping) -> in
        ),
        ITEM(
                (db, in) -> new Mappings(db.lookupItem(in).orElse(in), db.lookupItemTag(in).orElse(in)),
                (in, mappings) -> in.replace("<item_map>", mappings.objMapping).replace("<item_tag_map>", mappings.tagMapping)
        ),
        FLUID(
                (db, in) -> new Mappings(db.lookupFluid(in).orElse(in), db.lookupFluidTag(in).orElse(in)),
                (in, mappings) -> in.replace("<fluid_map>", mappings.objMapping).replace("<fluid_tag_map>", mappings.tagMapping)
        );

        private final BiFunction<UnifierDB, String, Mappings> mappingFactory;
        private final BiFunction<String, Mappings, String> replacer;

        MappingType(BiFunction<UnifierDB, String, Mappings> mappingFactory, BiFunction<String, Mappings, String> replacer) {
            this.mappingFactory = mappingFactory;
            this.replacer = replacer;
        }

        public static MappingType fromReplacementString(String str) {
            if (str.contains("<item_")) {
                return ITEM;
            } else if (str.contains("<fluid_")) {
                return FLUID;
            } else {
                return NONE;
            }
        }

        public Mappings createMappings(UnifierDB db, String input) {
            return mappingFactory.apply(db, input);
        }

        String doReplacement(String input, Mappings mappings) {
            return replacer.apply(input, mappings);
        }
    }

    private record Mappings(String objMapping, String tagMapping) {
        boolean differsFromInput(String input) {
            return !objMapping.equals(input) || !tagMapping.equals(input);
        }
    }

    public record Rule(String path, RewriteAction action) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.STRING.fieldOf("path").forGetter(Rule::path),
                RewriteAction.CODEC.fieldOf("action").forGetter(Rule::action)
        ).apply(builder, Rule::new));

        public boolean apply(JsonObject recipeJson, UnifierDB unifierDB) {
            boolean madeChange = false;

            try {
                var matchedNodes = getMatchedNodes(recipeJson);
                for (Pair<JsonObject, String> pair : matchedNodes) {
                    JsonObject json = pair.getFirst();
                    String fieldName = pair.getSecond();

                    String curVal = json.get(fieldName).getAsString();
                    var mappingType = MappingType.fromReplacementString(action.outputValue);
                    var mappings = mappingType.createMappings(unifierDB, curVal);

                    if (!action.inputFilter.isEmpty() && action.inputFilter.equals(curVal)
                            || action.inputFilter.isEmpty() && mappings.differsFromInput(curVal)) {
                        String newVal = mappingType.doReplacement(action.outputValue, mappings);
                        json.addProperty(action.fieldName, newVal);
                        if (!fieldName.equals(action.fieldName)) {
                            json.remove(fieldName);
                        }
                        madeChange = true;
                    }
                }
            } catch (IllegalArgumentException e) {
                FTBMaterials.LOGGER.error("invalid rule {} for recipe {}: {}", path, recipeJson.toString(), e.getMessage());
            }

            return madeChange;
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
                        collectNodes(arrayElement, part0, otherParts, res);
                    }
                } else {
                    throw new IllegalArgumentException("expected object or array for intermediate node " + part0);
                }
            }
        }

        public record RewriteAction(String fieldName, String outputValue, String inputFilter) {
            public static final Codec<RewriteAction> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("field").forGetter(RewriteAction::fieldName),
                    Codec.STRING.fieldOf("output_value").forGetter(RewriteAction::outputValue),
                    Codec.STRING.optionalFieldOf("input_value","").forGetter(RewriteAction::inputFilter)
            ).apply(builder, RewriteAction::new));

            public RewriteAction(String fieldName, String outputValue, String inputFilter) {
                this.fieldName = fieldName;
                this.outputValue = outputValue.replace("<tag_map>", "<item_tag_map>");  // legacy
                this.inputFilter = inputFilter;
            }

            public static RewriteAction create(String fieldName, String outputValue) {
                return new RewriteAction(fieldName, outputValue, "");
            }
        }
    }
}
