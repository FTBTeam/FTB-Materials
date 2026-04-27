package dev.ftb.mods.ftbmaterials.unification;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.unification.RecipeTweaker.Rule;
import dev.ftb.mods.ftbmaterials.unification.RecipeTweaker.Rule.RewriteAction;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.nio.file.Path;

public class DefaultCustomRules {
    public static void create(Path path) {
        RecipeTweaker tweaker = RecipeTweaker.createNew();

        tweaker.addRule(Identifier.fromNamespaceAndPath("immersiveengineering", "arcfurnace"),
                new Rule("input/tag", RewriteAction.create("item", "<tag_map>")),
                new Rule("additives/tag", RewriteAction.create("item", "<tag_map>")),
                new Rule("input/item", RewriteAction.create("item", "<item_map>")),
                new Rule("additives/item", RewriteAction.create("item", "<item_map>"))
        );
        tweaker.addRule(Identifier.fromNamespaceAndPath("immersiveengineering", "alloy"),
                new Rule("input0/tag", RewriteAction.create("item", "<tag_map>")),
                new Rule("input0/tag", RewriteAction.create("item", "<tag_map>")),
                new Rule("input1/item", RewriteAction.create("item", "<item_map>")),
                new Rule("input1/item", RewriteAction.create("item", "<item_map>"))
        );
        standardIERule(tweaker, "blast_furnace");
        standardIERule(tweaker, "bottling_machine");
        standardIERule(tweaker, "crusher");
        standardIERule(tweaker, "hammer_crushing");
        standardIERule(tweaker, "metal_press");
        standardIERule(tweaker, "squeezer");
        standardIERule(tweaker, "sawmill");

        tweaker.addRule(Identifier.fromNamespaceAndPath("enderio", "sag_milling"),
                new Rule("outputs/item/tag", new RewriteAction("id", "ftbmaterials:silicon_gem", "c:silicon")),
                new Rule("outputs/item/id", new RewriteAction("id", "ftbmaterials:silicon_gem", "enderio:silicon"))
        );

        try {
            tweaker.save(path);
        } catch (IOException e) {
            FTBMaterials.LOGGER.error("can't write default custom recipe tweaker rules to {}: {}", path, e.getMessage());
        }
    }

    private static void standardIERule(RecipeTweaker tweaker, String type) {
        // for machines with just a single input
        tweaker.addRule(Identifier.fromNamespaceAndPath("immersiveengineering", type),
                new Rule("input/tag", RewriteAction.create("item", "<tag_map>")),
                new Rule("input/item", RewriteAction.create("item", "<item_map>"))
        );
    }
}
