package dev.ftb.mods.ftbmaterials.unification;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.unification.RecipeTweaker.Rule;
import dev.ftb.mods.ftbmaterials.unification.RecipeTweaker.Rule.RewriteAction;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;

public class DefaultCustomRules {
    public static void create(Path path) {
        RecipeTweaker tweaker = RecipeTweaker.createNew();

        tweaker.addRule(ResourceLocation.fromNamespaceAndPath("immersiveengineering", "arcfurnace"),
                new Rule("input/tag", new RewriteAction("item", "<tag_map>")),
                new Rule("additives/tag", new RewriteAction("item", "<tag_map>")),
                new Rule("input/item", new RewriteAction("item", "<item_map>")),
                new Rule("additives/item", new RewriteAction("item", "<item_map>"))
        );
        tweaker.addRule(ResourceLocation.fromNamespaceAndPath("immersiveengineering", "alloy"),
                new Rule("input0/tag", new RewriteAction("item", "<tag_map>")),
                new Rule("input0/tag", new RewriteAction("item", "<tag_map>")),
                new Rule("input1/item", new RewriteAction("item", "<item_map>")),
                new Rule("input1/item", new RewriteAction("item", "<item_map>"))
        );
        standardIERule(tweaker, "blast_furnace");
        standardIERule(tweaker, "bottling_machine");
        standardIERule(tweaker, "crusher");
        standardIERule(tweaker, "hammer_crushing");
        standardIERule(tweaker, "metal_press");
        standardIERule(tweaker, "squeezer");
        standardIERule(tweaker, "sawmill");

        try {
            tweaker.save(path);
        } catch (IOException e) {
            FTBMaterials.LOGGER.error("can't write default custom recipe tweaker rules to {}: {}", path, e.getMessage());
        }
    }

    private static void standardIERule(RecipeTweaker tweaker, String type) {
        // for machines with just a single input
        tweaker.addRule(ResourceLocation.fromNamespaceAndPath("immersiveengineering", type),
                new Rule("input/tag", new RewriteAction("item", "<tag_map>")),
                new Rule("input/item", new RewriteAction("item", "<item_map>"))
        );
    }
}
