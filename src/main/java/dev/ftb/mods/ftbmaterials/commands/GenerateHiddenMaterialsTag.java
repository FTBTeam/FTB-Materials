package dev.ftb.mods.ftbmaterials.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.config.DisabledMaterialList;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.util.List;

public class GenerateHiddenMaterialsTag {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("generate-hidden-materials-tag")
                .requires(e -> e.hasPermission(Commands.LEVEL_OWNERS))
                .executes(GenerateHiddenMaterialsTag::generate);
    }

    private static int generate(CommandContext<CommandSourceStack> context) {
        var output = FMLPaths.GAMEDIR.get().resolve("generated/ftbmaterials/tags/c/hidden_from_recipe_viewers.json");
        try {
            Files.createDirectories(output.getParent());
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to create directories for output file: " + e.getMessage()));
            return 0;
        }

        List<String> disabledMaterials = DisabledMaterialList.DISABLED_MATERIALS.get();
        if (disabledMaterials.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No disabled materials, skipping tag generation"), false);
            return 0;
        }

        JsonArray values = new JsonArray();
        for (var resource : disabledMaterials) {
            values.add(FTBMaterials.MOD_ID + ":" + resource);
        }

        JsonObject tagJson = new JsonObject();
        tagJson.addProperty("replace", false);
        tagJson.add("values", values);

        try {
            Files.writeString(output, new Gson().newBuilder().setPrettyPrinting().create().toJson(tagJson));
            context.getSource().sendSuccess(() -> Component.literal("Generated hidden materials tag with " + disabledMaterials.size() + " entries!"), false);
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to write hidden materials tag: " + e.getMessage()));
        }

        return 0;
    }

}
