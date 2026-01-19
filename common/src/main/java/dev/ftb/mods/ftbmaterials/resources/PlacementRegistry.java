package dev.ftb.mods.ftbmaterials.resources;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class PlacementRegistry {
    public static final XRegistry<ConfiguredFeature<?, ?>> FEATURE_REGISTRY = XRegistry.create(Registries.CONFIGURED_FEATURE);
    public static final XRegistry<PlacedFeature> PLACED_FEATURE_REGISTRY = XRegistry.create(Registries.PLACED_FEATURE);

    public static void init() {
        // We need to register based on the config

        // STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

//        OreConfiguration.target();


        FEATURE_REGISTRY.init();
        PLACED_FEATURE_REGISTRY.init();
    }
}
