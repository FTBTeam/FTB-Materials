package dev.ftb.mods.ftbmaterials.util;

import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;

import java.util.Map;

public class VanillaResourceUtils {
    private static final Map<String,String> VANILLA_MAPPINGS = Map.of(
        "lapis_lazuli", "lapis"
    );

    public static String getVanillaResourceName(String resource) {
        return VANILLA_MAPPINGS.getOrDefault(resource, resource);
    }

    public static String vanillaPath(Resource resource, ResourceType type) {
        String resourceName = getVanillaResourceName(resource.name().toLowerCase());

        if (type == ResourceType.STONE_ORE) {
            return resourceName + "_ore";
        } else if (type == ResourceType.NETHER_ORE) {
            return "nether_" + resourceName + "_ore";
        } else if (type == ResourceType.DEEPSLATE_ORE) {
            return "deepslate_" + resourceName + "_ore";
        } else if (type == ResourceType.RAW_BLOCK) {
            return "raw_" + resourceName + "_block";
        } else if (type == ResourceType.RAW_ORE) {
            return "raw_" + resourceName;
        } else if (type == ResourceType.GEM && resource == Resource.QUARTZ) {
            return "quartz";
        }
        return resourceName + "_" + type.name().toLowerCase();
    }
}
