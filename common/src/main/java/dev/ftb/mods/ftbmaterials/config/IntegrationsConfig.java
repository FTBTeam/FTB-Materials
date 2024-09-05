package dev.ftb.mods.ftbmaterials.config;

import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftbmaterials.FTBMaterials;

public class IntegrationsConfig {
    private static final SNBTConfig CONFIG = SNBTConfig.create("integrations-config");

    private static final SNBTConfig INTEGRATIONS = CONFIG.addGroup("integrations")
            .comment(
                    "When ever an integration is enabled, we'll disable the mods world generation in favor of our own.",
                    "We'll also attempt to replace recipe inputs and outputs with our own materials."
            );

    public static final BooleanValue MEK_ENABLED = INTEGRATIONS.addBoolean("mekanism", true);
    public static final BooleanValue MI_ENABLED = INTEGRATIONS.addBoolean("modern_industrialization", true);
    public static final BooleanValue THERMAL_ENABLED = INTEGRATIONS.addBoolean("thermal_series", true);

    public static void init() {
        ConfigUtil.loadDefaulted(CONFIG, Platform.getConfigFolder().resolve(FTBMaterials.MOD_ID), FTBMaterials.MOD_ID, "integrations.snbt");
    }
}
