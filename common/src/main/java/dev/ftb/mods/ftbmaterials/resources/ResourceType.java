package dev.ftb.mods.ftbmaterials.resources;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public enum ResourceType {
    STONE_ORE(0x00, "ores", List.of("c:ores", "c:ores_in_ground/stone")),
    DEEPSLATE_ORE(0x01, "ores", List.of("c:ores", "c:ores_in_ground/deepslate")),
    END_ORE(0x02, "ores", List.of("c:ores", "c:ores_in_ground/end")),
    NETHER_ORE(0x03, "ores", List.of("c:ores", "c:ores_in_ground/nether")),
    BLOCK(0x04, "storage_blocks"),
    INGOT(0x05, "ingots"),
    RAW_ORE(0x06, "raw_materials"),
    RAW_BLOCK(0x07, "storage_blocks", List.of(), s -> "raw_" + s),
    NUGGET(0x08, "nuggets"),
    DUST(0x09, "dusts"),
    PLATE(0x0A, "plates"),
    GEAR(0x0B, "gears"),
    ROD(0x0C, "rods"),
    GEM(0x0D, "gems"),
    CRYSTAL(0x0E, "crystals"),
    WIRE(0x0F, "wires"),
    SHARD(0x10, "shards|mekanism:shards"),
    CLUMP(0x11, "clumps"),
    DIRTY_DUST(0x12, "dirty_dusts"),
    BLADE(0x13, "blades");

    private final int id;

    @Nullable
    private final String unifiedTagPrefix;
    private List<String> tags = List.of();

    @Nullable
    private Function<String, String> resourceNameMutator = null;

    ResourceType(int id) {
        this.id = id;
        this.unifiedTagPrefix = null;
    }

    ResourceType(int id, @Nullable String unifiedTagPrefix) {
        this.id = id;
        this.unifiedTagPrefix = unifiedTagPrefix;
    }

    ResourceType(int id, @Nullable String unifiedTagPrefix, List<String> tags) {
        this.id = id;
        this.unifiedTagPrefix = unifiedTagPrefix;
        this.tags = tags;
    }

    ResourceType(int id, @Nullable String unifiedTagPrefix, List<String> tags, @Nullable Function<String, String> unifiedPrefixMutator) {
        this.id = id;
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
}
