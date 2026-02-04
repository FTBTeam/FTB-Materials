package dev.ftb.mods.ftbmaterials.mixin;

import dev.ftb.mods.ftbmaterials.unification.recipes.RecipeMutationManager;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

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

        // Modify it!
        // TODO: WE NEED TO BE QUICK BOI!
        return RecipeMutationManager.INSTANCE.mutateRecipe(jsonElement);
    }

    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("TAIL")
    )
    private void ftbmaterials$afterApplyRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        // Clear cached unsupported types after recipe reload
        RecipeMutationManager.INSTANCE.commit();
    }
}
