package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.config.DisabledMaterialList;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class RecipesGenerator extends RecipeProvider {
    protected RecipesGenerator(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        // Raw/Ingot to Blocks Of
        createBlocksOfMaterial(ResourceType.RAW_ORE, ResourceType.RAW_BLOCK);
        createBlocksOfMaterial(ResourceType.INGOT, ResourceType.BLOCK);
        create4x4OfMaterial(ResourceType.CHUNK, ResourceType.CLUSTER);

        // Blocks of material to ingots
        createInputOutputRecipeFromTypes(ResourceType.BLOCK,
                ResourceType.INGOT,
                ResourceRegistryHolder::getBlockFromType,
                ResourceRegistryHolder::getItemFromType,
                (inputItemLike, outputItemLike, inputReg, outputReg) ->
                {
                    var inputName = inputReg.getId().getPath();
                    var outputName = outputReg.getId().getPath();

                    ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, outputItemLike, 9)
                            .requires(inputItemLike)
                            .unlockedBy("has_item", has(inputItemLike))
                            .save(
                                    this.output.withConditions(new ComponentsAvailableCondition(Set.of(inputName, outputName))),
                                    FTBMaterials.id(outputName + "_from_" + inputName).toString()
                            );
                });

        // Raw ore to blocks of raw ore
        createInputOutputRecipeFromTypes(ResourceType.RAW_BLOCK, ResourceType.RAW_ORE, ResourceRegistryHolder::getBlockFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, outputItemLike, 9)
                    .requires(inputItemLike)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromBlockToItem(inputReg, outputReg))
                            , FTBMaterials.id(outputName + "_from_" + inputName).toString());
        });

        // Ingots to nuggets
        createInputOutputRecipeFromTypes(ResourceType.INGOT, ResourceType.NUGGET, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, outputItemLike, 9)
                    .requires(inputItemLike)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg)));
        });

        // Nuggets to ingots
        createInputOutputRecipeFromTypes(ResourceType.NUGGET, ResourceType.INGOT, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, outputItemLike)
                    .define('N', inputItemLike)
                    .pattern("NNN")
                    .pattern("NNN")
                    .pattern("NNN")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg)));
        });

        // Raw ore smelts to ingots
        createInputOutputRecipeFromTypes(ResourceType.RAW_ORE, ResourceType.INGOT, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            SimpleCookingRecipeBuilder.blasting(Ingredient.of(inputItemLike), RecipeCategory.MISC, CookingBookCategory.MISC, outputItemLike, 0.7f, 100)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg)),
                            FTBMaterials.id(outputName + "_from_blasting_" + inputName).toString());

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputItemLike), RecipeCategory.MISC, CookingBookCategory.MISC, outputItemLike, 0.7f, 200)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg)),
                            FTBMaterials.id(outputName + "_from_smelting_" + inputName).toString());
        });

        // Ore blocks smelt to ingots, or gems if no ingot exists
        for (ResourceType oreType : ResourceType.ORE_TYPES) {
            for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                holder.getBlockFromType(oreType).ifPresent(oreBlock -> {
                    ResourceType smeltOutput = holder.getItemFromType(ResourceType.INGOT).isPresent() ? ResourceType.INGOT : ResourceType.GEM;
                    holder.getItemFromType(smeltOutput).ifPresent(resultItem -> {
                        var inputName = oreBlock.getId().getPath();
                        var outputName = resultItem.getId().getPath();

                        SimpleCookingRecipeBuilder.blasting(Ingredient.of(oreBlock.get()), RecipeCategory.MISC, CookingBookCategory.MISC, resultItem.get(), 0.7f, 100)
                                .unlockedBy("has_item", has(oreBlock.get()))
                                .save(this.output.withConditions(ComponentsAvailableCondition.fromBlockToItem(oreBlock, resultItem)),
                                        FTBMaterials.id(outputName + "_from_blasting_" + inputName).toString());

                        SimpleCookingRecipeBuilder.smelting(Ingredient.of(oreBlock.get()), RecipeCategory.MISC, CookingBookCategory.MISC, resultItem.get(), 0.7f, 200)
                                .unlockedBy("has_item", has(oreBlock.get()))
                                .save(this.output.withConditions(ComponentsAvailableCondition.fromBlockToItem(oreBlock, resultItem)),
                                        FTBMaterials.id(outputName + "_from_smelting_" + inputName).toString());
                    });
                });
            }
        }

        // Clusters smelt to ingots
        createInputOutputRecipeFromTypes(ResourceType.CLUSTER, ResourceType.INGOT, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            SimpleCookingRecipeBuilder.blasting(Ingredient.of(inputItemLike), RecipeCategory.MISC, CookingBookCategory.MISC, outputItemLike, 0.7f, 100)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg))
                            , FTBMaterials.id(outputName + "_from_blasting_" + inputName).toString());

            SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputItemLike), RecipeCategory.MISC, CookingBookCategory.MISC, outputItemLike, 0.7f, 200)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg))
                            , FTBMaterials.id(outputName + "_from_smelting_" + inputName).toString());
        });

        // Tiny Dust to Dust
        createInputOutputRecipeFromTypes(ResourceType.TINY_DUST, ResourceType.DUST, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, outputItemLike)
                    .define('N', inputItemLike)
                    .pattern("NNN")
                    .pattern("NNN")
                    .pattern("NNN")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg))
                            , FTBMaterials.id(outputName + "_from_" + inputName).toString());


        });

        // Dust to Tiny Dust
        createInputOutputRecipeFromTypes(ResourceType.DUST, ResourceType.TINY_DUST, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            var inputName = inputReg.getId().getPath();
            var outputName = outputReg.getId().getPath();

            ShapelessRecipeBuilder.shapeless(this.items, RecipeCategory.MISC, outputItemLike, 9)
                    .requires(inputItemLike)
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg))
                            , FTBMaterials.id(outputName + "_from_" + inputName).toString());
        });
    }

    private void createBlocksOfMaterial(ResourceType inputType, ResourceType outputType) {
        createInputOutputRecipeFromTypes(inputType, outputType, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getBlockFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, outputItemLike)
                    .define('I', inputItemLike)
                    .pattern("III")
                    .pattern("III")
                    .pattern("III")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItemToBlock(inputReg, outputReg)));
        });
    }

    private void create4x4OfMaterial(ResourceType inputType, ResourceType outputType) {
        createInputOutputRecipeFromTypes(inputType, outputType, ResourceRegistryHolder::getItemFromType, ResourceRegistryHolder::getItemFromType, (inputItemLike, outputItemLike, inputReg, outputReg) -> {
            ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, outputItemLike)
                    .define('I', inputItemLike)
                    .pattern("II ")
                    .pattern("II ")
                    .pattern("   ")
                    .unlockedBy("has_item", has(inputItemLike))
                    .save(this.output.withConditions(ComponentsAvailableCondition.fromItems(inputReg, outputReg)));
        });
    }

    private <I, I2 extends I, O, O2 extends O> void createInputOutputRecipeFromTypes(
            ResourceType input,
            ResourceType output,
            BiFunction<ResourceRegistryHolder, ResourceType, Optional<DeferredHolder<I, I2>>> inputGetter,
            BiFunction<ResourceRegistryHolder, ResourceType, Optional<DeferredHolder<O, O2>>> outputGetter,
            BuilderConsumer<I, I2, O, O2> recipeBuilder
    ) {
        for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
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

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new RecipesGenerator(registries, output);
        }

        @Override
        public String getName() {
            return "FTB Materials Recipes";
        }
    }
}
