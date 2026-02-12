package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistry;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistryHolder;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LanguageGenerator extends LanguageProvider {
    public LanguageGenerator(PackOutput output) {
        super(output, FTBMaterials.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.ftbmaterials.ftbmaterials_main", "FTB Materials");

        for (ResourceRegistryHolder holder : ResourceRegistry.RESOURCE_REGISTRY_HOLDERS) {
            var reverseBlockLookup = holder.getReverseBlockLookup();
            var reverseItemLookup = holder.getReverseItemLookup();

            createTranslations(reverseBlockLookup);
            createTranslations(reverseItemLookup);
        }
    }

    private <R,T extends R> void createTranslations(Map<? extends DeferredHolder<R,T>, Pair<Resource, ResourceType>> reverseBlockLookup) {
        for (var entry : reverseBlockLookup.entrySet()) {
            var target = entry.getKey();
            var pair = entry.getValue();

            var resource = pair.key();
            var component = pair.value();

            ResourceKey<R> resourceKey = target.getKey();

            var translationText = component.getTranslationText().replace("{material}", toTitleCase(resource.name().replace("_", " ")));
            var translationKey = resourceKey.location().getNamespace() + "." + resourceKey.location().getPath();
            var keyPrefix = target.get() instanceof Item ? "item" : "block";

            add(keyPrefix + "." + translationKey, translationText);
        }
    }

    private static String toTitleCase(String input) {
        return Arrays.stream(input.split(" "))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
