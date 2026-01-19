package dev.ftb.mods.ftbmaterials.neoforge.xplat;

import dev.ftb.mods.ftbmaterials.xplat.IPlatform;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.loading.FMLLoader;

public class PlatformNeoForge implements IPlatform {
    @Override
    public <T> XRegistry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return new XRegistryNeoForge<>(registryKey);
    }

    @Override
    public CreativeModeTab.Builder creativeTabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }
}
