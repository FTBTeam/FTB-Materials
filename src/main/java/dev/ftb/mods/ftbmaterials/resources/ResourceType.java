package dev.ftb.mods.ftbmaterials.resources;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum ResourceType {
    STONE_ORE(true, "{material} Ore", "c:ores", "c:ores_in_ground/stone"),
    DEEPSLATE_ORE(true, "Deepslate {material} Ore", "c:ores", "c:ores_in_ground/deepslate"),
    END_ORE(true,"Endstone {material} Ore", "c:ores", "c:ores_in_ground/end"),
    NETHER_ORE(true, "Nether {material} Ore", "c:ores", "c:ores_in_ground/nether"),
    BLOCK(true, "Block of {material}", "c:storage_blocks"),
    INGOT(false,"{material} Ingot", "c:ingots"),
    RAW_ORE(false,"Raw {material}", "c:raw_materials"),
    RAW_BLOCK(true, "Block of Raw {material}","c:storage_blocks", "", s -> "raw_" + s),
    NUGGET(false, "{material} Nugget", "c:nuggets"),
    DUST(false, "{material} Dust", "c:dusts"),
    PLATE(false, "{material} Plate", "c:plates"),
    GEAR(false, "{material} Gear", "c:gears"),
    ROD(false, "{material} Rod", "c:rods"),
    GEM(false, "{material} Gem", "c:gems"),
    CRYSTAL(false, "{material} Crystal", "c:crystals"),
    WIRE(false, "{material} Wire", "c:wires"),
    SHARD(false, "{material} Shard", "c:shards|mekanism:shards"),
    CLUMP(false, "{material} Clump", "c:clumps"),
    DIRTY_DUST(false, "{material} Dirty Dust", "c:dirty_dusts"),
    BLADE(false, "{material} Blade", "c:blades"),
    CHUNK(false, "{material} Chunk", "c:chunks"),
    CLUSTER(false, "{material} Cluster", "c:clusters"),
    SMALL_DUST(false, "Small {material} Dust", "c:small_dusts"),
    TINY_DUST(false, "Tiny {material} Dust", "c:tiny_dusts"),
    TINY(false, "Tiny {material}", "c:tiny");

    public static final List<ResourceType> ORE_TYPES = List.of(
            STONE_ORE, DEEPSLATE_ORE, NETHER_ORE, END_ORE
    );

    private final boolean block;
    private final String translationText;
    private final List<String> tags;
    private final String extraBlockTag;
    private final Function<String, String> resourceNameMutator;

    ResourceType(boolean block, String translationText, String tags) {
        this(block, translationText, tags, "", Function.identity());
    }

    ResourceType(boolean block, String translationText, String tags, String extraBlockTag) {
        this(block, translationText, tags, extraBlockTag, Function.identity());
    }

    ResourceType(boolean block, String translationText, String tags, String extraBlockTag, Function<String, String> unifiedPrefixMutator) {
        this.block = block;
        this.translationText = translationText;
        this.tags = Arrays.asList(tags.split("\\|"));
        this.extraBlockTag = extraBlockTag;
        this.resourceNameMutator = unifiedPrefixMutator;
    }

    public boolean isBlock() {
        return block;
    }

    public List<String> getTags() {
        return tags;
    }

    public Function<String, String> getResourceNameMutator() {
        return resourceNameMutator;
    }

    public String getTranslationText() {
        return translationText;
    }

    public String getExtraBlockTag() {
        return extraBlockTag;
    }
}
