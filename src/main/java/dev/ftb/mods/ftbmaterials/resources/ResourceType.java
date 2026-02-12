package dev.ftb.mods.ftbmaterials.resources;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public enum ResourceType {
    STONE_ORE(0x00, true, "{material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/stone")),
    DEEPSLATE_ORE(0x01, true, "Deepslate {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/deepslate")),
    END_ORE(0x02, true,"Endstone {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/end")),
    NETHER_ORE(0x03, true, "Nether {material} Ore", "ores", List.of("c:ores", "c:ores_in_ground/nether")),
    BLOCK(0x04, true, "Block of {material}", "storage_blocks"),
    INGOT(0x05, false,"{material} Ingot", "ingots"),
    RAW_ORE(0x06, false,"Raw {material}", "raw_materials"),
    RAW_BLOCK(0x07, true, "Block of Raw {material}", "storage_blocks", List.of(), s -> "raw_" + s),
    NUGGET(0x08,false, "{material} Nugget", "nuggets"),
    DUST(0x09,false, "{material} Dust", "dusts"),
    PLATE(0x0A,false, "{material} Plate", "plates"),
    GEAR(0x0B,false, "{material} Gear", "gears"),
    ROD(0x0C,false, "{material} Rod", "rods"),
    GEM(0x0D,false, "{material} Gem", "gems"),
    CRYSTAL(0x0E,false, "{material} Crystal", "crystals"),
    WIRE(0x0F,false, "{material} Wire", "wires"),
    SHARD(0x10,false, "{material} Shard", "shards|mekanism:shards"),
    CLUMP(0x11,false, "{material} Clump", "clumps"),
    DIRTY_DUST(0x12,false, "{material} Dirty Dust", "dirty_dusts"),
    BLADE(0x13,false, "{material} Blade", "blades"),
    CHUNK(0x14,false, "{material} Chunk", "chunks"),
    CLUSTER(0x15,false, "{material} Cluster", "clusters"),
    SMALL_DUST(0x16,false, "Small {material} Dust", "small_dusts"),
    TINY_DUST(0x17,false, "Tiny {material} Dust", "tiny_dusts"),
    TINY(0x18,false, "Tiny {material}", "tiny");

    private final int id;
    private final boolean block;
    @Nullable
    private final String unifiedTagPrefix;
    private final String translationText;
    private final List<String> tags;
    private final Function<String, String> resourceNameMutator;

    ResourceType(int id, boolean block, String translationText) {
        this(id, block, translationText, null);
    }

    ResourceType(int id, boolean block, String translationText, @Nullable String unifiedTagPrefix) {
        this(id, block, translationText, unifiedTagPrefix, List.of());
    }

    ResourceType(int id, boolean block, String translationText, @Nullable String unifiedTagPrefix, List<String> tags) {
        this(id, block, translationText, unifiedTagPrefix, tags, Function.identity());
    }

    ResourceType(int id, boolean block, String translationText, @Nullable String unifiedTagPrefix, List<String> tags, Function<String, String> unifiedPrefixMutator) {
        this.id = id;
        this.block = block;
        this.translationText = translationText;
        this.unifiedTagPrefix = unifiedTagPrefix;
        this.tags = tags;
        this.resourceNameMutator = unifiedPrefixMutator;
    }

    public int getId() {
        return id;
    }

    public boolean isBlock() {
        return block;
    }

    public @Nullable String getUnifiedTagPrefix() {
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
