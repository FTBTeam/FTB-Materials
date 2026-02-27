package dev.ftb.mods.ftbmaterials;

import dev.ftb.mods.ftblibrary.config.manager.ConfigManager;
import dev.ftb.mods.ftbmaterials.commands.BuildUnifierDB;
import dev.ftb.mods.ftbmaterials.commands.ConstructAllResources;
import dev.ftb.mods.ftbmaterials.commands.Reload;
import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModCreativeTab;
import dev.ftb.mods.ftbmaterials.registry.ModGlobalLootModifiers;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(FTBMaterials.MOD_ID)
public class FTBMaterials {
    public static final String MOD_ID = "ftbmaterials";

    public static final Logger LOGGER = LoggerFactory.getLogger(FTBMaterials.class);

    public FTBMaterials(IEventBus modBus) {
        ResourceRegistries.init();

        ModBlocks.REGISTRY.register(modBus);
        ModItems.REGISTRY.register(modBus);
        ModCreativeTab.REGISTRY.register(modBus);
        ModGlobalLootModifiers.REGISTRY.register(modBus);

        modBus.addListener(this::onSetup);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);

        UnifierManager.INSTANCE.init();

        ConfigManager.getInstance().registerStartupConfig(StartupConfig.CONFIG, "startup");
    }

    public void onSetup(FMLCommonSetupEvent event) {
        if (!FMLLoader.isProduction()) {
            return;
        }

        // This reverse lookup is only needed during data generation, so we can clear it once the game is set up
        ResourceRegistries.clearReverseLookups();
    }

    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(MOD_ID)
                .then(Commands.literal("dev")
                        .requires(cs -> cs.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(ConstructAllResources.register())
                        .then(BuildUnifierDB.register())
                        .then(Reload.register())
                )
        );
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(FTBMaterials.MOD_ID, path);
    }
}
