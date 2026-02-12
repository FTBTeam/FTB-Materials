package dev.ftb.mods.ftbmaterials.registry;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items REGISTRY
            = DeferredRegister.createItems(FTBMaterials.MOD_ID);

    public static Item.Properties defaultProps() {
        return new Item.Properties();
    }
}
