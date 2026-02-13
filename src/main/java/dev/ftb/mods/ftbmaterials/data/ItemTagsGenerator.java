package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ItemTagsGenerator extends ItemTagsProvider {
    public ItemTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, FTBMaterials.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        final var cacheTagKeyLookup = new CachedTagKeyLookup<>(this.registryKey);
        final var resourceRegistry = ResourceRegistries.allHolders();

        for (ResourceRegistryHolder holder : resourceRegistry) {
            Resource resource = holder.getResource();

            for (ResourceType resourceType : resource.getResourceTypes()) {
                holder.getItemFromType(resourceType).ifPresent(target -> {
                    if (target.getKey() != null) {
                        Set<TagKey<Item>> tags = collectTagsForElement(resource, resourceType, cacheTagKeyLookup);
                        for (var tag : tags) {
                            this.tag(tag).add(target.getKey());
                        }
                    }
                });
            }
        }
    }

    public static <T> Set<TagKey<T>> collectTagsForElement(
            Resource type,
            ResourceType component,
            CachedTagKeyLookup<T> cacheTagKeyLookup
    ) {
        Set<TagKey<T>> tags = new HashSet<>();

        var resourceName = type.name().toLowerCase();
        var prefixRaw = component.getUnifiedTagPrefix();

        if (prefixRaw.isEmpty()) {
            return tags;
        }

        var prefixes = prefixRaw.split("\\|");

        for (String prefix : prefixes) {
            resourceName = component.getResourceNameMutator().apply(resourceName);

            var base = cacheTagKeyLookup.getOrCreateUnifiedTag(prefix, "");
            var specific = cacheTagKeyLookup.getOrCreateUnifiedTag(prefix, resourceName);

            tags.add(base);
            tags.add(specific);
        }

        for (var tagName : component.getTags()) {
            var tag = cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, "");
            tags.add(tag);
        }

        return tags;
    }
}
