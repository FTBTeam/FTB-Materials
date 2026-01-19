package dev.ftb.mods.ftbmaterials.fabric.xplat;

import dev.ftb.mods.ftbmaterials.xplat.IPlatform;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class PlatformFabric implements IPlatform {
    @Override
    public <T> XRegistry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return new XRegistryFabric<>(registryKey);
    }

    @Override
    public CreativeModeTab.Builder creativeTabBuilder() {
        return new FabricItemGroupBuilderImpl();
    }

    @Override
    public boolean isDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
