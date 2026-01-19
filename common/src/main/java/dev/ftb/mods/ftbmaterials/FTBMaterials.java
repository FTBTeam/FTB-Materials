package dev.ftb.mods.ftbmaterials;

import com.mojang.brigadier.CommandDispatcher;
import dev.ftb.mods.ftbmaterials.dev.commands.ConstructAllResources;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModCreativeTab;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.xplat.IPlatform;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ServiceLoader;

public final class FTBMaterials {
    public static final String MOD_ID = "ftbmaterials";

    private static final IPlatform PLATFORM = ServiceLoader.load(IPlatform.class).findFirst().orElseThrow();

    public static void init() {
        // Write common init code here.
        ResourceRegistry.init();
        ModCreativeTab.REGISTRY.init();

        ModBlocks.REGISTRY.init();
        ModItems.REGISTRY.init();
    }

    public static void onSetup() {
        if (PLATFORM.isDevEnv()) {
            return;
        }

        // This reverse lookup is only needed during data generation so we can clear it once the game is setup
        ResourceRegistry.RESOURCE_REGISTRY_HOLDERS.forEach(ResourceRegistryHolder::clearReverseLookups);
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> commandSourceStackCommandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        commandSourceStackCommandDispatcher.register(Commands.literal(MOD_ID)
                .then(Commands.literal("dev")
                        .then(ConstructAllResources.register())
                )
        );
    }

    public static IPlatform platform() {
        return PLATFORM;
    }
}
