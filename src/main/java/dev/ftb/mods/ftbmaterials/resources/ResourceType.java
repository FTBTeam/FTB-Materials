package dev.ftb.mods.ftbmaterials.resources;

import java.util.List;
import java.util.function.Function;

public enum ResourceType {
    STONE_ORE(true, "{material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/stone")),
    DEEPSLATE_ORE(true, "Deepslate {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/deepslate")),
    END_ORE(true,"Endstone {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/end")),
    NETHER_ORE(true, "Nether {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/nether")),
    BLOCK(true, "Block of {material}", "storage_blocks"),
    INGOT(false,"{material} Ingot", "ingots"),
    RAW_ORE(false,"Raw {material}", "raw_materials"),
    RAW_BLOCK(true, "Block of Raw {material}", "storage_blocks", List.of(), s -> "raw_" + s),
    NUGGET(false, "{material} Nugget", "nuggets"),
    DUST(false, "{material} Dust", "dusts"),
    PLATE(false, "{material} Plate", "plates"),
    GEAR(false, "{material} Gear", "gears"),
    ROD(false, "{material} Rod", "rods"),
    GEM(false, "{material} Gem", "gems"),
    CRYSTAL(false, "{material} Crystal", "crystals"),
    WIRE(false, "{material} Wire", "wires"),
    SHARD(false, "{material} Shard", "shards|mekanism:shards"),
    CLUMP(false, "{material} Clump", "clumps"),
    DIRTY_DUST(false, "{material} Dirty Dust", "dirty_dusts"),
    BLADE(false, "{material} Blade", "blades"),
    CHUNK(false, "{material} Chunk", "chunks"),
    CLUSTER(false, "{material} Cluster", "clusters"),
    SMALL_DUST(false, "Small {material} Dust", "small_dusts"),
    TINY_DUST(false, "Tiny {material} Dust", "tiny_dusts"),
    TINY(false, "Tiny {material}", "tiny");

    private final boolean block;
    private final String unifiedTagPrefix;
    private final String translationText;
    private final List<String> tags;
    private final Function<String, String> resourceNameMutator;

//    ResourceType(boolean block, String translationText) {
//        this(block, translationText, "");
//    }

    ResourceType(boolean block, String translationText, String unifiedTagPrefix) {
        this(block, translationText, unifiedTagPrefix, List.of());
    }

    ResourceType(boolean block, String translationText, String unifiedTagPrefix, List<String> tags) {
        this(block, translationText, unifiedTagPrefix, tags, Function.identity());
    }

    ResourceType(boolean block, String translationText, String unifiedTagPrefix, List<String> tags, Function<String, String> unifiedPrefixMutator) {
        this.block = block;
        this.translationText = translationText;
        this.unifiedTagPrefix = unifiedTagPrefix;
        this.tags = tags;
        this.resourceNameMutator = unifiedPrefixMutator;
    }

    public boolean isBlock() {
        return block;
    }

    public String getUnifiedTagPrefix() {
        return unifiedTagPrefix;
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
}
