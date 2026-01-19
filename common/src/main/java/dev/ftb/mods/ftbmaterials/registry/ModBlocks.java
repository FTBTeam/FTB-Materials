package dev.ftb.mods.ftbmaterials.registry;

import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModBlocks {
    public static final XRegistry<Block> REGISTRY = XRegistry.create(Registries.BLOCK);

    public static final Block.Properties DEFAULT_PROPS = Block.Properties.ofFullCopy(Blocks.STONE);
}
