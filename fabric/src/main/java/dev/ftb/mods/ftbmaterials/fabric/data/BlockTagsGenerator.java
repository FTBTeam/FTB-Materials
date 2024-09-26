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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class BlockTagsGenerator extends FabricTagProvider<Block> {
    private final HashMap<String, TagKey<Block>> unifiedTags = new HashMap<>();

    public BlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            Resource type = holder.getType();

            for (ResourceType component : type.getComponents()) {
                if (!ResourceRegistryHolder.BLOCK_TYPES.contains(component)) {
                    continue;
                }

                var resourceName = type.name().toLowerCase();
                var prefixRaw = component.getUnifiedTagPrefix();

                if (prefixRaw == null) {
                    continue;
                }

                var prefixes = prefixRaw.split("\\|");

                var target = holder.getBlockFromType(component);
                if (target.isEmpty()) {
                    continue;
                }

                for (String p : prefixes) {
                    var base = getOrCreateUnifiedTag(p, "");
                    var specific = getOrCreateUnifiedTag(p, resourceName);

                    this.tag(base).add(target.get().getKey());
                    this.tag(specific).add(target.get().getKey());
                }

                this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(target.get().getKey());
                this.tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).add(target.get().getKey());

                ResourceLocation breakableWith = type.getBreakableWith();
                TagKey<Block> breakableWithTag = unifiedTags.computeIfAbsent(breakableWith.toString(), t ->
                        TagKey.create(Registries.BLOCK, breakableWith));

                this.tag(breakableWithTag).add(target.get().getKey());
            }
        }
    }

    private TagKey<Block> getOrCreateUnifiedTag(String prefix, String name) {
        var outputName = prefix;
        if (!name.isEmpty()) {
            outputName += "/" + name;
        }

        String finalOutputName = outputName;
        return unifiedTags.computeIfAbsent(prefix + ":" + name, t ->
                TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", finalOutputName)));
    }
}
