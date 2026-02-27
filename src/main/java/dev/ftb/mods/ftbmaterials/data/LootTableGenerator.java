package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class LootTableGenerator extends LootTableProvider {
    protected LootTableGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
        ), registryLookup);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
    }

    private static class BlockLoot extends BlockLootSubProvider {
        private final Map<Block, LootTable.Builder> toAdd = new HashMap<>();

        private BlockLoot(HolderLookup.Provider provider) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, provider);
        }

        @Override
        protected void add(Block block, LootTable.Builder builder) {
            super.add(block, builder);

            toAdd.put(block, builder);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return toAdd.keySet();
        }

        @Override
        protected void generate() {
            // Ores -> raw ore or gem
            ResourceType.ORE_TYPES.stream()
                    .map(this::findBlocksWithOreItem)
                    .flatMap(List::stream)
                    .forEach(drop -> add(drop.block, createOreDrop(drop.block, drop.item)));

            // Self drops for blocks & raw ore blocks
            addSelfDrops(ResourceType.BLOCK, ResourceType.RAW_BLOCK);

            // Special handling for vanilla ores (no ftbmaterials ores for these)
            addVanillaOreDrops();

            // Special handling for bauxite ore (no raw ore item)
            for (ResourceType ore : ResourceType.ORE_TYPES) {
                ResourceRegistries.get(Resource.BAUXITE).getBlockFromType(ore)
                        .ifPresent(blockFromType -> dropSelf(blockFromType.get()));
            }
        }

        private void addVanillaOreDrops() {
            Map<DeferredHolder<Block,Block>, ItemLike> oreBlocks = Util.make(new HashMap<>(), map -> {
                List.of(ResourceType.END_ORE, ResourceType.NETHER_ORE).forEach(type -> {
                    addOreIfPresent(map, Resource.EMERALD, type, Items.EMERALD);
                    addOreIfPresent(map, Resource.DIAMOND, type, Items.DIAMOND);
                    addOreIfPresent(map, Resource.LAPIS_LAZULI, type, Items.LAPIS_LAZULI);
                    addOreIfPresent(map, Resource.REDSTONE, type, Items.REDSTONE);
                    addOreIfPresent(map, Resource.IRON, type, Items.RAW_IRON);
                    addOreIfPresent(map, Resource.GOLD, type, Items.RAW_GOLD);
                    addOreIfPresent(map, Resource.COPPER, type, Items.RAW_COPPER);
                });
                List.of(ResourceType.END_ORE, ResourceType.STONE_ORE, ResourceType.DEEPSLATE_ORE)
                        .forEach(type -> addOreIfPresent(map, Resource.QUARTZ, type, Items.QUARTZ));
            });

            for (var entry : oreBlocks.entrySet()) {
                Block block = entry.getKey().get();
                ResourceLocation registryId = entry.getKey().getId();

                if (registryId.toString().contains("redstone")) {
                    add(block, createRedstoneOreDrops(block));
                } else if (registryId.toString().contains("copper")) {
                    add(block, createCopperOreDrops(block));
                } else if (registryId.toString().contains("lapis")) {
                    add(block, createLapisOreDrops(block));
                } else {
                    add(block, createOreDrop(block, entry.getValue().asItem()));
                }
            }
        }

        private void addOreIfPresent(Map<DeferredHolder<Block, Block>, ItemLike> map, Resource resource, ResourceType type, Item item) {
            ResourceRegistries.get(resource).getBlockFromType(type).ifPresent(b -> map.put(b, item));
        }

        private void addSelfDrops(ResourceType... resourceTypes) {
            for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                for (var type : resourceTypes) {
                    holder.getBlockFromType(type).ifPresent(block -> dropSelf(block.get()));
                }
            }
        }

        private List<BlockAndItem> findBlocksWithOreItem(ResourceType resourceType) {
            return Util.make(new ArrayList<>(), result -> {
                for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                    holder.getBlockFromType(resourceType).ifPresent(deferredBlock ->
                            holder.getItemFromType(ResourceType.RAW_ORE).or(() -> holder.getItemFromType(ResourceType.GEM))
                                    .ifPresent(oreItem -> result.add(new BlockAndItem(deferredBlock.get(), oreItem.get()))));
                }
            });
        }

        private record BlockAndItem(Block block, Item item) {}
    }

}
