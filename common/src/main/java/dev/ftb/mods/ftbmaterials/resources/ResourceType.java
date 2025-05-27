package dev.ftb.mods.ftbmaterials.resources;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public enum ResourceType {
    STONE_ORE(0x00, "{material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/stone")),
    DEEPSLATE_ORE(0x01, "Deepslate {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/deepslate")),
    END_ORE(0x02,"Endstone {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/end")),
    NETHER_ORE(0x03, "Nether {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/nether")),
    BLOCK(0x04, "Block of {material}", "storage_blocks"),
    INGOT(0x05, "{material} Ingot", "ingots"),
    RAW_ORE(0x06, "Raw {material}", "raw_materials"),
    RAW_BLOCK(0x07, "Block of Raw {material}", "storage_blocks", List.of(), s -> "raw_" + s),
    NUGGET(0x08, "{material} Nugget", "nuggets"),
    DUST(0x09, "{material} Dust", "dusts"),
    PLATE(0x0A, "{material} Plate", "plates"),
    GEAR(0x0B, "{material} Gear", "gears"),
    ROD(0x0C, "{material} Rod", "rods"),
    GEM(0x0D, "{material} Gem", "gems"),
    CRYSTAL(0x0E, "{material} Crystal", "crystals"),
    WIRE(0x0F, "{material} Wire", "wires"),
    SHARD(0x10, "{material} Shard", "shards|mekanism:shards"),
    CLUMP(0x11, "{material} Clump", "clumps"),
    DIRTY_DUST(0x12, "{material} Dirty Dust", "dirty_dusts"),
    BLADE(0x13, "{material} Blade", "blades"),
    CHUNK(0x14, "{material} Chunk", "chunks"),
    CLUSTER(0x15, "{material} Cluster", "clusters"),
    SMALL_DUST(0x16, "Small {material} Dust", "small_dusts"),
    TINY_DUST(0x17, "Tiny {material} Dust", "tiny_dusts"),;

    private final int id;

    @Nullable
    private final String unifiedTagPrefix;
    private final String translationText;

    private List<String> tags = List.of();

    @Nullable
    private Function<String, String> resourceNameMutator = null;

    ResourceType(int id, String translationText) {
        this.id = id;
        this.translationText = translationText;
        this.unifiedTagPrefix = null;
    }

    ResourceType(int id, String translationText, @Nullable String unifiedTagPrefix) {
        this.id = id;
        this.translationText = translationText;
        this.unifiedTagPrefix = unifiedTagPrefix;
    }

    ResourceType(int id, String translationText, @Nullable String unifiedTagPrefix, List<String> tags) {
        this.id = id;
        this.translationText = translationText;
        this.unifiedTagPrefix = unifiedTagPrefix;
        this.tags = tags;
    }

    ResourceType(int id, String translationText, @Nullable String unifiedTagPrefix, List<String> tags, @Nullable Function<String, String> unifiedPrefixMutator) {
        this.id = id;
        this.translationText = translationText;
        this.unifiedTagPrefix = unifiedTagPrefix;
        this.tags = tags;
        this.resourceNameMutator = unifiedPrefixMutator;
    }

    public int getId() {
        return id;
    }

    public @Nullable String getUnifiedTagPrefix() {
        return unifiedTagPrefix;
    }

    public List<String> getTags() {
        return tags;
    }

    public @Nullable Function<String, String> getResourceNameMutator() {
        return resourceNameMutator;
    }

    public String getTranslationText() {
        return translationText;
    }
}
