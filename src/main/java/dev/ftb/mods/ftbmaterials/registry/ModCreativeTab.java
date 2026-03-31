package dev.ftb.mods.ftbmaterials.registry;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> REGISTRY
            = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FTBMaterials.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = REGISTRY.register("tab", id -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ftbmaterials.ftbmaterials_main"))
            .icon(() -> new ItemStack(ResourceRegistries.get(Resource.LEAD).getItems().getFirst().get()))
            .displayItems((params, out) -> {
                for (ResourceRegistryHolder resourceRegistryHolder : ResourceRegistries.allHolders()) {
                    out.acceptAll(resourceRegistryHolder.getItems().stream().map(e -> new ItemStack(e.get())).toList());
                    out.acceptAll(resourceRegistryHolder.getBlockItems().stream().map(e -> new ItemStack(e.get())).toList());
                }
            })
            .build()
    );
}
