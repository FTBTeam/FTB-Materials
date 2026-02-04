package dev.ftb.mods.ftbmaterials.unification.recipes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum RecipeMutationManager {
    INSTANCE;

    private final Set<String> unsupportedTypes = new HashSet<>();
    private final Set<String> uniqueTypes = new HashSet<>();
    private final Map<String, List<String>> typeToKeys = new HashMap<>();

    public JsonElement mutateRecipe(JsonElement json) {
        if (!json.isJsonObject()) {
            // I have no clue how this is not an object. Just move on!
            return json;
        }

        var recipeObj = json.getAsJsonObject();
        var recipeType = recipeObj.get("type");

        if (recipeType == null || recipeType == JsonNull.INSTANCE) {
            // I have no clue how there is no type. Just move on!
            return json;
        }

        var typeStr = recipeType.getAsString();
        if (uniqueTypes.add(typeStr)) {
            // Compute the keys and their depth for this type
            var keys = computeKeys(json);
            typeToKeys.put(typeStr, keys);
        }

        if (unsupportedTypes.contains(typeStr)) {
            // We already know this type is unsupported, skip processing
            return json;
        }

        var schema = RecipeSchemas.SCHEMAS.get(typeStr);
        if (schema == null) {
            // Unsupported recipe type
            unsupportedTypes.add(typeStr);
            return json;
        }

        // Don't do anything for now, we'll come back to this later
//        return json;

        // Now we can mutate it.
        return applyMutations(recipeObj, schema);
    }

    private JsonObject applyMutations(JsonObject json, RecipeSchema schema) {
        var inputFields = schema.input();
        var outputFields = schema.output();

        for (var output : outputFields) {
            if (json.has(output)) {
                var field = json.get(output);
                if (field.isJsonObject()) {
                    var input = field.getAsJsonObject();
                    if (input.has("id")) {
                        System.out.println("Mutating recipe output id: " + input.get("id").getAsString());
                        input.addProperty("id", "minecraft:diamond");
                    }
                }
            }
        }

        return json;
    }

    /**
     * Completes a list of keys and their depth in the json structure.
     * A json like:
     * {
     *   "ingredient": {
     *     "item": "minecraft:iron_ingot"
     *   },
     *   "result": {
     *     "item": "minecraft:iron_nugget"
     *   }
     * }
     * Would yield the keys:
     * - ingredient.item
     * - result.item
     *
     * @param json The json to compute keys for
     * @return The list of keys
     */
    private List<String> computeKeys(JsonElement json) {
        if (!json.isJsonObject()) {
            return List.of();
        }

        var jsonObj = json.getAsJsonObject();

        var keys = new HashSet<String>();
        for (var entry : jsonObj.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            if (value.isJsonObject()) {
                var subKeys = computeKeys(value);
                for (var subKey : subKeys) {
                    keys.add(key + "." + subKey);
                }
            } else {
                keys.add(key);
            }
        }

        return List.copyOf(keys);
    }

    public void commit() {
        Path outputDir = Path.of(".debugging_output");
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectories(outputDir);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create debugging output directory", e);
            }
        }

        // Write the unique types to a Json array file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            var uniqueTypesPath = outputDir.resolve("unique_recipe_types.json");
            Files.writeString(uniqueTypesPath, gson.toJson(uniqueTypes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write unique recipe types", e);
        }

        // Write the type with their keys to their own json files
        for (var entry : typeToKeys.entrySet()) {
            var type = entry.getKey();
            var keys = entry.getValue();

            try {
                var typePath = outputDir.resolve(type.replaceAll(":", "/") + "_keys.json");
                var parent = typePath.getParent();
                if (parent != null && !Files.exists(parent)) {
                    Files.createDirectories(parent);
                }
                Files.writeString(typePath, gson.toJson(keys));
            } catch (Exception e) {
                throw new RuntimeException("Failed to write keys for recipe type: " + type, e);
            }
        }
    }
}
