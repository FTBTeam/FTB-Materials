package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BlockTagsGenerator extends BlockTagsProvider {
    public BlockTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, FTBMaterials.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        final var cacheTagKeyLookup = new CachedTagKeyLookup<>(this.registryKey);

        for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
            Resource resource = holder.getResource();

            for (ResourceType resourceType : resource.getResourceTypes()) {
                if (resourceType.isBlock()) {
                    holder.getBlockFromType(resourceType).ifPresent(target -> {
                        Set<TagKey<Block>> tags = collectTagsForElement(resource, resourceType, cacheTagKeyLookup);
                        Block block = target.get();
                        tags.forEach(t -> tag(t).add(block));

                        Identifier breakableWith = resource.getBreakableWith();
                        TagKey<Block> breakableWithTag = cacheTagKeyLookup.getOrCreateUnifiedTag(breakableWith.toString(), "");

                        tag(breakableWithTag).add(block);
                        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                        tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).add(block);
                    });
                }

            }
        }
    }

    public static <T> Set<TagKey<T>> collectTagsForElement(
            Resource type,
            ResourceType component,
            CachedTagKeyLookup<T> cacheTagKeyLookup
    ) {
        var resourceName = type.name().toLowerCase();

        Set<TagKey<T>> tags = new HashSet<>();
        for (var tagName : component.getTags()) {
            tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, ""));
            tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, component.getResourceNameMutator().apply(resourceName)));
        }
        String extra = component.getExtraBlockTag();
        if (component.isBlock() && !extra.isEmpty()) {
            tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(extra, ""));
        }

        return tags;
    }
}
