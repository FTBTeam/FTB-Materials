package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class FTBMaterialsModelProvider {
    public static class ItemModel extends ItemModelProvider {
        public ItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, FTBMaterials.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                holder.getItems().forEach(item -> this.basicItem(item.get()));
            }
        }
    }

    public static class BlockState extends BlockStateProvider {
        public BlockState(PackOutput output, ExistingFileHelper exFileHelper) {
            super(output, FTBMaterials.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                holder.getBlocks().forEach(block -> simpleBlock(block.get()));
            }
        }
    }
}
