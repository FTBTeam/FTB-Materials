package dev.ftb.mods.ftbmaterials.resources;

import com.google.common.collect.ImmutableList;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistryRef;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class ResourceRegistryHolder {
    public static final Set<ResourceType> BLOCK_TYPES = Set.of(ResourceType.BLOCK, ResourceType.RAW_BLOCK, ResourceType.STONE_ORE, ResourceType.DEEPSLATE_ORE, ResourceType.END_ORE, ResourceType.NETHER_ORE);

    private final Resource type;

    private final List<XRegistryRef<Block>> blocks = new LinkedList<>();
    private final List<XRegistryRef<Item>> items = new LinkedList<>();

    private final Map<ResourceType, XRegistryRef<Block>> componentToBlockRegister = new LinkedHashMap<>();
    private final Map<ResourceType, XRegistryRef<Item>> componentToItemRegister = new LinkedHashMap<>();

    public final Map<XRegistryRef<Block>, Pair<Resource, ResourceType>> reverseBlockLookup = new LinkedHashMap<>();
    public final Map<XRegistryRef<Item>, Pair<Resource, ResourceType>> reverseItemLookup = new LinkedHashMap<>();

    public ResourceRegistryHolder(Resource type) {
        this.type = type;
        Set<ResourceType> components = this.type.getComponents();

        this.registerEntries(components);
    }

    private void registerEntries(Set<ResourceType> components) {
        for (ResourceType component : components) {
            String niceName = this.type.name().toLowerCase() + "_" + component.name().toLowerCase();

            if (BLOCK_TYPES.contains(component)) {
                XRegistryRef<Block> regItem = ResourceRegistry.BLOCKS.register(niceName, () -> new Block(ModBlocks.DEFAULT_PROPS));
                XRegistryRef<Item> blockItem = ResourceRegistry.ITEMS.register(niceName, () -> new BlockItem(regItem.get(), ModItems.DEFAULT_PROPS));

                this.blocks.add(regItem);
                this.componentToBlockRegister.put(component, regItem);
                this.componentToItemRegister.put(component, blockItem);

                reverseBlockLookup.put(regItem, Pair.of(this.type, component));
                reverseItemLookup.put(blockItem, Pair.of(this.type, component));
            } else {
                XRegistryRef<Item> regBlock = ResourceRegistry.ITEMS.register(niceName, () -> new Item(ModItems.DEFAULT_PROPS));

                this.items.add(regBlock);
                this.componentToItemRegister.put(component, regBlock);

                reverseItemLookup.put(regBlock, Pair.of(this.type, component));
            }
        }
    }

    public Optional<XRegistryRef<Block>> getBlockFromType(ResourceType type) {
        return Optional.ofNullable(this.componentToBlockRegister.get(type));
    }

    public Optional<XRegistryRef<Item>> getItemFromType(ResourceType type) {
        return Optional.ofNullable(this.componentToItemRegister.get(type));
    }

    public ImmutableList<XRegistryRef<Block>> getBlocks() {
        return ImmutableList.copyOf(this.blocks);
    }

    public ImmutableList<XRegistryRef<Item>> getItems() {
        return ImmutableList.copyOf(this.items);
    }

    public Resource getType() {
        return type;
    }

    public Map<XRegistryRef<Block>, Pair<Resource, ResourceType>> getReverseBlockLookup() {
        return reverseBlockLookup;
    }

    public Map<XRegistryRef<Item>, Pair<Resource, ResourceType>> getReverseItemLookup() {
        return reverseItemLookup;
    }

    public void clearReverseLookups() {
        reverseBlockLookup.clear();
        reverseItemLookup.clear();
    }
}
