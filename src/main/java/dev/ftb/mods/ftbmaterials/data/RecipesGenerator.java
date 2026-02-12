package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class RecipesGenerator extends RecipeProvider {
    public RecipesGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        // Raw/Ingot to Blocks Of
        createBlocksOfMaterial(recipeOutput, ResourceType.RAW_ORE, ResourceType.RAW_BLOCK);
        createBlocksOfMaterial(recipeOutput, ResourceType.INGOT, ResourceType.BLOCK);
        create4x4OfMaterial(recipeOutput, ResourceType.CHUNK, ResourceType.CLUSTER);

        // Blocks of material to ingots
        createInputOutputRecipeFromTypes(ResourceType.BLOCK,
                ResourceType.INGOT,
                ResourceRegistryHolder::getBlockFromType,
                ResourceRegistryHolder::getItemFromType,
                (inputItemLike, outputItemLike, inputReg, outputReg) ->
                {
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

        // Tiny Dust to Dust
        createInputOutputRecipeFromTypes(ResourceType.TINY_DUST, ResourceType.DUST, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputItemLike)
                    .define('N', inputItemLike)
                    .pattern("NNN")
                    .pattern("NNN")
                    .pattern("NNN")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput, outputName + "_from_" + inputName);


        });

        // Dust to Tiny Dust
        createInputOutputRecipeFromTypes(ResourceType.DUST, ResourceType.TINY_DUST, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, outputItemLike, 9)
                    .requires(inputItemLike)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(recipeOutput, outputName + "_from_" + inputName);
        });
    }

    private void createBlocksOfMaterial(RecipeOutput recipeOutput, ResourceType inputType, ResourceType outputType) {
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

    private void create4x4OfMaterial(RecipeOutput recipeOutput, ResourceType inputType, ResourceType outputType) {
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

    private <I, I2 extends I, O, O2 extends O> void createInputOutputRecipeFromTypes(
            ResourceType input,
            ResourceType output,
            BiFunction<ResourceRegistryHolder, ResourceType, Optional<DeferredHolder<I, I2>>> inputGetter,
            BiFunction<ResourceRegistryHolder, ResourceType, Optional<DeferredHolder<O, O2>>> outputGetter,
            BuilderConsumer<I, I2, O, O2> recipeBuilder
    ) {
        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            Optional<DeferredHolder<I, I2>> inputResource = inputGetter.apply(holder, input);
            Optional<DeferredHolder<O, O2>> outputResource = outputGetter.apply(holder, output);

            if (inputResource.isPresent() && outputResource.isPresent()) {
                recipeBuilder.accept((ItemLike) inputResource.get().get(), (ItemLike) outputResource.get().get(), inputResource.get(), outputResource.get());
            }
        }
    }

    @FunctionalInterface
    private interface BuilderConsumer<I, I2 extends I, O, O2 extends O> {
        void accept(ItemLike input, ItemLike output, DeferredHolder<I, I2> inputResource, DeferredHolder<O, O2> outputResource);
    }
}
