package dev.ftb.mods.ftbmaterials.fabric;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.fabricmc.api.ModInitializer;

public final class FTBMaterialsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FTBMaterials.init();
    }
}
