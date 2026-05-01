package dev.ftb.mods.ftbmaterials.mixin;

import com.google.gson.JsonElement;
import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @ModifyArg(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
            ),
            index = 1
    )
    private Object ftbmaterials$modifyRecipeDecodeArg(Object input) {
        if (!StartupConfig.TWEAK_RECIPES.get() || !(input instanceof JsonElement jsonElement)) {
            // input should always be a JSON element but let's be safe...
            return input;
        }

        return UnifierManager.INSTANCE.mutateRecipeJson(jsonElement);
    }
}
