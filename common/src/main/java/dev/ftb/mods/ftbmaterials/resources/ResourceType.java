package dev.ftb.mods.ftbmaterials.resources;

public enum ResourceType {
    STONE_ORE(0x00),
    DEEPSLATE_ORE(0x01),
    END_ORE(0x02),
    NETHER_ORE(0x03),
    BLOCK(0x04),
    INGOT(0x05),
    RAW_ORE(0x06),
    RAW_BLOCK(0x07),
    NUGGET(0x08),
    DUST(0x09),
    PLATE(0x0A),
    GEAR(0x0B),
    ROD(0x0C),
    GEM(0x0D),
    CRYSTAL(0x0E),
    WIRE(0x0F),
    SHARD(0x10),
    CLUMP(0x11),
    DIRTY_DUST(0x12),
    BLADE(0x13);

    private final int id;

    ResourceType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
