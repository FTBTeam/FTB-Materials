package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ItemTagsGenerator extends FabricTagProvider<Item> {
    private final HashMap<String, TagKey<Item>> unifiedTags = new HashMap<>();

    public ItemTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            Resource type = holder.getType();

            for (ResourceType component : type.getComponents()) {
                var resourceName = type.name().toLowerCase();
                var prefixRaw = component.getUnifiedTagPrefix();

                if (prefixRaw == null) {
                    continue;
                }

                var prefixes = prefixRaw.split("\\|");

                var target = holder.getItemFromType(component);
                if (target.isEmpty()) {
                    continue;
                }

                for (String p : prefixes) {
                    var base = getOrCreateUnifiedTag(p, "");
                    var specific = getOrCreateUnifiedTag(p, resourceName);

                    this.tag(base).add(target.get().getKey());
                    this.tag(specific).add(target.get().getKey());
                }
            }
        }
    }

    private TagKey<Item> getOrCreateUnifiedTag(String prefix, String name) {
        var namespace = "c";
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
        return unifiedTags.computeIfAbsent(namespace + ":" + prefix + ":" + name, t ->
                TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(finalNamespace, finalOutputName)));
    }
}
