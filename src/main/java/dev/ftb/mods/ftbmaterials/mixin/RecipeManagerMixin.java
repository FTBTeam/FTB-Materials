package dev.ftb.mods.ftbmaterials.mixin;

import com.google.gson.JsonElement;
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
        // This shouldn't ever happen but we also get free type safety this way
        if (!(input instanceof JsonElement jsonElement)) {
            return input;
        }

        return UnifierManager.INSTANCE.mutateRecipeJson(jsonElement);
    }

//    @Inject(
//            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
//            at = @At("TAIL")
//    )
//    private void ftbmaterials$afterApplyRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
//        // Clear cached unsupported types after recipe reload
//        RecipeMutationManager.INSTANCE.commit();
//    }
}
