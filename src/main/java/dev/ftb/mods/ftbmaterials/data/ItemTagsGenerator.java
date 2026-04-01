package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ItemTagsGenerator extends ItemTagsProvider {
    public ItemTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, FTBMaterials.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        final var cacheTagKeyLookup = new CachedTagKeyLookup<>(this.registryKey);

        for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
            Resource resource = holder.getResource();

            for (ResourceType resourceType : resource.getResourceTypes()) {
                holder.getItemFromType(resourceType).ifPresent(target -> {
                    Item item = target.get();
                    Set<TagKey<Item>> tags = collectTagsForElement(resource, resourceType, cacheTagKeyLookup);
                    for (var tag : tags) {
                        tag(tag).add(item);
                    }
                });
            }
        }
    }

    public static <T> Set<TagKey<T>> collectTagsForElement(
            Resource type,
            ResourceType resourceType,
            CachedTagKeyLookup<T> cacheTagKeyLookup
    ) {
        var resourceName = type.name().toLowerCase();

        Set<TagKey<T>> tags = new HashSet<>();
        for (var tagName : resourceType.getTags()) {
            tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, ""));
            tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, resourceType.getResourceNameMutator().apply(resourceName)));
            if (tagName.startsWith("c:ores")) {
                String ftbTagName = tagName.replace("c:", "ftbmaterials:");
                tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(ftbTagName, ""));
                tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(ftbTagName, resourceType.getResourceNameMutator().apply(resourceName)));
            }
        }

        String extra = resourceType.getExtraBlockTag();
        if (!extra.isEmpty()) {
            tags.add(cacheTagKeyLookup.getOrCreateUnifiedTag(extra, ""));
        }

        return tags;
    }
}
