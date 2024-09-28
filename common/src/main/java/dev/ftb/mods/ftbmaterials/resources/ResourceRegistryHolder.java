package dev.ftb.mods.ftbmaterials.resources;

import com.google.common.collect.ImmutableList;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class ResourceRegistryHolder {
    public static final Set<ResourceType> BLOCK_TYPES = Set.of(ResourceType.BLOCK, ResourceType.RAW_BLOCK, ResourceType.STONE_ORE, ResourceType.DEEPSLATE_ORE, ResourceType.END_ORE, ResourceType.NETHER_ORE);

    private final Resource type;

    private final List<RegistrySupplier<Block>> blocks = new LinkedList<>();
    private final List<RegistrySupplier<Item>> items = new LinkedList<>();

    private final Map<ResourceType, RegistrySupplier<Block>> componentToBlockRegister = new LinkedHashMap<>();
    private final Map<ResourceType, RegistrySupplier<Item>> componentToItemRegister = new LinkedHashMap<>();

    public final Map<RegistrySupplier<Block>, Pair<Resource, ResourceType>> reverseBlockLookup = new LinkedHashMap<>();
    public final Map<RegistrySupplier<Item>, Pair<Resource, ResourceType>> reverseItemLookup = new LinkedHashMap<>();

    public ResourceRegistryHolder(Resource type) {
        this.type = type;
        Set<ResourceType> components = this.type.getComponents();

        this.registerEntries(components);
    }

    private void registerEntries(Set<ResourceType> components) {
        for (ResourceType component : components) {
            String niceName = this.type.name().toLowerCase() + "_" + component.name().toLowerCase();

            if (BLOCK_TYPES.contains(component)) {
                RegistrySupplier<Block> regItem = ResourceRegistry.BLOCKS.register(niceName, () -> new Block(ModBlocks.DEFAULT_PROPS));
                RegistrySupplier<Item> blockItem = ResourceRegistry.ITEMS.register(niceName, () -> new BlockItem(regItem.get(), ModItems.DEFAULT_PROPS));

                this.blocks.add(regItem);
                this.componentToBlockRegister.put(component, regItem);
                this.componentToItemRegister.put(component, blockItem);

                reverseBlockLookup.put(regItem, Pair.of(this.type, component));
                reverseItemLookup.put(blockItem, Pair.of(this.type, component));
            } else {
                RegistrySupplier<Item> regBlock = ResourceRegistry.ITEMS.register(niceName, () -> new Item(ModItems.DEFAULT_PROPS));

                this.items.add(regBlock);
                this.componentToItemRegister.put(component, regBlock);

                reverseItemLookup.put(regBlock, Pair.of(this.type, component));
            }
        }
    }

    public Optional<RegistrySupplier<Block>> getBlockFromType(ResourceType type) {
        if (!BLOCK_TYPES.contains(type)) {
            throw new IllegalArgumentException("Type is not a block type");
        }

        return Optional.ofNullable(this.componentToBlockRegister.get(type));
    }

    public Optional<RegistrySupplier<Item>> getItemFromType(ResourceType type) {
        return Optional.ofNullable(this.componentToItemRegister.get(type));
    }

    public ImmutableList<RegistrySupplier<Block>> getBlocks() {
        return ImmutableList.copyOf(this.blocks);
    }

    public ImmutableList<RegistrySupplier<Item>> getItems() {
        return ImmutableList.copyOf(this.items);
    }

    public Resource getType() {
        return type;
    }

    public Map<RegistrySupplier<Block>, Pair<Resource, ResourceType>> getReverseBlockLookup() {
        return reverseBlockLookup;
    }

    public Map<RegistrySupplier<Item>, Pair<Resource, ResourceType>> getReverseItemLookup() {
        return reverseItemLookup;
    }

    public void clearReverseLookups() {
        reverseBlockLookup.clear();
        reverseItemLookup.clear();
    }
}
