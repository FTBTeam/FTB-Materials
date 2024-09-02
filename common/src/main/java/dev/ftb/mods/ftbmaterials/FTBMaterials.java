package dev.ftb.mods.ftbmaterials;

import dev.ftb.mods.ftbmaterials.config.GeneralConfig;
import dev.ftb.mods.ftbmaterials.config.GenerationConfig;
import dev.ftb.mods.ftbmaterials.config.IntegrationsConfig;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModCreativeTab;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;

public final class FTBMaterials {
    public static final String MOD_ID = "ftbmaterials";

    public static void init() {
        GeneralConfig.init();
        GenerationConfig.init();
        IntegrationsConfig.init();

        // Write common init code here.
        ModCreativeTab.REGISTRY.register();

        ResourceRegistry.init();
        ModBlocks.REGISTRY.register();
        ModItems.REGISTRY.register();
    }
}
