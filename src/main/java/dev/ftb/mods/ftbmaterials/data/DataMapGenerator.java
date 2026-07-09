package dev.ftb.mods.ftbmaterials.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider {
    protected DataMapGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        TagKey<Item> tinyCoal = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tiny/coal"));
        TagKey<Item> tinyCharcoal = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tiny/charcoal"));

        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(tinyCharcoal, new FurnaceFuel(200), false)
                .add(tinyCoal, new FurnaceFuel(200), false)
                .build();
    }
}
