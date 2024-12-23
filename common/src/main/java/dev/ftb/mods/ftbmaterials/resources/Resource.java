package dev.ftb.mods.ftbmaterials.resources;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public enum Resource {
    ALUMINUM(builder().noGem().build()),
    APATITE(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    BRONZE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noChunk().noCluster().build()),
    CHARCOAL(builder().noOres().noIngot().noBlock().noRawOre().noWire().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    CONSTANTAN(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    COPPER(builder().noDeepslateOre().noStoneOre().noBlock().noIngot().noRawOre().noRawBlock().noPlate().noGear().noRod().noGem().build()),
    DIAMOND(ResourceLocation.withDefaultNamespace("needs_iron_tool"),  builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noBlade().build()),
    ELECTRUM(builder().noOres().noRawOre().noRawBlock().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    GOLD(ResourceLocation.withDefaultNamespace("needs_iron_tool"),  builder().noDeepslateOre().noStoneOre().noBlock().noIngot().noNugget().noRawOre().noRawBlock().noBlade().build()),
    GRAPHITE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    INVAR(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    IRON(builder().noRawOre().noStoneOre().noDeepslateOre().noRawBlock().noBlock().noIngot().noNugget().noGem().build()),
    LEAD(builder().noGem().noBlade().build()),
    LUMIUM(builder().noOres().noRawOre().noRawBlock().noGem().noBlade().noChunk().noCluster().build()),
    NETHERITE(ResourceLocation.withDefaultNamespace("needs_diamond_tool"),  builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noIngot().noBlock().noChunk().noCluster().build()),
    NICKEL(builder().noGem().noBlade().build()),
    OBSIDIAN(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noBlock().noChunk().noCluster().build()),
    OSMIUM(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noGem().noBlade().build()),
    QUARTZ(builder().noCrystal().noBlock().noNetherOre().noIngot().noRawOre().noRawBlock().noGem().noBlade().build()),
    REFINED_GLOWSTONE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().noChunk().noCluster().build()),
    SIGNALUM(builder().noOres().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    SILVER(builder().noBlade().build()),
    STEEL(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    SULFUR(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noBlade().noChunk().noCluster().build()),
    TIN(builder().noGem().build()),
    URANIUM(builder().noGem().noBlade().build()),
    ZINC(builder().noGem().noBlade().noChunk().noCluster().build()),
    REDSTONE(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noBlock().noDust().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    FLUORITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    EMERALD(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    LAPIS_LAZULI(builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    RESONATING_ORE(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    DIMENSIONAL_SHARD(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    ANTIMONY(builder().noPlate().noGear().noRod().noGem().noBlade().noChunk().noCluster().build()),
    BAUXITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    IRIDIUM(builder().noGear().noRod().noGem().noBlade().noChunk().noCluster().build()),
    MONAZITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noChunk().noCluster().build()),
    TUNGSTEN(builder().noGear().noGem().noBlade().noChunk().noCluster().build()),
    TITANIUM(builder().noGem().noChunk().noCluster().build()),
    PLATINUM(builder().noGem().noBlade().build()),
    STAINLESS_STEEL(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().noChunk().noCluster().build()),
    PLUTONIUM(builder().noOres().noChunk().noCluster().build()),
    CHROMIUM(builder().noOres().noChunk().noCluster().build()),
    SILICON(builder().noOres().noChunk().noCluster().build()),
    BRASS(builder().noOres().noChunk().noCluster().build());

    private final int resourceComponents;
    private ResourceLocation breakableWith = ResourceLocation.withDefaultNamespace("needs_stone_tool");

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

        public int build() {
            return resourceComponents;
        }
    }
}
