package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class FTBMaterialsModelProvider extends ModelProvider {
    public FTBMaterialsModelProvider(PackOutput output) {
        super(output, FTBMaterials.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
            holder.getBlocks().forEach(block -> blockModels.createTrivialCube(block.get()));
            holder.getItems().forEach(item -> itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM));
        }
    }
}
