package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LanguageGenerator extends FabricLanguageProvider {
    protected LanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.ftbmaterials.ftbmaterials_main", "FTB Materials");

        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            holder.getBlocks().forEach(e -> {
                ResourceKey<Block> itemResourceKey = e.unwrapKey().get();
                ResourceLocation location = itemResourceKey.location();
                String path = location.getPath();
                String prettyName = toTitleCase(path.replace("_", " "));

                if (prettyName.endsWith(" Block") && !prettyName.endsWith(" Raw Block")) {
                    prettyName = "Block of " + prettyName.replace(" Block", "");
                }

                translationBuilder.add("block." + location.getNamespace() + "." + location.getPath(), prettyName);
            });
        }

        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            holder.getItems().forEach(e -> {
                ResourceKey<Item> itemResourceKey = e.unwrapKey().get();
                ResourceLocation location = itemResourceKey.location();
                String path = location.getPath();
                String prettyName = toTitleCase(path.replace("_", " "));

                if (prettyName.endsWith("Raw Ore")) {
                    prettyName = "Raw " + prettyName.replace("Raw Ore", "");
                }

                translationBuilder.add("item." + location.getNamespace() + "." + location.getPath(), prettyName);
            });
        }
    }

    private static String toTitleCase(String input) {
        return Arrays.stream(input.split(" "))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
