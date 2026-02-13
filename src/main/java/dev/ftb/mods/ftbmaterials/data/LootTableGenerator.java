package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import it.unimi.dsi.fastutil.Pair;
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
    private static final List<ResourceType> ORES = List.of(
            ResourceType.STONE_ORE,
            ResourceType.DEEPSLATE_ORE,
            ResourceType.NETHER_ORE,
            ResourceType.END_ORE
    );

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
            var oreTargets = ORES.stream()
                    .map(this::seekBlocksWithOreItem)
                    .flatMap(List::stream)
                    .toList();

            for (var pair : oreTargets) {
                add(pair.value(), createOreDrop(pair.value(), pair.key()));
            }

            addSelfDropsForResourceTypes(ResourceType.BLOCK, ResourceType.RAW_BLOCK);

            // Drops something from vanilla. This means we can't do the normal one
            // Ore drops special case
            Map<Optional<DeferredHolder<Block,Block>>, ItemLike> oreBlocks = Util.make(new HashMap<>(), map -> {
                map.put(findBlockFromTypeAndComponent(Resource.EMERALD, ResourceType.END_ORE), Items.EMERALD);
                map.put(findBlockFromTypeAndComponent(Resource.DIAMOND, ResourceType.END_ORE), Items.DIAMOND);
                map.put(findBlockFromTypeAndComponent(Resource.LAPIS_LAZULI, ResourceType.END_ORE), Items.LAPIS_LAZULI);
                map.put(findBlockFromTypeAndComponent(Resource.REDSTONE, ResourceType.END_ORE), Items.REDSTONE);
                map.put(findBlockFromTypeAndComponent(Resource.IRON, ResourceType.END_ORE), Items.RAW_IRON);
                map.put(findBlockFromTypeAndComponent(Resource.GOLD, ResourceType.END_ORE), Items.RAW_GOLD);
                map.put(findBlockFromTypeAndComponent(Resource.COPPER, ResourceType.END_ORE), Items.RAW_COPPER);
                map.put(findBlockFromTypeAndComponent(Resource.QUARTZ, ResourceType.END_ORE), Items.QUARTZ);
                map.put(findBlockFromTypeAndComponent(Resource.EMERALD, ResourceType.NETHER_ORE), Items.EMERALD);
                map.put(findBlockFromTypeAndComponent(Resource.DIAMOND, ResourceType.NETHER_ORE), Items.DIAMOND);
                map.put(findBlockFromTypeAndComponent(Resource.LAPIS_LAZULI, ResourceType.NETHER_ORE), Items.LAPIS_LAZULI);
                map.put(findBlockFromTypeAndComponent(Resource.REDSTONE, ResourceType.NETHER_ORE), Items.REDSTONE);
                map.put(findBlockFromTypeAndComponent(Resource.IRON, ResourceType.NETHER_ORE), Items.RAW_IRON);
                map.put(findBlockFromTypeAndComponent(Resource.GOLD, ResourceType.NETHER_ORE), Items.RAW_GOLD);
                map.put(findBlockFromTypeAndComponent(Resource.COPPER, ResourceType.NETHER_ORE), Items.RAW_COPPER);
                map.put(findBlockFromTypeAndComponent(Resource.QUARTZ, ResourceType.STONE_ORE), Items.QUARTZ);
                map.put(findBlockFromTypeAndComponent(Resource.QUARTZ, ResourceType.DEEPSLATE_ORE), Items.QUARTZ);
            });

            for (var entry : oreBlocks.entrySet()) {
                if (entry.getKey().isEmpty()) {
                    continue;
                }

                DeferredHolder<Block,Block> blockSupplier = entry.getKey().get();
                ResourceLocation registryId = blockSupplier.getId();

                if (registryId.toString().contains("redstone")) {
                    add(blockSupplier.get(), createRedstoneOreDrops(blockSupplier.get()));
                } else if (registryId.toString().contains("copper")) {
                    add(blockSupplier.get(), createCopperOreDrops(blockSupplier.get()));
                } else if (registryId.toString().contains("lapis")) {
                    add(blockSupplier.get(), createLapisOreDrops(blockSupplier.get()));
                } else {
                    add(blockSupplier.get(), createOreDrop(blockSupplier.get(), entry.getValue().asItem()));
                }
            }

            // Bauxite is special as it does not have a raw ore item
            for (ResourceType ore : ORES) {
                ResourceRegistries.get(Resource.BAUXITE)
                        .flatMap(holder -> holder.getBlockFromType(ore))
                        .ifPresent(blockFromType -> dropSelf(blockFromType.get()));
            }
        }

        private void addSelfDropsForResourceTypes(ResourceType ...types) {
            for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                for (var type : types) {
                    var blockFromType = holder.getBlockFromType(type);
                    if (blockFromType.isEmpty()) {
                        continue;
                    }

                    dropSelf(blockFromType.get().get());
                }
            }
        }

        private List<Pair<Item, Block>> seekBlocksWithOreItem(ResourceType type) {
            List<Pair<Item, Block>> pairs = new ArrayList<>();

            for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                var blockFromType = holder.getBlockFromType(type);
                if (blockFromType.isEmpty()) {
                    continue;
                }

                var block = blockFromType.get().get();
                var oreItem = holder.getItemFromType(ResourceType.RAW_ORE).or(() -> holder.getItemFromType(ResourceType.GEM));
                if (oreItem.isEmpty()) {
                    continue;
                }

                var item = oreItem.get().get();
                pairs.add(Pair.of(item, block));
            }

            return pairs;
        }

        private Optional<DeferredHolder<Block,Block>> findBlockFromTypeAndComponent(Resource type, ResourceType component) {
            for (ResourceRegistryHolder holder : ResourceRegistries.allHolders()) {
                if (holder.getResource() == type) {
                    return holder.getBlockFromType(component);
                }
            }

            return Optional.empty();
        }
    }

}
