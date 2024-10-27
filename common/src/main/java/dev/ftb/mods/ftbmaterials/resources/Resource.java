package dev.ftb.mods.ftbmaterials.resources;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public enum Resource {
    ALUMINUM(builder().noGem().build()),
    APATITE(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    BRONZE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().build()),
    CHARCOAL(builder().noOres().noIngot().noBlock().noRawOre().noWire().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    CONSTANTAN(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    COPPER(builder().noDeepslateOre().noStoneOre().noBlock().noIngot().noRawOre().noRawBlock().noPlate().noGear().noRod().noGem().build()),
    DIAMOND(ResourceLocation.withDefaultNamespace("needs_iron_tool"),  builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noBlade().build()),
    ELECTRUM(builder().noOres().noRawOre().noRawBlock().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    GOLD(ResourceLocation.withDefaultNamespace("needs_iron_tool"),  builder().noDeepslateOre().noStoneOre().noBlock().noIngot().noNugget().noRawOre().noRawBlock().noBlade().build()),
    GRAPHITE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    INVAR(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    IRON(builder().noRawOre().noStoneOre().noDeepslateOre().noRawBlock().noBlock().noIngot().noNugget().noGem().build()),
    LEAD(builder().noGem().noBlade().build()),
    LUMIUM(builder().noOres().noRawOre().noRawBlock().noGem().noBlade().build()),
    NETHERITE(ResourceLocation.withDefaultNamespace("needs_diamond_tool"),  builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noIngot().noBlock().build()),
    NICKEL(builder().noGem().noBlade().build()),
    OBSIDIAN(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().noBlock().build()),
    OSMIUM(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noGem().noBlade().build()),
    QUARTZ(builder().noCrystal().noBlock().noNetherOre().noIngot().noRawOre().noRawBlock().noGem().noBlade().build()),
    REFINED_GLOWSTONE(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    SIGNALUM(builder().noOres().noBlock().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    SILVER(builder().noBlade().build()),
    STEEL(builder().noOres().noRawOre().noRawBlock().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    SULFUR(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noGem().noBlade().build()),
    TIN(builder().noGem().build()),
    URANIUM(builder().noGem().noBlade().build()),
    ZINC(builder().noGem().noBlade().build()),
    REDSTONE(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noBlock().noDust().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    FLUORITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    EMERALD(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    LAPIS_LAZULI(builder().noGem().noBlock().noDeepslateOre().noStoneOre().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    RESONATING_ORE(builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    DIMENSIONAL_SHARD(ResourceLocation.withDefaultNamespace("needs_iron_tool"), builder().noIngot().noRawOre().noRawBlock().noNugget().noDust().noPlate().noGear().noRod().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    ANTIMONY(builder().noPlate().noGear().noRod().noGem().noBlade().build()),
    BAUXITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    IRIDIUM(builder().noGear().noRod().noGem().noBlade().build()),
    MONAZITE(builder().noIngot().noRawOre().noRawBlock().noNugget().noPlate().noGear().noRod().noGem().noCrystal().noShard().noClump().noDirtyDust().noBlade().build()),
    TUNGSTEN(builder().noGear().noGem().noBlade().build()),
    TITANIUM(builder().noGem().build()),
    PLATINUM(builder().noGem().noBlade().build()),
    STAINLESS_STEEL(builder().noOres().noIngot().noRawOre().noRawBlock().noNugget().noGem().noCrystal().noShard().noClump().noDirtyDust().build()),
    PLUTONIUM(builder().noOres().build()),
    CHROMIUM(builder().noOres().build()),
    SILICON(builder().noOres().build()),
    BRASS(builder().noOres().build());

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

        public int build() {
            return resourceComponents;
        }
    }
}
