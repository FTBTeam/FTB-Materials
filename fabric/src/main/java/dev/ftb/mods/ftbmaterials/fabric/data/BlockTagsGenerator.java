package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.ftb.mods.ftbmaterials.fabric.data.helpers.CachedTagKeyLookup;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.ftb.mods.ftbmaterials.fabric.data.ItemTagsGenerator.collectTagsForElement;

public class BlockTagsGenerator extends FabricTagProvider<Block> {
    public BlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        final var cacheTagKeyLookup = new CachedTagKeyLookup<>(this.registryKey);
        final var resourceRegistry = ResourceRegistry.RESOURCE_REGISTRY_HOLDERS;

        for (ResourceRegistryHolder holder : resourceRegistry) {
            Resource type = holder.getType();

            for (ResourceType component : type.getComponents()) {
                var target = holder.getBlockFromType(component);
                if (target.isEmpty()) {
                    continue;
                }

                List<TagKey<Block>> tags = collectTagsForElement(type, component, cacheTagKeyLookup);
                for (var tag : tags) {
                    this.tag(tag).add(target.get().getKey());
                }
            }
        }
    }
}
