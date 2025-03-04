package dev.ftb.mods.ftbmaterials.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(FTBMaterials.MOD_ID, Registries.BLOCK);

    public static final Block.Properties DEFAULT_PROPS = Block.Properties.copy(Blocks.STONE);
}
