package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class RecipesGenerator extends FabricRecipeProvider {
    public RecipesGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> recipeOutput) {
        // Raw/Ingot to Blocks Of
        createBlocksOfMaterial(recipeOutput, ResourceType.RAW_ORE, ResourceType.RAW_BLOCK);
        createBlocksOfMaterial(recipeOutput, ResourceType.INGOT, ResourceType.BLOCK);
        create4x4OfMaterial(recipeOutput, ResourceType.CHUNK, ResourceType.CLUSTER);

        // Blocks of material to ingots
        createInputOutputRecipeFromTypes(ResourceType.BLOCK, ResourceType.INGOT, ResourceRegistryHolder::getBlockFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItemLike, 9)
                    .requires(inputItemLike)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput, outputName + "_from_" + inputName);
        });

        createInputOutputRecipeFromTypes(ResourceType.RAW_BLOCK, ResourceType.RAW_ORE, ResourceRegistryHolder::getBlockFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItemLike, 9)
                    .requires(inputItemLike)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput, outputName + "_from_" + inputName);
        });

        // Ingots to nuggets
        createInputOutputRecipeFromTypes(ResourceType.INGOT, ResourceType.NUGGET, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItemLike, 9)
                    .requires(inputItemLike)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput);
        });

        // Nuggets to ingots
        createInputOutputRecipeFromTypes(ResourceType.NUGGET, ResourceType.INGOT, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputItemLike)
                    .define('N', inputItemLike)
                    .pattern("NNN")
                    .pattern("NNN")
                    .pattern("NNN")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput);
        });

        // Raw ore smelts to ingots
        createInputOutputRecipeFromTypes(ResourceType.RAW_ORE, ResourceType.INGOT, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            // Blast furnace
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            SimpleCookingRecipeBuilder.blasting(Ingredient.of(inputItemLike), RecipeCategory.MISC, outputItemLike, 0.7f, 100)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput, outputName + "_from_blasting_" + inputName);

            // Furnace
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputItemLike), RecipeCategory.MISC, outputItemLike, 0.7f, 200)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput, outputName + "_from_smelting_" + inputName);
        });
    }

    private void createBlocksOfMaterial(Consumer<FinishedRecipe> recipeOutput, ResourceType inputType, ResourceType outputType) {
        createInputOutputRecipeFromTypes(inputType, outputType, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getBlockFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputItemLike)
                    .define('I', inputItemLike)
                    .pattern("III")
                    .pattern("III")
                    .pattern("III")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput);
        });
    }

    private void create4x4OfMaterial(Consumer<FinishedRecipe> recipeOutput, ResourceType inputType, ResourceType outputType) {
        createInputOutputRecipeFromTypes(inputType, outputType, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputItemLike)
                    .define('I', inputItemLike)
                    .pattern("II ")
                    .pattern("II ")
                    .pattern("   ")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput);
        });
    }

    private <INPUT, OUTPUT> void createInputOutputRecipeFromTypes(
            ResourceType input,
            ResourceType output,
            BiFunction<ResourceRegistryHolder, ResourceType, Optional<RegistrySupplier<INPUT>>> inputGetter,
            BiFunction<ResourceRegistryHolder, ResourceType, Optional<RegistrySupplier<OUTPUT>>> outputGetter,
            BuilderConsumer<INPUT, OUTPUT> recipeBuilder
    ) {
        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            Optional<RegistrySupplier<INPUT>> inputResource = inputGetter.apply(holder, input);
            Optional<RegistrySupplier<OUTPUT>> outputResource = outputGetter.apply(holder, output);

            if (inputResource.isPresent() && outputResource.isPresent()) {
                recipeBuilder.accept((ItemLike) inputResource.get().get(), (ItemLike) outputResource.get().get(), inputResource.get(), outputResource.get());
            }
        }
    }


    @FunctionalInterface
    private interface BuilderConsumer<INPUT, OUTPUT> {
        void accept(ItemLike input, ItemLike output, RegistrySupplier<INPUT> inputResource, RegistrySupplier<OUTPUT> outputResource);
    }
}
