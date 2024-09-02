package dev.ftb.mods.ftbmaterials.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(FTBMaterials.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final DeferredSupplier<CreativeModeTab> TAB = REGISTRY.register("ftbmaterials", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.ftbmaterials.ftbmaterials_main"))
            .icon(() -> new ItemStack(Blocks.DIAMOND_BLOCK))
            .build()
    );
}
