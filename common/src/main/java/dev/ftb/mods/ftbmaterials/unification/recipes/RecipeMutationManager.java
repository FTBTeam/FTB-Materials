package dev.ftb.mods.ftbmaterials.unification.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Set;

public enum RecipeMutationManager {
    INSTANCE;

    private final Set<String> unsupportedTypes = new HashSet<>();

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
        if (unsupportedTypes.contains(typeStr)) {
            // We already know this type is unsupported, skip processing
            return json;
        }

        var schema = RecipeSchemas.SCHEMAS.get(typeStr);
        if (schema == null) {
            // Unsupported recipe type
            unsupportedTypes.add(typeStr);
            System.out.println("Unsupported recipe type for mutation: " + typeStr);
            return json;
        }

        System.out.println("Attempting to mutate recipe of type: " + typeStr);

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
}
