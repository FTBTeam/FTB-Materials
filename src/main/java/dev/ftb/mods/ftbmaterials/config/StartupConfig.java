package dev.ftb.mods.ftbmaterials.config;

import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftblibrary.snbt.config.StringListValue;
import dev.ftb.mods.ftbmaterials.FTBMaterials;

import java.util.ArrayList;
import java.util.HashMap;

public interface StartupConfig {
    String KEY = FTBMaterials.MOD_ID + "-startup";

    SNBTConfig CONFIG = SNBTConfig.create(KEY);

    SNBTConfig TWEAKS = CONFIG.addGroup("tweaks");
    BooleanValue TWEAK_RECIPES = TWEAKS.addBoolean("tweak_recipes", true)
            .comment("If true, tweaks recipes during the recipe manager load phase so that all relevant modded inputs & outputs use their FTB Materials counterparts instead. Vanilla materials are not affected.");
    BooleanValue TWEAK_LOOT_TABLES = TWEAKS.addBoolean("tweak_loot_tables", true)
            .comment("If true, tweaks loot tables so that loot generated in chests as well as modded materials dropped from broken blocks are replaced by their FTB Materials counterparts. Vanilla loot is not affected.");
    BooleanValue TWEAK_WORLDGEN = TWEAKS.addBoolean("tweak_worldgen", true)
            .comment("If true, tweaks worldgen so that any modded generated ores are replaced by their FTB Materials counterparts. Vanilla ores are not affected.");

    SNBTConfig BLACKLISTS = CONFIG.addGroup("blacklists");
    StringListValue UNIFICATION_BLACKLIST_ITEMS = BLACKLISTS.addStringList("unification_blacklist_items", new ArrayList<>())
            .comment("List of item IDs which should never be automatically added to the unification DB",
                    "These can be wildcarded, e.g. 'somemod:*' blacklists all id's in the 'somemod' namespace");
    StringListValue UNIFICATION_BLACKLIST_TAGS = BLACKLISTS.addStringList("unification_blacklist_item_tags", new ArrayList<>())
            .comment("List of item tag IDs which should never be automatically added to the unification DB",
                    "These can be wildcarded, e.g. 'somemod:*' blacklists all id's in the 'somemod' namespace");
    StringListValue UNIFICATION_BLACKLIST_BLOCKS = BLACKLISTS.addStringList("unification_blacklist_blocks", new ArrayList<>())
            .comment("List of block IDs which should never be automatically added to the unification DB",
                    "These can be wildcarded, e.g. 'somemod:*' blacklists all id's in the 'somemod' namespace");

    SNBTConfig OVERRIDES = CONFIG.addGroup("overrides");
    StringStringMapValue ITEM_OVERRIDES = OVERRIDES.add(new StringStringMapValue(OVERRIDES, "item_overrides", new HashMap<>()))
            .comment("Map of <mod-id> -> map of <tag> -> <replacement_tag>",
                    "Overrides applied to recipe types of the given mod when doing item lookup in the unification DB");
    StringStringMapValue TAG_OVERRIDES = OVERRIDES.add(new StringStringMapValue(OVERRIDES, "tag_overrides", new HashMap<>()))
            .comment("Map of <mod-id> -> map of <item> -> <replacement_item>",
                    "Overrides applied to recipe types of the given mod when doing item tag lookup in the unification DB");

    static String itemOverride(String in, String modId) {
        var map = ITEM_OVERRIDES.get().get(modId);
        return map != null ? map.getOrDefault(in, in) : in;
    }

    static String itemTagOverride(String in, String modId) {
        var map = TAG_OVERRIDES.get().get(modId);
        return map != null ? map.getOrDefault(in, in) : in;
    }
}
