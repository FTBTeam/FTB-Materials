package dev.ftb.mods.ftbmaterials.unification;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;

// See BulkSectionAccessMixin
public class UnifyingLevelChunkSection extends LevelChunkSection {
    public UnifyingLevelChunkSection(LevelChunkSection wrapped) {
        super(wrapped.getStates(), wrapped.getBiomes());

        // the wrapped object is a normal LevelChunkSection with the counts properly calculated
        //   so just copy the counts here to avoid redundant recalculation
        nonEmptyBlockCount = wrapped.nonEmptyBlockCount;
        tickingBlockCount = wrapped.tickingBlockCount;
        tickingFluidCount = wrapped.tickingFluidCount;
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState state, boolean useLocks) {
        return super.setBlockState(x, y, z, UnifierManager.INSTANCE.unifierDB().lookupBlock(state), useLocks);
    }

    @Override
    public void recalcBlockCounts() {
        // do nothing here: our ctor copies the block counts from the wrapped object
    }
}
