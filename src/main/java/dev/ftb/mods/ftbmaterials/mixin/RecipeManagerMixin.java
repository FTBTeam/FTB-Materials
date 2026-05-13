package dev.ftb.mods.ftbmaterials.mixin;

import com.google.gson.JsonElement;
import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import com.google.gson.JsonObject;
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
                    target = "Lnet/minecraft/world/item/crafting/RecipeManager;fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lcom/mojang/serialization/DynamicOps;)Ljava/util/Optional;"
            ),
            index = 1
    )
    private JsonObject ftbmaterials$modifyRecipeDecodeArg(JsonObject input) {
        if (!StartupConfig.TWEAK_RECIPES.get()) {
            // input should always be a JSON element but let's be safe...
            return input;
        }

        return UnifierManager.INSTANCE.mutateRecipeJson(input);
    }
}
