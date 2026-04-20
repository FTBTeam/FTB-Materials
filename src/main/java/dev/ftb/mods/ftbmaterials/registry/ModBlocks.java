package dev.ftb.mods.ftbmaterials.registry;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks REGISTRY
            = DeferredRegister.createBlocks(FTBMaterials.MOD_ID);

    // see ResourceRegistryHolder for actual block registration

    public static Block.Properties defaultProps() {
        return propsFor(null);
    }

    public static Block.Properties propsFor(ResourceType type) {
        Block.Properties props = Block.Properties.of().requiresCorrectToolForDrops();
        if (type == null) {
            return props.strength(3.0F, 3.0F).sound(SoundType.STONE);
        }
        return switch (type) {
            case DEEPSLATE_ORE -> props.strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE);
            case NETHER_ORE -> props.strength(3.0F, 3.0F).sound(SoundType.NETHER_ORE);
            case END_ORE -> props.strength(3.0F, 9.0F).sound(SoundType.STONE);
            case BLOCK -> props.strength(5.0F, 6.0F).sound(SoundType.METAL);
            case RAW_BLOCK -> props.strength(5.0F, 6.0F).sound(SoundType.STONE);
            default -> props.strength(3.0F, 3.0F).sound(SoundType.STONE);
        };
    }
}
