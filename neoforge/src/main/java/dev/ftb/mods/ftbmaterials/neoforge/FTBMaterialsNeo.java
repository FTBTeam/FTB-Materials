package dev.ftb.mods.ftbmaterials.neoforge;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(FTBMaterials.MOD_ID)
public final class FTBMaterialsNeo {
    public FTBMaterialsNeo(IEventBus eventBus) {
        FTBMaterials.init();
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        eventBus.addListener(this::onSetup);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        FTBMaterials.registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    private void onSetup(FMLCommonSetupEvent event) {
        FTBMaterials.onSetup();
    }
}
