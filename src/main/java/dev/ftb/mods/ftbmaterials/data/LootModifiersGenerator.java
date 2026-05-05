package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.unification.LootTableUnifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class LootModifiersGenerator extends GlobalLootModifierProvider {
    public LootModifiersGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, FTBMaterials.MOD_ID);
    }

    @Override
    protected void start() {
        add("unification", new LootTableUnifier(new LootItemCondition[] { }));
    }
}
