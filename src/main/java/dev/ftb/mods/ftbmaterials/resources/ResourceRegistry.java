package dev.ftb.mods.ftbmaterials.resources;

import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModCreativeTab;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ResourceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceRegistry.class);

    public static final Set<ResourceRegistryHolder> RESOURCE_REGISTRY_HOLDERS = new LinkedHashSet<>();

    public static void init(IEventBus bus) {
        LOGGER.info("Initializing resource registry holders");

        for (Resource type : Resource.values()) {
            var holder = create(type);
            RESOURCE_REGISTRY_HOLDERS.add(holder);
        }

        ModBlocks.REGISTRY.register(bus);
        ModItems.REGISTRY.register(bus);
        ModCreativeTab.REGISTRY.register(bus);
    }

    private static ResourceRegistryHolder create(Resource type) {
        return new ResourceRegistryHolder(type);
    }

    public static Optional<ResourceRegistryHolder> get(Resource type) {
        for (ResourceRegistryHolder holder : RESOURCE_REGISTRY_HOLDERS) {
            if (holder.getResource() == type) {
                return Optional.of(holder);
            }
        }

        return Optional.empty();
    }
}
