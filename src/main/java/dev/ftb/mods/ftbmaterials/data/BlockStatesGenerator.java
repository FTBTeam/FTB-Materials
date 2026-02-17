package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
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
        for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
            holder.getBlocks().forEach(b -> simpleBlock(b.get()));
        }
    }
}
