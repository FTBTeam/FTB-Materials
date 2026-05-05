package dev.ftb.mods.ftbmaterials.resources;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Resource {
    ALUMINUM(builder().gem(false).tiny(false)
            .build()),
    APATITE(emptyBuilder()
            .block(true).ores(true)
            .dust(true).gem(true).wire(true)
            .build()),
    BRONZE(emptyBuilder().allCraftedProducts().allDusts().blade(true)
            .build()),
    CHARCOAL(emptyBuilder().block(true).allDusts().tiny(true)
            .build()),
    CONSTANTAN(emptyBuilder().allCraftedProducts().allDusts()
            .build()),
    COPPER(emptyBuilder().netherOre(true).endOre(true)
            .allCraftedProducts().ingot(false).block(false).nugget(false)
            .allProcessingProducts().chunkAndCluster().allDusts()
            .blade(true)
            .build()),
    DIAMOND("iron", emptyBuilder().netherOre(true).endOre(true)
            .allCraftedProducts().ingot(false).block(false)
            .allProcessingProducts().chunkAndCluster().allDusts()
            .build()),
    ELECTRUM(emptyBuilder().allCraftedProducts().allDusts().gem(true)
            .build()),
    GOLD("iron",  emptyBuilder().netherOre(true).endOre(true)
            .allCraftedProducts().ingot(false).block(false).nugget(false)
            .allProcessingProducts().chunkAndCluster().allDusts()
            .gem(true)
            .build()),
    GRAPHITE(emptyBuilder().allCraftedProducts().allDusts()
            .build()),
    INVAR("iron", emptyBuilder().allCraftedProducts().allDusts()
            .build()),
    IRON(emptyBuilder().netherOre(true).endOre(true)
            .allCraftedProducts().ingot(false).block(false).nugget(false)
            .allProcessingProducts().chunkAndCluster().allDusts()
            .blade(true)
            .build()),
    LEAD(builder().gem(false).blade(false).tiny(false)
            .build()),
    LUMIUM(emptyBuilder().allCraftedProducts().allDusts().allProcessingProducts()
            .build()),
    NETHERITE("diamond",  emptyBuilder()
            .allCraftedProducts().ingot(false).block(false)
            .allDusts()
            .build()),
    NICKEL(builder().gem(false).blade(false).tiny(false)
            .build()),
    OBSIDIAN(emptyBuilder().allDusts()
            .wire(true).plate(true).gear(true).rod(true)
            .build()),
    OSMIUM("iron", builder().gem(false).blade(false).tiny(false)
            .build()),
    QUARTZ(emptyBuilder().ores(true).netherOre(false)
            .allCraftedProducts().ingot(false).block(false)
            .allProcessingProducts().chunkAndCluster().allDusts()
            .crystal(false)
            .build()),
    REFINED_GLOWSTONE(emptyBuilder().allCraftedProducts().allDusts()
            .build()),
    SIGNALUM(emptyBuilder().wire(true)
            .build()),
    SILVER(builder().tiny(false)
            .build()),
    STEEL(emptyBuilder().allCraftedProducts().allDusts()
            .build()),
    SULFUR(emptyBuilder().ores(true).block(true)
            .wire(true).dust(true).gem(true)
            .allProcessingProducts()
            .build()),
    TIN(builder().gem(false).tiny(false)
            .build()),
    URANIUM(builder().gem(false).blade(false).tiny(false)
            .build()),
    ZINC(builder().gem(false).blade(false).tiny(false)
            .build()),
    REDSTONE(emptyBuilder()
            .netherOre(true).endOre(true)
            .chunkAndCluster()
            .wire(true)
            .smallDust(true).tinyDust(true)
            .build()),
    FLUORITE(emptyBuilder().ores(true).block(true)
            .chunkAndCluster().allDusts()
            .gem(true).wire(true)
            .build()),
    EMERALD("iron", emptyBuilder().netherOre(true).endOre(true)
            .plate(true).wire(true)
            .dust(true).tinyDust(true)
            .chunkAndCluster()
            .build()),
    LAPIS_LAZULI(emptyBuilder()
            .netherOre(true).endOre(true)
            .dust(true).tinyDust(true).wire(true)
            .chunkAndCluster()
            .build()),
    RESONATING_ORE(emptyBuilder().ores(true).block(true).gem(true).wire(true)
            .build()),
    DIMENSIONAL_SHARD("iron", emptyBuilder().ores(true).block(true).gem(true).wire(true)
            .build()),
    ANTIMONY(emptyBuilder().oresAndRaw()
            .allCraftedProducts().plate(false).gear(false).rod(false)
            .allDusts().allProcessingProducts()
            .build()),
    BAUXITE(emptyBuilder().ores(true).block(true).wire(true).allDusts()
            .build()),
    IRIDIUM(emptyBuilder().oresAndRaw()
            .allCraftedProducts()
            .allDusts().allProcessingProducts()
            .build()),
    MONAZITE(emptyBuilder().ores(true).block(true).wire(true).allDusts()
            .build()),
    TUNGSTEN(builder().gem(false).blade(false).chunk(false).cluster(false).tiny(false)
            .build()),
    TITANIUM(builder().gem(false).chunk(false).cluster(false).tiny(false)
            .build()),
    PLATINUM(builder().gem(false).blade(false).tiny(false)
            .build()),
    STAINLESS_STEEL(emptyBuilder().allCraftedProducts().allDusts().blade(true)
            .build()),
    PLUTONIUM(emptyBuilder().rawBlock(true).rawOre(true)
            .allCraftedProducts()
            .allProcessingProducts().allDusts()
            .gem(true).blade(true)
            .build()),
    CHROMIUM(emptyBuilder().rawBlock(true).rawOre(true)
            .allCraftedProducts()
            .allProcessingProducts().allDusts()
            .gem(true).blade(true)
            .build()),
    SILICON(emptyBuilder().rawBlock(true).rawOre(true)
            .allCraftedProducts()
            .allProcessingProducts().allDusts()
            .gem(true).blade(true)
            .build()),
    BRASS(emptyBuilder().rawBlock(true).rawOre(true)
            .allCraftedProducts()
            .allProcessingProducts().allDusts()
            .gem(true).blade(true)
            .build()),
    COAL(emptyBuilder().netherOre(true).endOre(true)
            .dust(true).plate(true).tiny(true)
            .build()),
    CINNABAR(emptyBuilder().ores(true).block(true).dust(true).gem(true)
            .build()),
    NITER(emptyBuilder().ores(true).block(true).dust(true).gem(true)
            .build()),
    RUBY(emptyBuilder().ores(true).block(true).dust(true).tinyDust(true).gem(true)
            .build()),
    SALT(emptyBuilder().ores(true).block(true).dust(true).tinyDust(true).gem(true)
            .build()),
    SAPPHIRE(emptyBuilder().ores(true).block(true).dust(true).tinyDust(true).gem(true)
            .build()),
    COAL_COKE(emptyBuilder().gem(true).block(true).dust(true)
            .build()),
    REFINED_OBSIDIAN(emptyBuilder().block(true).ingot(true).nugget(true).dust(true)
            .build()),
    SAW(emptyBuilder().dust(true)
            .build()),
    LITHIUM(emptyBuilder().dust(true)
            .build()),
    TOPAZ(emptyBuilder().tiny(true)
            .build()),
    ;

    private static final Map<String,Resource> MAP = Util.make(new HashMap<>(), m -> {
        for (var val : Resource.values()) {
            m.put(val.name().toLowerCase(), val);
        }
    });

    private final EnumSet<ResourceType> resourceTypes;
    private final ResourceLocation breakableWith;

    Resource(EnumSet<ResourceType> resourceTypes) {
        this("stone", resourceTypes);
    }

    Resource(String breakableWith, EnumSet<ResourceType> resourceTypes) {
        this.resourceTypes = resourceTypes;
        this.breakableWith = ResourceLocation.withDefaultNamespace("needs_" + breakableWith + "_tool");
    }

    public static boolean isFTBResource(String resourceName) {
        return MAP.containsKey(resourceName);
    }

    public Set<ResourceType> getResourceTypes() {
        return resourceTypes;
    }

    public ResourceLocation getBreakableWith() {
        return breakableWith;
    }

    private static ResourceBuilder builder() {
        return new ResourceBuilder(EnumSet.allOf(ResourceType.class));
    }

    private static ResourceBuilder emptyBuilder() {
        return new ResourceBuilder(EnumSet.noneOf(ResourceType.class));
    }

    private static class ResourceBuilder {
        private final EnumSet<ResourceType> resourceComponents;

        private ResourceBuilder(EnumSet<ResourceType> types) {
            resourceComponents = types;
        }

        public ResourceBuilder withType(ResourceType type, boolean add) {
            if (add) {
                resourceComponents.add(type);
            } else {
                resourceComponents.remove(type);
            }
            return this;
        }

        public ResourceBuilder oresAndRaw() {
            return ores(true).rawOre(true).rawBlock(true);
        }

        public ResourceBuilder allDusts() {
            return tinyDust(true).smallDust(true).dust(true);
        }

        public ResourceBuilder allProcessingProducts() {
            return crystal(true).shard(true).clump(true).dirtyDust(true);
        }

        public ResourceBuilder chunkAndCluster() {
            return chunk(true).cluster(true);
        }

        public ResourceBuilder allCraftedProducts() {
            return ingot(true).block(true).nugget(true)
                    .wire(true).plate(true).rod(true).gear(true);
        }

        public ResourceBuilder ores(boolean add) {
            return stoneOre(add).deepslateOre(add).endOre(add).netherOre(add);
        }

        public ResourceBuilder stoneOre(boolean add) {
            return withType(ResourceType.STONE_ORE, add);
        }

        public ResourceBuilder deepslateOre(boolean add) {
            return withType(ResourceType.DEEPSLATE_ORE, add);
        }

        public ResourceBuilder endOre(boolean add) {
            return withType(ResourceType.END_ORE, add);
        }

        public ResourceBuilder netherOre(boolean add) {
            return withType(ResourceType.NETHER_ORE, add);
        }

        public ResourceBuilder block(boolean add) {
            return withType(ResourceType.BLOCK, add);
        }

        public ResourceBuilder ingot(boolean add) {
            return withType(ResourceType.INGOT, add);
        }

        public ResourceBuilder rawOre(boolean add) {
            return withType(ResourceType.RAW_ORE, add);
        }

        public ResourceBuilder rawBlock(boolean add) {
            return withType(ResourceType.RAW_BLOCK, add);
        }

        public ResourceBuilder nugget(boolean add) {
            return withType(ResourceType.NUGGET, add);
        }

        public ResourceBuilder dust(boolean add) {
            return withType(ResourceType.DUST, add);
        }

        public ResourceBuilder plate(boolean add) {
            return withType(ResourceType.PLATE, add);
        }

        public ResourceBuilder gear(boolean add) {
            return withType(ResourceType.GEAR, add);
        }

        public ResourceBuilder rod(boolean add) {
            return withType(ResourceType.ROD, add);
        }

        public ResourceBuilder gem(boolean add) {
            return withType(ResourceType.GEM, add);
        }

        public ResourceBuilder crystal(boolean add) {
            return withType(ResourceType.CRYSTAL, add);
        }

        public ResourceBuilder wire(boolean add) {
            return withType(ResourceType.WIRE, add);
        }

        public ResourceBuilder shard(boolean add) {
            return withType(ResourceType.SHARD, add);
        }

        public ResourceBuilder clump(boolean add) {
            return withType(ResourceType.CLUMP, add);
        }

        public ResourceBuilder dirtyDust(boolean add) {
            return withType(ResourceType.DIRTY_DUST, add);
        }

        public ResourceBuilder blade(boolean add) {
            return withType(ResourceType.BLADE, add);
        }

        public ResourceBuilder chunk(boolean add) {
            return withType(ResourceType.CHUNK, add);
        }

        public ResourceBuilder cluster(boolean add) {
            return withType(ResourceType.CLUSTER, add);
        }

        public ResourceBuilder smallDust(boolean add) {
            return withType(ResourceType.SMALL_DUST, add);
        }

        public ResourceBuilder tinyDust(boolean add) {
            return withType(ResourceType.TINY_DUST, add);
        }

        public ResourceBuilder tiny(boolean add) {
            return withType(ResourceType.TINY, add);
        }

        public EnumSet<ResourceType> build() {
            return resourceComponents;
        }
    }
}
