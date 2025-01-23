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
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ItemTagsGenerator extends FabricTagProvider<Item> {
    public ItemTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        final var cacheTagKeyLookup = new CachedTagKeyLookup<>(this.registryKey);
        final var resourceRegistry = ResourceRegistry.RESOURCE_REGISTRY_HOLDERS;

        for (ResourceRegistryHolder holder : resourceRegistry) {
            Resource type = holder.getType();

            for (ResourceType component : type.getComponents()) {
                var target = holder.getItemFromType(component);
                if (target.isEmpty()) {
                    continue;
                }

                Set<TagKey<Item>> tags = collectTagsForElement(type, component, cacheTagKeyLookup);
                for (var tag : tags) {
                    this.tag(tag).add(target.get().getKey());
                }
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

        if (prefixRaw == null) {
            return tags;
        }

        var prefixes = prefixRaw.split("\\|");

        for (String prefix : prefixes) {
            Function<String, String> resourceNameMutator = component.getResourceNameMutator();
            if (resourceNameMutator != null) {
                resourceName = resourceNameMutator.apply(resourceName);
            }

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
