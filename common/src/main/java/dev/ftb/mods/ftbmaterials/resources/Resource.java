package dev.ftb.mods.ftbmaterials.resources;

import java.util.HashSet;
import java.util.Set;

public enum Resource {
    ALUMINUM(builder().noGem().build()),
    APATITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    BRONZE(builder().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().build()),
    CHARCOAL(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    CONSTANTAN(builder().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    COPPER(builder().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().build()),
    DIAMOND(builder().noIngot().noRawOre().noRawBlock().noBlade().build()),
    ELECTRUM(builder().noRawOre().noRawBlock().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    GOLD(builder().noBlade().build()),
    GRAPHITE(builder().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    INVAR(builder().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    IRON(builder().noGem().build()),
    LEAD(builder().noGem().noBlade().build()),
    LUMIUM(builder().noRawOre().noRawBlock().noGem().noBlade().build()),
    NETHERITE(builder().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    NICKEL(builder().noGem().noBlade().build()),
    OBSIDIAN(builder().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    OSMIUM(builder().noGem().noBlade().build()),
    QUARTZ(builder().noIngot().noRawOre().noRawBlock().noGem().noBlade().build()),
    REFINED_GLOWSTONE(builder().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    SIGNALUM(builder().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    SILVER(builder().noBlade().build()),
    STEEL(builder().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    SULFUR(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noBlade().build()),
    TIN(builder().noGem().build()),
    URANIUM(builder().noGem().noBlade().build()),
    ZINC(builder().noGem().noBlade().build()),
    REDSTONE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    FLUORITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    EMERALD(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    LAPIS_LAZULI(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    RESONATING_ORE(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    DIMENSIONAL_SHARD(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    ANTIMONY(builder().noPlate().noGear().noRod().noGem().noBlade().build()),
    BAUXITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    IRIDIUM(builder().noGear().noRod().noGem().noBlade().build()),
    MONAZITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    TUNGSTEN(builder().noGear().noGem().noBlade().build()),
    TITANIUM(builder().noGem().build()),
    PLATINUM(builder().noGem().noBlade().build()),
    STAINLESS_STEEL(builder().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().build());

    private final int resourceComponents;

    Resource(int resourceComponents) {
        this.resourceComponents = resourceComponents;
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

        public int build() {
            return resourceComponents;
        }
    }
}
