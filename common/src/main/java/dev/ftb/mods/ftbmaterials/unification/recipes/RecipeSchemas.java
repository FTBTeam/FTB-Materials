package dev.ftb.mods.ftbmaterials.unification.recipes;

import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RecipeSchemas {
    static final Map<String, RecipeSchema> SCHEMAS = new HashMap<>();

    static {
        bootstrap();
    }

    public static void bootstrap() {
//        register(RecipeType.CRAFTING, new CraftingRecipeSchema());
        // TODO: Load from source files!
        register("minecraft:smelting", new RecipeSchema(
            List.of("ingredient"),
            List.of("result"),
            Optional.empty()
        ));
    }

    private static void register(String type, RecipeSchema schema) {
        SCHEMAS.put(type, schema);
    }
}
