package dev.ftb.mods.ftbmaterials.xplat.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface XRegistryRef<T> extends Supplier<T> {
    ResourceLocation identifier();

    ResourceKey<T> resourceKey();
}
