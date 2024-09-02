package dev.ftb.mods.ftbmaterials.config;

import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftbmaterials.FTBMaterials;

public class IntegrationsConfig {
    static SNBTConfig CONFIG = SNBTConfig.create("integrations-config");

    public static void init() {
        ConfigUtil.loadDefaulted(CONFIG, Platform.getConfigFolder(), FTBMaterials.MOD_ID, "integrations-config.snbt");
    }
}
