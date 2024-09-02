package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for (ResourceRegistryHolder resourceRegistryHolder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            resourceRegistryHolder.getBlocks().forEach(e -> blockStateModelGenerator.createTrivialCube(e.get()));
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (ResourceRegistryHolder resourceRegistryHolder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            resourceRegistryHolder.getItems().forEach(e -> {
                var data = e.get();
                if (!(data instanceof BlockItem)) {
                    // Inherit from block
                    itemModelGenerator.generateFlatItem(data, ModelTemplates.FLAT_ITEM);
                }
            });
        }
    }

    private void handHeldItem(ItemModelGenerators itemModelGenerator, RegistrySupplier<Item> item) {
        itemModelGenerator.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
    }
}
