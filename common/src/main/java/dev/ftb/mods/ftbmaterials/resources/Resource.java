package dev.ftb.mods.ftbmaterials.resources;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public enum Resource {
    ALUMINUM(builder().noGem().noTiny().build()),
    APATITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    BRONZE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noChunk().noCluster().noTiny().build()),
    CHARCOAL(builder().noOres().noIngot().noRawOre().noWire().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    CONSTANTAN(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noTiny().build()),
    COPPER(builder().noDeepslateOre().noStoneOre().noBlock().noIngot().noRawOre().noRawBlock().noGem().noTiny().build()),
    DIAMOND(new ResourceLocation("needs_iron_tool"),  builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noBlade().noTiny().build()),
    ELECTRUM(builder().noOres().noRawOre().noRawBlock().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noTiny().build()),
    GOLD(new ResourceLocation("needs_iron_tool"),  builder().noDeepslateOre().noStoneOre().noBlock().noIngot().noNugget().noRawOre().noRawBlock().noBlade().noTiny().build()),
    GRAPHITE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noTiny().build()),
    INVAR(new ResourceLocation("needs_iron_tool"), builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noTiny().build()),
    IRON(builder().noRawOre().noStoneOre().noDeepslateOre().noRawBlock().noBlock().noIngot().noNugget().noGem().noTiny().build()),
    LEAD(builder().noGem().noBlade().noTiny().build()),
    LUMIUM(builder().noOres().noRawOre().noRawBlock().noGem().noBlade().noChunk().noCluster().noTiny().build()),
    NETHERITE(new ResourceLocation("needs_diamond_tool"),  builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noIngot().noBlock().noChunk().noCluster().noTiny().build()),
    NICKEL(builder().noGem().noBlade().noTiny().build()),
    OBSIDIAN(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noBlock().noChunk().noCluster().noTiny().build()),
    OSMIUM(new ResourceLocation("needs_iron_tool"), builder().noGem().noBlade().noTiny().build()),
    QUARTZ(builder().noCrystal().noBlock().noNetherOre().noIngot().noRawOre().noRawBlock().noGem().noBlade().noTiny().build()),
    REFINED_GLOWSTONE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noChunk().noCluster().noTiny().build()),
    SIGNALUM(builder().noOres().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    SILVER(builder().noBlade().noTiny().build()),
    STEEL(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noTiny().build()),
    SULFUR(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    TIN(builder().noGem().noTiny().build()),
    URANIUM(builder().noGem().noBlade().noTiny().build()),
    ZINC(builder().noGem().noBlade().noTiny().build()),
    REDSTONE(new ResourceLocation("needs_iron_tool"), builder().noBlock().noDust().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noTiny().build()),
    FLUORITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noTiny().build()),
    EMERALD(new ResourceLocation("needs_iron_tool"), builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noSmallDust().noTiny().build()),
    LAPIS_LAZULI(builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noSmallDust().noTiny().build()),
    RESONATING_ORE(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    DIMENSIONAL_SHARD(new ResourceLocation("needs_iron_tool"), builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    ANTIMONY(builder().noPlate().noGear().noRod().noGem().noBlade().noChunk().noCluster().noTiny().build()),
    BAUXITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noTiny().build()),
    IRIDIUM(builder().noGear().noRod().noGem().noBlade().noChunk().noCluster().noTiny().build()),
    MONAZITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noTiny().build()),
    TUNGSTEN(builder().noGear().noGem().noBlade().noChunk().noCluster().noTiny().build()),
    TITANIUM(builder().noGem().noChunk().noCluster().noTiny().build()),
    PLATINUM(builder().noGem().noBlade().noTiny().build()),
    STAINLESS_STEEL(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noChunk().noCluster().noTiny().build()),
    PLUTONIUM(builder().noOres().noChunk().noCluster().noTiny().build()),
    CHROMIUM(builder().noOres().noChunk().noCluster().noTiny().build()),
    SILICON(builder().noOres().noChunk().noCluster().noTiny().build()),
    BRASS(builder().noOres().noChunk().noCluster().noTiny().build()),
    COAL(builder().noStoneOre().noDeepslateOre().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().noChunk().noCluster().noBlade().noGear().noRod().noWire().noSmallDust().noTinyDust().build()),
    CINNABAR(builder().noIngot().noRawOre().noRawBlock().noWire().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    NITER(builder().noIngot().noRawOre().noRawBlock().noWire().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    RUBY(builder().noIngot().noRawOre().noRawBlock().noWire().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().build()),
    SALT(builder().noIngot().noRawOre().noRawBlock().noWire().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTiny().build()),
    SAPPHIRE(builder().noIngot().noRawOre().noRawBlock().noWire().noNugget().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTiny().build()),
    COAL_COKE(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noCrystal().noShard().noClump().noDirtyDust().noChunk().noCluster().noBlade().noGear().noRod().noWire().noPlate().noSmallDust().noTinyDust().noTiny().build()),
    REFINED_OBSIDIAN(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noChunk().noCluster().noBlade().noGear().noRod().noWire().noPlate().noSmallDust().noTinyDust().noTiny().build()),
    SAW(builder().noOres().noStoneOre().noDeepslateOre().noEndOre().noNetherOre().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noWire().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    LITHIUM(builder().noOres().noStoneOre().noDeepslateOre().noEndOre().noNetherOre().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noWire().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noTiny().build()),
    TOPAZ(builder().noOres().noStoneOre().noDeepslateOre().noEndOre().noNetherOre().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noWire().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noSmallDust().noTinyDust().noDust().build()),;


    private final int resourceComponents;
    private ResourceLocation breakableWith = new ResourceLocation("minecraft","needs_stone_tool");

    Resource(int resourceComponents) {
        this.resourceComponents = resourceComponents;
    }

    Resource(ResourceLocation breakableWith, int resourceComponents) {
        this.resourceComponents = resourceComponents;
        this.breakableWith = breakableWith;
    }

    public boolean hasComponent(ResourceType type) {
        return (resourceComponents & (1 << type.getId())) != 0;
    }

    public Set<ResourceType> getComponents() {
        var types = new HashSet<ResourceType>();
        for (var type : ResourceType.values()) {
            if (hasComponent(type)) {
                types.add(type);
            }
        }
        return types;
    }

    public ResourceLocation getBreakableWith() {
        return breakableWith;
    }

    private static ResourceBuilder builder() {
        return new ResourceBuilder();
    }

    private static class ResourceBuilder {
        private int resourceComponents = 0;

        public ResourceBuilder() {
            // Add all the ResourceTypes by default
            for (var type : ResourceType.values()) {
                resourceComponents |= 1 << type.getId();
            }
        }

        public ResourceBuilder noType(ResourceType type) {
            // Remove the ResourceType from the resourceComponents
            resourceComponents &= ~(1 << type.getId());
            return this;
        }

        public ResourceBuilder noOres() {
            return noStoneOre().noDeepslateOre().noEndOre().noNetherOre();
        }

        public ResourceBuilder noStoneOre() {
            return noType(ResourceType.STONE_ORE);
        }

        public ResourceBuilder noDeepslateOre() {
            return noType(ResourceType.DEEPSLATE_ORE);
        }

        public ResourceBuilder noEndOre() {
            return noType(ResourceType.END_ORE);
        }

        public ResourceBuilder noNetherOre() {
            return noType(ResourceType.NETHER_ORE);
        }

        public ResourceBuilder noBlock() {
            return noType(ResourceType.BLOCK);
        }

        public ResourceBuilder noIngot() {
            return noType(ResourceType.INGOT);
        }

        public ResourceBuilder noRawOre() {
            return noType(ResourceType.RAW_ORE);
        }

        public ResourceBuilder noRawBlock() {
            return noType(ResourceType.RAW_BLOCK);
        }

        public ResourceBuilder noNugget() {
            return noType(ResourceType.NUGGET);
        }

        public ResourceBuilder noDust() {
            return noType(ResourceType.DUST);
        }

        public ResourceBuilder noPlate() {
            return noType(ResourceType.PLATE);
        }

        public ResourceBuilder noGear() {
            return noType(ResourceType.GEAR);
        }

        public ResourceBuilder noRod() {
            return noType(ResourceType.ROD);
        }

        public ResourceBuilder noGem() {
            return noType(ResourceType.GEM);
        }

        public ResourceBuilder noCrystal() {
            return noType(ResourceType.CRYSTAL);
        }

        public ResourceBuilder noWire() {
            return noType(ResourceType.WIRE);
        }

        public ResourceBuilder noShard() {
            return noType(ResourceType.SHARD);
        }

        public ResourceBuilder noClump() {
            return noType(ResourceType.CLUMP);
        }

        public ResourceBuilder noDirtyDust() {
            return noType(ResourceType.DIRTY_DUST);
        }

        public ResourceBuilder noBlade() {
            return noType(ResourceType.BLADE);
        }

        public ResourceBuilder noChunk() {
            return noType(ResourceType.CHUNK);
        }

        public ResourceBuilder noCluster() {
            return noType(ResourceType.CLUSTER);
        }

        public ResourceBuilder noSmallDust() {
            return noType(ResourceType.SMALL_DUST);
        }
        
        public ResourceBuilder noTinyDust() {
            return noType(ResourceType.TINY_DUST);
        }

        public ResourceBuilder noTiny() {
            return noType(ResourceType.TINY);
        }

        public int build() {
            return resourceComponents;
        }
    }
}
