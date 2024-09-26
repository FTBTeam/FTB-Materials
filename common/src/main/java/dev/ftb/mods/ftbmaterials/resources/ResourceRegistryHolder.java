package dev.ftb.mods.ftbmaterials.resources;

import com.google.common.collect.ImmutableList;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class ResourceRegistryHolder {
    public static final Set<ResourceType> BLOCK_TYPES = Set.of(ResourceType.BLOCK, ResourceType.RAW_BLOCK, ResourceType.STONE_ORE, ResourceType.DEEPSLATE_ORE, ResourceType.END_ORE, ResourceType.NETHER_ORE);

    private final Resource type;

    private final List<RegistrySupplier<Block>> blocks = new ArrayList<>();
    private final List<RegistrySupplier<Item>> items = new ArrayList<>();

    private final Map<ResourceType, RegistrySupplier<Block>> componentToBlockRegister = new HashMap<>();
    private final Map<ResourceType, RegistrySupplier<Item>> componentToItemRegister = new HashMap<>();

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
                ResourceRegistry.ITEMS.register(niceName, () -> new BlockItem(regItem.get(), ModItems.DEFAULT_PROPS));

                this.blocks.add(regItem);
                this.componentToBlockRegister.put(component, regItem);
            } else {
                RegistrySupplier<Item> regBlock = ResourceRegistry.ITEMS.register(niceName, () -> new Item(ModItems.DEFAULT_PROPS));

                this.items.add(regBlock);
                this.componentToItemRegister.put(component, regBlock);
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
}
