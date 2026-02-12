package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStatesGenerator extends BlockStateProvider {
    public BlockStatesGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FTBMaterials.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (ResourceRegistryHolder resourceRegistryHolder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            resourceRegistryHolder.getBlocks().forEach(e -> simpleBlock(e.get()));
        }
    }
}
