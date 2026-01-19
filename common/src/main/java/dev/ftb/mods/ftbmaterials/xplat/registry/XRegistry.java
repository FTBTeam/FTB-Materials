package dev.ftb.mods.ftbmaterials.xplat.registry;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface XRegistry<T> {
    static <T> XRegistry<T> create(ResourceKey<Registry<T>> registryKey) {
        return FTBMaterials.platform().createRegistry(registryKey);
    }

    void init();

    XRegistryRef<T> register(String id, Supplier<T> value);
}
