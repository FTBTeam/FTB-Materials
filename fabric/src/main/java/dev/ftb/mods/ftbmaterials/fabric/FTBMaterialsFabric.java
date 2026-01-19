package dev.ftb.mods.ftbmaterials.fabric;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class FTBMaterialsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FTBMaterials.init();
        FTBMaterials.onSetup();

        CommandRegistrationCallback.EVENT.register(FTBMaterials::registerCommands);
    }
}
