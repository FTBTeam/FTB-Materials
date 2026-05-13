package dev.ftb.mods.ftbmaterials.config;

import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftblibrary.snbt.config.StringListValue;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;

import java.util.ArrayList;
import java.util.List;

public interface DisabledMaterialList {
    String KEY = FTBMaterials.MOD_ID + "-disabled-materials";

    SNBTConfig CONFIG = SNBTConfig.create(KEY);

    StringListValue DISABLED_MATERIALS = CONFIG.addStringList("disabled_materials", List.of())
            .comment(
                    "A list of materials to disable.",
                    "This list will only remove the materials recipes, not the worldgen!",
                    "Possible values only updated on first config creation. Delete this file if you want and updated list",
                    "Possible values: \n"
            )
            .comment(generateComments());

    static String[] generateComments() {
        List<String> comments = new ArrayList<>();

        for (Resource resource : Resource.values()) {
            for (ResourceType resourceType : resource.getResourceTypes()) {
                String niceName = resource.name().toLowerCase() + "_" + resourceType.name().toLowerCase();
                comments.add(niceName);
            }
        }

        return comments.toArray(new String[0]);
    }
}
