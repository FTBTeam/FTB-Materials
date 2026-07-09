package dev.ftb.mods.ftbmaterials.unification.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbmaterials.FTBMaterials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

record Rule(String path, RewriteAction action) {
    static final Codec<Rule> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("path").forGetter(Rule::path),
            RewriteAction.CODEC.fieldOf("action").forGetter(Rule::action)
    ).apply(builder, Rule::new));

    boolean apply(JsonObject recipeJson, UnifierDB unifierDB, String modId) {
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
                    String newVal = mappingType.doReplacement(action.outputValue, mappings, modId);
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

    private List<Pair<JsonObject, String>> getMatchedNodes(JsonObject object) {
        String p = path;
        if (p.startsWith("/")) {
            p = p.substring(1);
        }
        String[] parts = p.split("/");
        if (parts.length == 0) {
            return List.of();
        }
        List<Pair<JsonObject, String>> res = new ArrayList<>();
        collectNodes(object, parts[0], Arrays.copyOfRange(parts, 1, parts.length), res);
        return res;
    }

    private void collectNodes(JsonElement el, String part0, String[] otherParts, List<Pair<JsonObject, String>> res) {
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

    record RewriteAction(String fieldName, String outputValue, String inputFilter) {
        public static final Codec<RewriteAction> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.STRING.fieldOf("field").forGetter(RewriteAction::fieldName),
                Codec.STRING.fieldOf("output_value").forGetter(RewriteAction::outputValue),
                Codec.STRING.optionalFieldOf("input_value", "").forGetter(RewriteAction::inputFilter)
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
