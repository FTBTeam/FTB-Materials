package dev.ftb.mods.ftbmaterials.fabric.data;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LanguageGenerator extends FabricLanguageProvider {
    protected LanguageGenerator(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.ftbmaterials.ftbmaterials_main", "FTB Materials");

        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            Map<RegistrySupplier<Block>, Pair<Resource, ResourceType>> reverseBlockLookup = holder.getReverseBlockLookup();
            Map<RegistrySupplier<Item>, Pair<Resource, ResourceType>> reverseItemLookup = holder.getReverseItemLookup();

            createTranslations(translationBuilder, reverseBlockLookup);
            createTranslations(translationBuilder, reverseItemLookup);
        }
    }

    private <T> void createTranslations(TranslationBuilder translationBuilder, Map<RegistrySupplier<T>, Pair<Resource, ResourceType>> reverseBlockLookup) {
        for (Map.Entry<RegistrySupplier<T>, Pair<Resource, ResourceType>> entry : reverseBlockLookup.entrySet()) {
            var target = entry.getKey();
            var pair = entry.getValue();

            var resource = pair.key();
            var component = pair.value();

            ResourceKey<T> resourceKey = target.getKey();

            var translationText = component.getTranslationText().replace("{material}", toTitleCase(resource.name().replace("_", " ")));
            var translationKey = resourceKey.location().getNamespace() + "." + resourceKey.location().getPath();
            var keyPrefix = target.get() instanceof Item ? "item" : "block";

            translationBuilder.add(keyPrefix + "." + translationKey, translationText);
        }
    }

    private static String toTitleCase(String input) {
        return Arrays.stream(input.split(" "))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
