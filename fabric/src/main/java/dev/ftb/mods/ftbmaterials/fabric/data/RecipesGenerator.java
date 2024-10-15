package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RecipesGenerator extends FabricRecipeProvider {
    public RecipesGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        createBlocksOfMaterial(recipeOutput, ResourceType.RAW_ORE, ResourceType.RAW_BLOCK);
        createBlocksOfMaterial(recipeOutput, ResourceType.INGOT, ResourceType.BLOCK);
    }

    private void createBlocksOfMaterial(RecipeOutput recipeOutput, ResourceType inputType, ResourceType outputType) {
        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            Optional<RegistrySupplier<Item>> itemFromType = holder.getItemFromType(inputType);
            Optional<RegistrySupplier<Block>> blockFromType = holder.getBlockFromType(outputType);

            if (itemFromType.isPresent() && blockFromType.isPresent()) {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, blockFromType.get().get())
                        .define('I', itemFromType.get().get())
                        .pattern("III")
                        .pattern("III")
                        .pattern("III")
                        .unlockedBy("has_item", has(itemFromType.get().get()))
                        .save(recipeOutput);
            }
        }
    }
}
