package dev.ftb.mods.ftbmaterials.registry;

import com.mojang.serialization.MapCodec;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.unification.LootTableUnifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModGlobalLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY
            = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FTBMaterials.MOD_ID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> LOOT_TABLE_UNIFIER
            = REGISTRY.register("unifier", id -> LootTableUnifier.CODEC.get());
}
