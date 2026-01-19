package dev.ftb.mods.ftbmaterials.neoforge.xplat;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistryRef;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class XRegistryNeoForge<T> implements XRegistry<T> {
    DeferredRegister<T> backingRegistry;

    public XRegistryNeoForge(ResourceKey<Registry<T>> registryKey) {
        this.backingRegistry = DeferredRegister.create(registryKey, FTBMaterials.MOD_ID);
    }

    @Override
    public void init() {
        var modBus = ModList.get().getModContainerById(FTBMaterials.MOD_ID)
                .map(ModContainer::getEventBus);

        backingRegistry.register(modBus.orElseThrow());
    }

    @Override
    public XRegistryRef<T> register(String id, Supplier<T> value) {
        var identifier = ResourceLocation.fromNamespaceAndPath(FTBMaterials.MOD_ID, id);
        var entry = backingRegistry.register(id, value);

        return new XRegistryRef<T>() {
            @Override
            public ResourceLocation identifier() {
                return identifier;
            }

            @Override
            public ResourceKey<T> resourceKey() {
                return entry.getKey();
            }

            @Override
            public T get() {
                return entry.get();
            }
        };
    }
}
