package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ItemTagsGenerator extends ItemTagsProvider {
    private static final TagKey<Item> SILICON = TagKey.create(Registries.ITEM, conventional("silicon"));
    private static final TagKey<Item> DUST_WOODS = TagKey.create(Registries.ITEM, conventional("dusts/wood"));

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

        for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
            Map<DeferredItem<Item>, Pair<Resource, ResourceType>> reverseItemLookup = holder.getReverseItemLookup();
            for (Map.Entry<DeferredItem<Item>, Pair<Resource, ResourceType>> deferredItemPairEntry : reverseItemLookup.entrySet()) {
                var resource = deferredItemPairEntry.getValue().left();
                var resourceType = deferredItemPairEntry.getValue().right();

                if (resource.equals(Resource.SILICON) && resourceType.equals(ResourceType.GEM)) {
                    tag(SILICON).add(deferredItemPairEntry.getKey().get());
                }

                if (resource.equals(Resource.SAW) && resourceType.equals(ResourceType.DUST)) {
                    tag(DUST_WOODS).add(deferredItemPairEntry.getKey().get());
                }
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

    private static Identifier conventional(String name) {
        return Identifier.fromNamespaceAndPath("c", name);
    }
}
