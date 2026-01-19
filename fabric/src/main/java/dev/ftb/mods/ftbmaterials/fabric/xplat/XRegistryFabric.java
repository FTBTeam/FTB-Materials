package dev.ftb.mods.ftbmaterials.fabric.xplat;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistryRef;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class XRegistryFabric<T> implements XRegistry<T> {
    private final Registry<T> backingRegistry;
    private final List<XRegistryRef<T>> entries = new LinkedList<>();

    @SuppressWarnings("unchecked")
    public XRegistryFabric(ResourceKey<Registry<T>> backingRegistry) {
        this.backingRegistry = (Registry<T>) BuiltInRegistries.REGISTRY.get(backingRegistry.location());
    }

    @Override
    public void init() {
        for (XRegistryRef<T> entry : entries) {
            Registry.register(backingRegistry, entry.identifier(), entry.get());
        }
    }

    @Override
    public XRegistryRef<T> register(String id, Supplier<T> value) {
        var identifier = ResourceLocation.fromNamespaceAndPath(FTBMaterials.MOD_ID, id);
        var entry = new XRegistryRefFabric<>(this.backingRegistry.key(), identifier, value);
        entries.add(entry);
        return entry;
    }
}
