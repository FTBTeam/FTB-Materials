package dev.ftb.mods.ftbmaterials.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import dev.ftb.mods.ftbmaterials.unification.UnifierManager;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @SuppressWarnings("unchecked")
    @ModifyArg(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/SimpleJsonResourceReloadListener;scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V"
            ),
            index = 3
    )
    private Codec<?> ftbmaterials$wrapRecipeCodec(Codec<?> originalCodec) {
        if (!StartupConfig.TWEAK_RECIPES.get()) {
            return originalCodec;
        }

        Codec<Recipe<?>> original = (Codec<Recipe<?>>) originalCodec;
        return new Codec<Recipe<?>>() {
            @Override
            public <T> DataResult<com.mojang.datafixers.util.Pair<Recipe<?>, T>> decode(DynamicOps<T> ops, T input) {
                if (input instanceof JsonElement jsonElement) {
                    T mutated = (T) UnifierManager.INSTANCE.mutateRecipeJson(jsonElement);
                    return original.decode(ops, mutated);
                }
                return original.decode(ops, input);
            }

            @Override
            public <T> DataResult<T> encode(Recipe<?> recipe, DynamicOps<T> ops, T prefix) {
                return original.encode(recipe, ops, prefix);
            }
        };
    }
}
