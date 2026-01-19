package dev.ftb.mods.ftbmaterials.registry;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistryRef;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTab {
    public static final XRegistry<CreativeModeTab> REGISTRY = XRegistry.create(Registries.CREATIVE_MODE_TAB);

    public static final XRegistryRef<CreativeModeTab> TAB = REGISTRY.register("tab", () -> FTBMaterials.platform().creativeTabBuilder()
            .title(Component.translatable("itemGroup.ftbmaterials.ftbmaterials_main"))
            .icon(() -> new ItemStack(ResourceRegistry.get(Resource.LEAD).orElseThrow().getItems().getFirst().get()))
            .build()
    );
}
