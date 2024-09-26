package dev.ftb.mods.ftbmaterials.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(FTBMaterials.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final DeferredSupplier<CreativeModeTab> TAB = REGISTRY.register("tab", () -> CreativeTabRegistry.create(
            Component.translatable("itemGroup.ftbmaterials.ftbmaterials_main"),
            () -> new ItemStack(ResourceRegistry.get(Resource.LEAD).orElseThrow().getItems().get(0).get())
        )
    );
}
