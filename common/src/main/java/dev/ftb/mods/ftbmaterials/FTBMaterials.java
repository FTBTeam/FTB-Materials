package dev.ftb.mods.ftbmaterials;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.ftb.mods.ftbmaterials.config.GeneralConfig;
import dev.ftb.mods.ftbmaterials.config.GenerationConfig;
import dev.ftb.mods.ftbmaterials.config.IntegrationsConfig;
import dev.ftb.mods.ftbmaterials.dev.commands.ConstructAllResources;
import dev.ftb.mods.ftbmaterials.integration.ModIntegrations;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModCreativeTab;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class FTBMaterials {
    public static final String MOD_ID = "ftbmaterials";

    public static void init() {
        GeneralConfig.init();
        GenerationConfig.init();
        IntegrationsConfig.init();

        // Write common init code here.
        ResourceRegistry.init();
        ModCreativeTab.REGISTRY.register();

        ModBlocks.REGISTRY.register();
        ModItems.REGISTRY.register();

        CommandRegistrationEvent.EVENT.register(FTBMaterials::registerCommands);
        ModIntegrations.INSTANCE.init();
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> commandSourceStackCommandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        commandSourceStackCommandDispatcher.register(Commands.literal(MOD_ID)
                .then(Commands.literal("dev")
                        .then(ConstructAllResources.register())
                )
        );
    }
}
