package dev.ftb.mods.ftbmaterials.fabric.data.helpers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.HashMap;

public class CachedTagKeyLookup<T> {
    private final HashMap<String, TagKey<T>> tagCache = new HashMap<>();
    private final ResourceKey<? extends Registry<T>> registry;

    public CachedTagKeyLookup(ResourceKey<? extends Registry<T>> registry) {
        this.registry = registry;
    }

    public TagKey<T> getOrCreateUnifiedTag(String prefix, String name) {
        // We default to the C namespace if no : is present in the string
        var namespace = "forge";
        if (prefix.contains(":")) {
            var parts = prefix.split(":");
            namespace = parts[0];
            prefix = parts[1];
        }

        var outputName = prefix;
        if (!name.isEmpty()) {
            outputName += "/" + name;
        }

        String finalOutputName = outputName;
        String finalNamespace = namespace;

        return tagCache.computeIfAbsent(
                this.cacheKey(namespace, prefix, name),
                t -> TagKey.create(registry, new ResourceLocation(finalNamespace, finalOutputName))
        );
    }

    private String cacheKey(String namespace, String prefix, String name) {
        return namespace + ":" + prefix + ":" + name;
    }
}
