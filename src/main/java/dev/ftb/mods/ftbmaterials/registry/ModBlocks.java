package dev.ftb.mods.ftbmaterials.registry;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks REGISTRY
            = DeferredRegister.createBlocks(FTBMaterials.MOD_ID);

    // see ResourceRegistryHolder for actual block registration

    public static Block.Properties defaultProps() {
        return Block.Properties.ofFullCopy(Blocks.STONE);
    }
}
