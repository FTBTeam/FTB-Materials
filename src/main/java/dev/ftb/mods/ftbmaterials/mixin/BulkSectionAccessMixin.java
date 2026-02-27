package dev.ftb.mods.ftbmaterials.mixin;

import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.unification.UnifyingLevelChunkSection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BulkSectionAccess.class)
public class BulkSectionAccessMixin {
    @Shadow
    private LevelChunkSection lastSection;

    @Inject(method = "getSection", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/chunk/BulkSectionAccess;lastSectionKey:J", opcode = Opcodes.PUTFIELD))
    private void inject(BlockPos pos, CallbackInfoReturnable<LevelChunkSection> cir) {
        if (StartupConfig.TWEAK_WORLDGEN.get()) {
            lastSection = new UnifyingLevelChunkSection(lastSection);
        }
    }
}
