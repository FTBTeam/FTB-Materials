package dev.ftb.mods.ftbmaterials.resources;

import com.google.common.collect.ImmutableList;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.*;

public class ResourceRegistryHolder {
    private final Resource resource;

    private final List<DeferredBlock<Block>> blocks = new LinkedList<>();
    private final List<DeferredItem<Item>> blockItems = new LinkedList<>();
    private final List<DeferredItem<Item>> items = new LinkedList<>();

    private final Map<ResourceType, DeferredBlock<Block>> componentToBlockRegister = new LinkedHashMap<>();
    private final Map<ResourceType, DeferredItem<Item>> componentToItemRegister = new LinkedHashMap<>();

    public final Map<DeferredBlock<Block>, Pair<Resource, ResourceType>> reverseBlockLookup = new LinkedHashMap<>();
    public final Map<DeferredItem<Item>, Pair<Resource, ResourceType>> reverseItemLookup = new LinkedHashMap<>();

    public ResourceRegistryHolder(Resource resource) {
        this.resource = resource;

        this.registerEntries(resource.getResourceTypes());
    }

    private void registerEntries(Set<ResourceType> resourceTypes) {
        for (ResourceType resourceType : resourceTypes) {
            String niceName = resource.name().toLowerCase() + "_" + resourceType.name().toLowerCase();

            if (resourceType.isBlock()) {
                DeferredBlock<Block> regBlock = ModBlocks.REGISTRY.register(niceName,
                        id -> new Block(ModBlocks.propsFor(resourceType)));
                DeferredItem<Item> regBlockItem = ModItems.REGISTRY.register(niceName,
                        id -> new BlockItem(regBlock.get(), ModItems.defaultProps()));

                blocks.add(regBlock);
                blockItems.add(regBlockItem);
                componentToBlockRegister.put(resourceType, regBlock);
                componentToItemRegister.put(resourceType, regBlockItem);

                reverseBlockLookup.put(regBlock, Pair.of(resource, resourceType));
                reverseItemLookup.put(regBlockItem, Pair.of(resource, resourceType));
            } else {
                DeferredItem<Item> regItem = ModItems.REGISTRY.register(niceName,
                        id -> new Item(ModItems.defaultProps()));

                items.add(regItem);
                componentToItemRegister.put(resourceType, regItem);

                reverseItemLookup.put(regItem, Pair.of(resource, resourceType));
            }
        }
    }

    public Optional<DeferredHolder<Block,Block>> getBlockFromType(ResourceType type) {
        return Optional.ofNullable(this.componentToBlockRegister.get(type));
    }

    public Optional<DeferredHolder<Item,Item>> getItemFromType(ResourceType type) {
        return Optional.ofNullable(this.componentToItemRegister.get(type));
    }

    public ImmutableList<DeferredBlock<Block>> getBlocks() {
        return ImmutableList.copyOf(this.blocks);
    }

    public ImmutableList<DeferredItem<Item>> getItems() {
        return ImmutableList.copyOf(this.items);
    }

    public List<DeferredItem<Item>> getBlockItems() {
        return blockItems;
    }

    public Resource getResource() {
        return resource;
    }

    public Map<DeferredBlock<Block>, Pair<Resource, ResourceType>> getReverseBlockLookup() {
        return reverseBlockLookup;
    }

    public Map<DeferredItem<Item>, Pair<Resource, ResourceType>> getReverseItemLookup() {
        return reverseItemLookup;
    }

    public void clearReverseLookups() {
        reverseBlockLookup.clear();
        reverseItemLookup.clear();
    }
}
