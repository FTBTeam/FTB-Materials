package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootTableGenerator extends FabricBlockLootTableProvider {
    protected LootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        var ores = List.of(ResourceType.STONE_ORE, ResourceType.DEEPSLATE_ORE, ResourceType.NETHER_ORE, ResourceType.END_ORE);
        var oreTargets = ores.stream().map(this::seekBlocksWithOreItem).flatMap(List::stream).toList();

        for (var pair : oreTargets) {
            add(pair.value(), createOreDrop(pair.value(), pair.key()));
        }

        addSelfDropsForResourceTypes(ResourceType.BLOCK, ResourceType.RAW_BLOCK);
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
}
