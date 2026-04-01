package dev.ftb.mods.ftbmaterials.unification;

import com.google.gson.JsonElement;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public enum UnifierManager {
    INSTANCE;

    private static final Path CONFIG_DIR = FMLPaths.GAMEDIR.get().resolve("config").resolve(FTBMaterials.MOD_ID);

    public static final Path UNIFIER_DB_NAME = CONFIG_DIR.resolve("unifier-db.json");
    private static final Path RECIPE_TWEAKER_NAME = CONFIG_DIR.resolve("custom-rules.json");

    private UnifierDB unifierDB;
    private RecipeTweaker recipeTweaker;

    public void init() {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            FTBMaterials.LOGGER.error("can't create dir {} ! {}", CONFIG_DIR, e.getMessage());
        }
    }

    public void buildDB() throws IOException {
        unifierDB = UnifierDB.build();
        unifierDB.save(CONFIG_DIR.resolve(UNIFIER_DB_NAME));
    }

    public JsonElement mutateRecipeJson(JsonElement jsonElement) {
        return recipeTweaker().mutateRecipe(jsonElement, unifierDB());
    }

    public void reload() {
        unifierDB = null;
        recipeTweaker = null;
    }

    private RecipeTweaker recipeTweaker() {
        if (recipeTweaker == null) {
            try {
                if (!Files.exists(RECIPE_TWEAKER_NAME)) {
                    DefaultCustomRules.create(RECIPE_TWEAKER_NAME);
                    FTBMaterials.LOGGER.info("created new default custom rules file: {}", RECIPE_TWEAKER_NAME);
                }

                recipeTweaker = RecipeTweaker.load(RECIPE_TWEAKER_NAME);
            } catch (IOException e) {
                FTBMaterials.LOGGER.error("can't load recipe tweaker rules from {}: {}", RECIPE_TWEAKER_NAME, e.getMessage());
                recipeTweaker = RecipeTweaker.EMPTY;
            }
        }
        return recipeTweaker;
    }

    public UnifierDB unifierDB() {
        if (unifierDB == null) {
            try {
                unifierDB = UnifierDB.load(UNIFIER_DB_NAME);
            } catch (IOException e) {
                FTBMaterials.LOGGER.error("can't load unifier DB from {}: {}", UNIFIER_DB_NAME, e.getMessage());
                unifierDB = UnifierDB.EMPTY;
            }
        }
        return unifierDB;
    }
}
