package dev.ftb.mods.ftbmaterials.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FTBMaterials.MOD_ID)
public final class FTBMaterialsForge {

    public FTBMaterialsForge() {
        EventBuses.registerModEventBus(FTBMaterials.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FTBMaterials.init();
    }
}
