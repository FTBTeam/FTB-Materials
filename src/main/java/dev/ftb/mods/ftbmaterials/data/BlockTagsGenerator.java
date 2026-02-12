package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BlockTagsGenerator extends BlockTagsProvider {
    public BlockTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registriesFuture, FTBMaterials.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        final var cacheTagKeyLookup = new CachedTagKeyLookup<>(this.registryKey);
        final var resourceRegistry = ResourceRegistry.RESOURCE_REGISTRY_HOLDERS;

        for (ResourceRegistryHolder holder : resourceRegistry) {
            Resource resource = holder.getResource();

            for (ResourceType resourceType : resource.getComponents()) {
                if (!resourceType.isBlock()) {
                    continue;
                }

                holder.getBlockFromType(resourceType).ifPresent(target -> {
                    Set<TagKey<Block>> tags = ItemTagsGenerator.collectTagsForElement(resource, resourceType, cacheTagKeyLookup);
                    ResourceKey<Block> resourceKey = target.getKey();
                    if (resourceKey != null) {
                        for (var tag : tags) {
                            tag(tag).add(resourceKey);
                        }

                        // Extra for blocks
                        ResourceLocation breakableWith = resource.getBreakableWith();
                        TagKey<Block> breakableWithTag = cacheTagKeyLookup.getOrCreateUnifiedTag(breakableWith.toString(), "");

                        tag(breakableWithTag).add(resourceKey);
                        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(resourceKey);
                        tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).add(resourceKey);
                    }
                });
            }
        }
    }
}
