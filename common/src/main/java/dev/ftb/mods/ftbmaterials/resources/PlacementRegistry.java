package dev.ftb.mods.ftbmaterials.resources;

import dev.architectury.registry.registries.DeferredRegister;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class PlacementRegistry {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> FEATURE_REGISTRY = DeferredRegister.create(FTBMaterials.MOD_ID, Registries.CONFIGURED_FEATURE);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURE_REGISTRY = DeferredRegister.create(FTBMaterials.MOD_ID, Registries.PLACED_FEATURE);

    public static void init() {
        // We need to register based on the config

        // STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

//        OreConfiguration.target();


        FEATURE_REGISTRY.register();
        PLACED_FEATURE_REGISTRY.register();
    }
}
