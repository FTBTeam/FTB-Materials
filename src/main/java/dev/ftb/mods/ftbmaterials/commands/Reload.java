package dev.ftb.mods.ftbmaterials.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class Reload {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("reload")
                .requires(e -> e.hasPermission(Commands.LEVEL_OWNERS))
                .executes(Reload::buildDB);
    }

    private static int buildDB(CommandContext<CommandSourceStack> ctx) {
        UnifierManager.INSTANCE.reload();
        ctx.getSource().sendSuccess(() -> Component.literal("Marked unifier DB and custom recipe rules for reload"), false);
        ctx.getSource().sendSuccess(() -> Component.literal("Reload will occur on next server restart or /reload"), false);
        return Command.SINGLE_SUCCESS;
    }
}
