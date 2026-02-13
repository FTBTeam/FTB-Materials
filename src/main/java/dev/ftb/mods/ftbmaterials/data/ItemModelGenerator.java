package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
    private static final ResourceLocation GENERATED = ResourceLocation.parse("item/generated");

    public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FTBMaterials.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (ResourceRegistryHolder resourceRegistryHolder : ResourceRegistries.allHolders()) {
            resourceRegistryHolder.getItems().forEach(e -> {
                simpleItem(e.getId(), "item/" + e.getId().getPath());
            });
            resourceRegistryHolder.getBlocks().forEach(e -> {
                withExistingParent(e.getId().getPath(), FTBMaterials.id("block/" + e.getId().getPath()));
            });
        }
    }

    private ItemModelBuilder simpleItem(ResourceLocation itemKey, String... textures) {
        ItemModelBuilder builder = withExistingParent(itemKey.getPath(), GENERATED);
        for (int i = 0; i < textures.length; i++) {
            builder.texture("layer" + i, textures[i]);
        }
        return builder;
    }
}
