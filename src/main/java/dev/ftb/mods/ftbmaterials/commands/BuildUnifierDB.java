package dev.ftb.mods.ftbmaterials.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.IOException;

public class BuildUnifierDB {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("build-unifier-db")
                .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                .executes(BuildUnifierDB::buildDB);
    }

    private static int buildDB(CommandContext<CommandSourceStack> ctx) {
        try {
            UnifierManager.INSTANCE.buildDB();
            ctx.getSource().sendSuccess(() -> Component.literal("Wrote unifier DB!"), false);
            return Command.SINGLE_SUCCESS;
        } catch (IOException e) {
            ctx.getSource().sendFailure(Component.literal("Failed to write unifier DB: " + e.getMessage()));
            return 0;
        }
    }
}
