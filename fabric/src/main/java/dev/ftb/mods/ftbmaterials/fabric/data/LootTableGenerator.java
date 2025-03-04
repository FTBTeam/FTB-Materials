package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class LootTableGenerator extends FabricBlockLootTableProvider {
    protected LootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        var ores = List.of(ResourceType.STONE_ORE, ResourceType.DEEPSLATE_ORE, ResourceType.NETHER_ORE, ResourceType.END_ORE);
        var oreTargets = ores.stream().map(this::seekBlocksWithOreItem).flatMap(List::stream).toList();

        for (var pair : oreTargets) {
            add(pair.value(), createOreDrop(pair.value(), pair.key()));
        }

        addSelfDropsForResourceTypes(ResourceType.BLOCK, ResourceType.RAW_BLOCK);

        // Drops something from vanilla. This means we can't do the normal one
        // Ore drops special case
        Map<Optional<RegistrySupplier<Block>>, ItemLike> oreBlocks = new HashMap<>() {{
            put(findBlockFromTypeAndComponent(Resource.EMERALD, ResourceType.END_ORE), Items.EMERALD);
            put(findBlockFromTypeAndComponent(Resource.DIAMOND, ResourceType.END_ORE), Items.DIAMOND);
            put(findBlockFromTypeAndComponent(Resource.LAPIS_LAZULI, ResourceType.END_ORE), Items.LAPIS_LAZULI);
            put(findBlockFromTypeAndComponent(Resource.REDSTONE, ResourceType.END_ORE), Items.REDSTONE);
            put(findBlockFromTypeAndComponent(Resource.IRON, ResourceType.END_ORE), Items.RAW_IRON);
            put(findBlockFromTypeAndComponent(Resource.GOLD, ResourceType.END_ORE), Items.RAW_GOLD);
            put(findBlockFromTypeAndComponent(Resource.COPPER, ResourceType.END_ORE), Items.RAW_COPPER);
            put(findBlockFromTypeAndComponent(Resource.QUARTZ, ResourceType.END_ORE), Items.QUARTZ);
            put(findBlockFromTypeAndComponent(Resource.EMERALD, ResourceType.NETHER_ORE), Items.EMERALD);
            put(findBlockFromTypeAndComponent(Resource.DIAMOND, ResourceType.NETHER_ORE), Items.DIAMOND);
            put(findBlockFromTypeAndComponent(Resource.LAPIS_LAZULI, ResourceType.NETHER_ORE), Items.LAPIS_LAZULI);
            put(findBlockFromTypeAndComponent(Resource.REDSTONE, ResourceType.NETHER_ORE), Items.REDSTONE);
            put(findBlockFromTypeAndComponent(Resource.IRON, ResourceType.NETHER_ORE), Items.RAW_IRON);
            put(findBlockFromTypeAndComponent(Resource.GOLD, ResourceType.NETHER_ORE), Items.RAW_GOLD);
            put(findBlockFromTypeAndComponent(Resource.COPPER, ResourceType.NETHER_ORE), Items.RAW_COPPER);
        }};

        for (var entry : oreBlocks.entrySet()) {
            if (entry.getKey().isEmpty()) {
                continue;
            }


            RegistrySupplier<Block> blockSupplier = entry.getKey().get();
            ResourceLocation registryId = blockSupplier.getId();

            if (registryId.toString().contains("redstone")) {
                add(blockSupplier.get(), createRedstoneOreDrops(blockSupplier.get()));
                continue;
            }

            add(blockSupplier.get(), createOreDrop(blockSupplier.get(), entry.getValue().asItem()));
        }
    }

    private List<Pair<Item, Block>> seekBlocksWithOreItem(ResourceType type) {
        List<Pair<Item, Block>> pairs = new ArrayList<>();

        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            var blockFromType = holder.getBlockFromType(type);
            if (blockFromType.isEmpty()) {
                continue;
            }

            var block = blockFromType.get().get();
            var oreItem = holder.getItemFromType(ResourceType.RAW_ORE);
            if (oreItem.isEmpty()) {
                continue;
            }

            var item = oreItem.get().get();
            pairs.add(Pair.of(item, block));
        }

        return pairs;
    }

    private void addSelfDropsForResourceTypes(ResourceType ...types) {
        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            for (var type : types) {
                var blockFromType = holder.getBlockFromType(type);
                if (blockFromType.isEmpty()) {
                    continue;
                }

                dropSelf(blockFromType.get().get());
            }
        }
    }

    private Optional<RegistrySupplier<Block>> findBlockFromTypeAndComponent(Resource type, ResourceType component) {
        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            if (holder.getType() == type) {
                return holder.getBlockFromType(component);
            }
        }

        return Optional.empty();
    }
}
