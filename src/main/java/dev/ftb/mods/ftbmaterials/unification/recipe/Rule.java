package dev.ftb.mods.ftbmaterials.unification.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

record Rule(String path, RewriteAction action) {
    static final Codec<Rule> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("path").forGetter(Rule::path),
            RewriteAction.CODEC.fieldOf("action").forGetter(Rule::action)
    ).apply(builder, Rule::new));

    boolean apply(JsonObject recipeJson, UnifierDB unifierDB, String modId) {
        MutableBoolean madeChange = new MutableBoolean(false);

        try {
            var matchedNodes = getMatchedNodes(recipeJson);
            for (Pair<JsonObject, String> pair : matchedNodes) {
                JsonObject json = pair.getFirst();
                String fieldName = pair.getSecond();

                JsonElement field = json.get(fieldName);
                var mappingType = MappingType.fromReplacementString(action.outputValue);
                String actionField = action.fieldName.isEmpty() ? fieldName : action.fieldName;

                if (field instanceof JsonPrimitive primitive) {
                    // simple case; element is a primitive
                    mapOneValue(unifierDB, mappingType, primitive.getAsString(), modId).ifPresent(newVal -> {
                        json.addProperty(actionField, newVal);
                        if (!fieldName.equals(actionField)) {
                            json.remove(fieldName);
                        }
                        madeChange.setTrue();
                    });
                } else if (field instanceof JsonArray array) {
                    // element is an array; need to carry out mapping for each array element and build a new array
                    JsonArray outputArray = new JsonArray();
                    for (JsonElement el : array) {
                        if (el instanceof JsonPrimitive primitive) {
                            String curVal = primitive.getAsString();
                            mapOneValue(unifierDB, mappingType, curVal, modId).ifPresentOrElse(
                                    newVal -> {
                                        outputArray.add(newVal);
                                        madeChange.setTrue();
                                    },
                                    () -> outputArray.add(curVal)
                            );
                        } else {
                            outputArray.add(el);
                        }
                    }
                    if (madeChange.booleanValue()) {
                        json.add(actionField, outputArray);
                        if (!fieldName.equals(actionField)) {
                            json.remove(fieldName);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            FTBMaterials.LOGGER.error("invalid rule {} for recipe {}: {}", path, recipeJson.toString(), e.getMessage());
        }

        return madeChange.booleanValue();
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

    private Optional<String> mapOneValue(UnifierDB unifierDB, MappingType mappingType, String currentValue, String modId) {
        var mappings = mappingType.createMappings(unifierDB, currentValue);
        if (!action.inputFilter.isEmpty() && action.inputFilter.equals(currentValue)
                || action.inputFilter.isEmpty() && mappings.differsFromInput(currentValue)) {
            String res = mappingType.doReplacement(action.outputValue, mappings, modId);
            return res.equals(currentValue) ? Optional.empty() : Optional.of(res);
        } else {
            return Optional.empty();
        }
    }

    private void collectNodes(JsonElement el, String part0, String[] otherParts, List<Pair<JsonObject, String>> res) {
        if (otherParts.length == 0) {
            // leaf node; element should be an object & field should be a primitive member or array
            if (el.isJsonObject()) {
                JsonObject o = el.getAsJsonObject();
                if (o.has(part0) && (o.get(part0).isJsonPrimitive() || o.get(part0).isJsonArray())) {
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
                Codec.STRING.optionalFieldOf("field", "").forGetter(RewriteAction::fieldName),
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
