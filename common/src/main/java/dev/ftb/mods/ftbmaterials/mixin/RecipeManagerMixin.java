package dev.ftb.mods.ftbmaterials.mixin;

import dev.ftb.mods.ftbmaterials.unification.recipes.RecipeMutationManager;
import com.google.gson.JsonElement;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
//    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getKey()Ljava/lang/Object;"))
//    private void ftbmaterials$onApplyRecipes(Map<?, ?> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci, @Local Map.Entry<ResourceLocation, JsonElement> entry) {
//
//        // RecipeSchemas.bootstrap();
//    }

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

        // Modify it!
        // TODO: WE NEED TO BE QUICK BOI!
        return RecipeMutationManager.INSTANCE.mutateRecipe(jsonElement);
    }
}
