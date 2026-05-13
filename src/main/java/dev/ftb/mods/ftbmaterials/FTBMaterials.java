package dev.ftb.mods.ftbmaterials;

import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import dev.ftb.mods.ftbmaterials.commands.BuildUnifierDB;
import dev.ftb.mods.ftbmaterials.commands.ConstructAllResources;
import dev.ftb.mods.ftbmaterials.commands.GenerateHiddenMaterialsTag;
import dev.ftb.mods.ftbmaterials.commands.Reload;
import dev.ftb.mods.ftbmaterials.config.DisabledMaterialList;
import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.data.ComponentsAvailableCondition;
import dev.ftb.mods.ftbmaterials.registry.ModBlocks;
import dev.ftb.mods.ftbmaterials.registry.ModCreativeTab;
import dev.ftb.mods.ftbmaterials.registry.ModGlobalLootModifiers;
import dev.ftb.mods.ftbmaterials.registry.ModItems;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import com.mojang.serialization.Codec;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@Mod(FTBMaterials.MOD_ID)
public class FTBMaterials {
    public static final String MOD_ID = "ftbmaterials";

    public static final Logger LOGGER = LoggerFactory.getLogger(FTBMaterials.class);

    public static final DeferredRegister<Codec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, MOD_ID);

    public static final Supplier<Codec<ComponentsAvailableCondition>> COMPONENTS_AVAILABLE =
            CONDITION_CODECS.register("components_available", () -> ComponentsAvailableCondition.CODEC);

    public FTBMaterials(IEventBus modBus) {
        ResourceRegistries.init();

        CONDITION_CODECS.register(modBus);
        ModBlocks.REGISTRY.register(modBus);
        ModItems.REGISTRY.register(modBus);
        ModCreativeTab.REGISTRY.register(modBus);
        ModGlobalLootModifiers.REGISTRY.register(modBus);

        modBus.addListener(this::onSetup);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);

        UnifierManager.INSTANCE.init();

        ConfigUtil.loadDefaulted(StartupConfig.CONFIG, FMLPaths.CONFIGDIR.get(), FTBMaterials.MOD_ID);
        ConfigUtil.loadDefaulted(DisabledMaterialList.CONFIG, FMLPaths.CONFIGDIR.get(), FTBMaterials.MOD_ID);
    }

    // TODO: Replace with mixin!
//    private void modifyRecipeJsonResults(ModifyRecipeJsonsEvent event) {
//        Map<Identifier, JsonElement> recipeJsons = new HashMap<>(event.getRecipeJsons());
//        for (Map.Entry<Identifier, JsonElement> entry : recipeJsons.entrySet()) {
//            event.getRecipeJsons().put(entry.getKey(), UnifierManager.INSTANCE.mutateRecipeJson(entry.getValue()));
//        }
//    }

    public void onSetup(FMLCommonSetupEvent event) {
        // This reverse lookup is only needed during data generation, so we can clear it once the game is set up
        ResourceRegistries.clearReverseLookups();
    }

    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(MOD_ID)
                .then(Commands.literal("dev")
                        .requires(e -> e.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(ConstructAllResources.register())
                        .then(BuildUnifierDB.register())
                        .then(Reload.register())
                        .then(GenerateHiddenMaterialsTag.register())
                )
        );
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(FTBMaterials.MOD_ID, path);
    }
}
