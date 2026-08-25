package dev.ftb.mods.ftbmaterials.unification.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import dev.ftb.mods.ftbmaterials.config.Blacklists;
import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

class RecipeTweaker {
    private static final UnboundedMapCodec<String, List<Rule>> RULES_CODEC_RAW
            = Codec.unboundedMap(Codec.STRING, Rule.CODEC.listOf());
    private static final Codec<Map<String, List<Rule>>> RULES_CODEC
            = RULES_CODEC_RAW.xmap(HashMap::new, Function.identity());

    private static final Codec<RecipeTweaker> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            RULES_CODEC.fieldOf("rules").forGetter(r -> r.ruleDB)
    ).apply(builder, RecipeTweaker::new));

    static final RecipeTweaker EMPTY = new RecipeTweaker(Map.of());

    private final Map<String, List<Rule>> ruleDB;

    private RecipeTweaker(Map<String, List<Rule>> rules) {
        this.ruleDB = rules;
    }

    static RecipeTweaker createNew() {
        return new RecipeTweaker(new HashMap<>());  // mutable
    }

    static RecipeTweaker load(Path path) throws IOException {
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

    void save(Path path) throws IOException {
        var res = CODEC.encodeStart(JsonOps.INSTANCE, this);
        if (res.isSuccess()) {
            var gson = new Gson().newBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            Files.writeString(path, gson.toJson(res.getOrThrow()));
        }
    }

    JsonElement mutateRecipe(Identifier recipeId, JsonElement element, UnifierDB unifierDB) {
        if (element.isJsonObject() && element.getAsJsonObject().has("type")) {
            String recipeType = element.getAsJsonObject().get("type").getAsString();
            String modId = recipeId.getNamespace();
            List<Rule> customRules = ruleDB.get(recipeType);
            boolean madeChange = false;
            if (customRules != null) {
                // apply all custom rules
                for (Rule rule : customRules) {
                    if (rule.apply(element.getAsJsonObject(), unifierDB, modId)) {
                        madeChange = true;
                    }
                }
            }
            if (!madeChange) {
                // just autoscan
                Identifier recipeTypeId = Identifier.tryParse(recipeType);
                return recipeTypeId != null && Blacklists.isRecipeTweakingAllowed(recipeTypeId) ?
                        scanAndMutateJsonElement(element, unifierDB, modId) :
                        element ;
            }
        }
        return element;
    }

    private JsonElement scanAndMutateJsonElement(JsonElement element, UnifierDB unifierDB, String modId) {
        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(e -> scanAndMutateJsonElement(e, unifierDB, modId));
        } else if (element.isJsonObject()) {
            scanAndMutateJsonObject(element.getAsJsonObject(), unifierDB, modId);
        }
        return element;
    }

    private void scanAndMutateJsonObject(JsonObject o, UnifierDB unifierDB, String modId) {
        Map<String,String> alterations = new HashMap<>();
        Set<String> toRemove = new HashSet<>();
        for (Map.Entry<String, JsonElement> entry : o.entrySet()) {
            String key = entry.getKey();
            JsonElement val = entry.getValue();
            if (key.equals("id") || key.equals("item")) {
                if (val.isJsonPrimitive()) {
                    unifierDB.lookupItem(val.getAsString()).ifPresent(r -> alterations.put(key, StartupConfig.itemOverride(r, modId)));
                } else {
                    scanAndMutateJsonElement(val, unifierDB, modId);
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
                            alterations.put("item", StartupConfig.itemTagOverride(r, modId));
                            toRemove.add("tag");
                        });
                    }
                }
            } else if (!val.isJsonPrimitive() && !key.startsWith("neoforge:")) {
                scanAndMutateJsonElement(val, unifierDB, modId);
            }
        }
        alterations.forEach((property, value) -> {
            if (value != null) {
                o.addProperty(property, value);
            }
        });
        toRemove.forEach(o::remove);
    }

    void addRule(Identifier recipeType, Rule... rules) {
        ruleDB.computeIfAbsent(recipeType.toString(), ignored -> new ArrayList<>()).addAll(List.of(rules));
    }
}
