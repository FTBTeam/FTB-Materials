package dev.ftb.mods.ftbmaterials.unification.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

public record RecipeSchema(
    List<String> input,
    List<String> output,
    Optional<List<String>> catalyst
) {
    public static Codec<RecipeSchema> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.list(Codec.STRING).fieldOf("input").forGetter(RecipeSchema::input),
        Codec.list(Codec.STRING).fieldOf("output").forGetter(RecipeSchema::output),
        Codec.list(Codec.STRING).optionalFieldOf("catalyst").forGetter(RecipeSchema::catalyst)
    ).apply(instance, RecipeSchema::new));
}
