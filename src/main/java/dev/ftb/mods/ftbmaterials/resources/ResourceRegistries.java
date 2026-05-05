package dev.ftb.mods.ftbmaterials.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Optional;

public class ResourceRegistries {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceRegistries.class);

    private static final EnumMap<Resource, ResourceRegistryHolder> RESOURCE_REGISTRY_HOLDERS = new EnumMap<>(Resource.class);

    public static void init() {
        LOGGER.info("Initializing resource registry holders");

        for (Resource resource : Resource.values()) {
            RESOURCE_REGISTRY_HOLDERS.put(resource, new ResourceRegistryHolder(resource));
        }
    }

    public static Collection<ResourceRegistryHolder> allHolders() {
        return RESOURCE_REGISTRY_HOLDERS.values();
    }

    public static ResourceRegistryHolder get(Resource type) {
        return RESOURCE_REGISTRY_HOLDERS.get(type);
    }

    public static void clearReverseLookups() {
        RESOURCE_REGISTRY_HOLDERS.forEach((resource, holder) -> holder.clearReverseLookups());
    }
}
