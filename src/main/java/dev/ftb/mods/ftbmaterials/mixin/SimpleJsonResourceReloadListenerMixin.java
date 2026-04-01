package dev.ftb.mods.ftbmaterials.mixin;

import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Debug(export = true)
@Mixin(SimpleJsonResourceReloadListener.class)
public class SimpleJsonResourceReloadListenerMixin {
    @ModifyArg(
            method = "scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
            )
    )
    private static <T> Object ftbmaterials$wrapJsonParser(Object input, @Local(argsOnly = true) Codec<T> codec) {
        if (codec != Recipe.CODEC) {
            return input;
        }

        if (!StartupConfig.TWEAK_RECIPES.get() || !(input instanceof JsonElement jsonElement)) {
            // input should always be a JSON element but let's be safe...
            return input;
        }

        return UnifierManager.INSTANCE.mutateRecipeJson(jsonElement);
    }
}
