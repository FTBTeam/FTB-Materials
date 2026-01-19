package dev.ftb.mods.ftbmaterials.fabric.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipeSchemaGenerator implements DataProvider {
    protected final FabricDataOutput dataOutput;
    private final Map<ResourceLocation, JsonObject> schemaEntries = new HashMap<>();

    public RecipeSchemaGenerator(FabricDataOutput dataOutput) {
        this.dataOutput = dataOutput;
    }

    private void addSchemas() {
        simpleInOut(ResourceLocation.withDefaultNamespace("stonecutting"));
        simpleInOut(ResourceLocation.withDefaultNamespace("smelting"));
        simpleAnyInOneOut(ResourceLocation.withDefaultNamespace("crafting_shapeless"));
        add(ResourceLocation.withDefaultNamespace("crafting_shaped"), List.of("key.*"), List.of("result"));
    }

    public void simpleInOut(ResourceLocation recipeTypeId) {
        add(recipeTypeId, List.of("ingredient"), List.of("result"), List.of());
    }

    public void simpleAnyInOneOut(ResourceLocation recipeTypeId) {
        add(recipeTypeId, List.of("ingredients.*"), List.of("result"), List.of());
    }

    public void add(ResourceLocation recipeTypeId, List<String> inputs, List<String> outputs) {
        add(recipeTypeId, inputs, outputs, List.of());
    }

    public void add(ResourceLocation recipeTypeId, List<String> inputs, List<String> outputs, List<String> catalysts) {
        JsonObject json = new JsonObject();
        json.addProperty("type", recipeTypeId.toString());
        JsonArray jsonInput = new JsonArray();
        for (String input : inputs) {
            jsonInput.add(input);
        }

        json.add("input", jsonInput);
        JsonArray jsonOutput = new JsonArray();
        for (String output : outputs) {
            jsonOutput.add(output);
        }

        json.add("output", jsonOutput);

        if (!catalysts.isEmpty()) {
            JsonArray jsonCatalyst = new JsonArray();
            for (String catalyst : catalysts) {
                jsonCatalyst.add(catalyst);
            }
            json.add("catalyst", jsonCatalyst);
        }

        schemaEntries.put(recipeTypeId, json);
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput writer) {
        this.addSchemas();

        return schemaEntries.entrySet().stream()
                .map(entry -> DataProvider.saveStable(writer, entry.getValue(), dataOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipe_schemas")
                        .json(entry.getKey())))
                .reduce(CompletableFuture.completedFuture(null), (a, b) -> a.thenCompose(v -> b));
    }

    @Override
    public @NotNull String getName() {
        return "recipe_schemas";
    }
}
