package dev.ftb.mods.ftbmaterials.resources;

import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ResourceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceRegistry.class);

    static final XRegistry<Block> BLOCKS = XRegistry.create(Registries.BLOCK);
    static final XRegistry<Item> ITEMS = XRegistry.create(Registries.ITEM);

    public static final Set<ResourceRegistryHolder> RESOURCE_REGISTRY_HOLDERS = new LinkedHashSet<>();

    public static void init() {
        LOGGER.info("Initializing resource registry holders");

        for (Resource type : Resource.values()) {
            var holder = create(type);
            RESOURCE_REGISTRY_HOLDERS.add(holder);
        }

        BLOCKS.init();
        ITEMS.init();
    }

    private static ResourceRegistryHolder create(Resource type) {
        return new ResourceRegistryHolder(type);
    }

    public static Optional<ResourceRegistryHolder> get(Resource type) {
        for (ResourceRegistryHolder holder : RESOURCE_REGISTRY_HOLDERS) {
            if (holder.getType() == type) {
                return Optional.of(holder);
            }
        }

        return Optional.empty();
    }
}
