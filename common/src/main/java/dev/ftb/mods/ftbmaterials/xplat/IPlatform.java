package dev.ftb.mods.ftbmaterials.xplat;

import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public interface IPlatform {
    <T> XRegistry<T> createRegistry(ResourceKey<Registry<T>> registryKey);

    CreativeModeTab.Builder creativeTabBuilder();

    boolean isDevEnv();
}
