package dev.ftb.mods.ftbmaterials.config;

import dev.ftb.mods.ftblibrary.config.value.BooleanValue;
import dev.ftb.mods.ftblibrary.config.value.Config;
import dev.ftb.mods.ftbmaterials.FTBMaterials;

public interface StartupConfig {
    String KEY = FTBMaterials.MOD_ID + "-startup";

    Config CONFIG = Config.create(KEY);

    Config TWEAKS = CONFIG.addGroup("tweaks");
    BooleanValue TWEAK_RECIPES = TWEAKS.addBoolean("tweak_recipes", true)
            .comment("If true, tweaks recipes during the recipe manager load phase so that all relevant modded inputs & outputs use their FTB Materials counterparts instead. Vanilla materials are not affected.");
    BooleanValue TWEAK_LOOT_TABLES = TWEAKS.addBoolean("tweak_loot_tables", true)
            .comment("If true, tweaks loot tables so that loot generated in chests as well as modded materials dropped from broken blocks are replaced by their FTB Materials counterparts. Vanilla loot is not affected.");
    BooleanValue TWEAK_WORLDGEN = TWEAKS.addBoolean("tweak_worldgen", true)
            .comment("If true, tweaks worldgen so that any modded generated ores are replaced by their FTB Materials counterparts. Vanilla ores are not affected.");
}
