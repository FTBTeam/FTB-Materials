package dev.ftb.mods.ftbmaterials.config;

import dev.ftb.mods.ftblibrary.util.Lazy;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class Blacklists {
    private static final Lazy<WildcardedRLMatcher> M_UNIFICATION_BLACKLIST_ITEMS
            = Lazy.of(() -> new WildcardedRLMatcher(StartupConfig.UNIFICATION_BLACKLIST_ITEMS.get()));
    private static final Lazy<WildcardedRLMatcher> M_UNIFICATION_BLACKLIST_TAGS
            = Lazy.of(() -> new WildcardedRLMatcher(StartupConfig.UNIFICATION_BLACKLIST_TAGS.get()));
    private static final Lazy<WildcardedRLMatcher> M_UNIFICATION_BLACKLIST_BLOCKS
            = Lazy.of(() -> new WildcardedRLMatcher(StartupConfig.UNIFICATION_BLACKLIST_BLOCKS.get()));
    private static final Lazy<WildcardedRLMatcher> M_RECIPE_TYPE_BLACKLIST
            = Lazy.of(() -> new WildcardedRLMatcher(StartupConfig.RECIPE_TYPE_BLACKLIST.get()));
    private static final Lazy<WildcardedRLMatcher> M_RECIPE_ID_BLACKLIST
            = Lazy.of(() -> new WildcardedRLMatcher(StartupConfig.RECIPE_ID_BLACKLIST.get()));

    public static boolean isItemUnificationAllowed(Item item) {
        return !M_UNIFICATION_BLACKLIST_ITEMS.get().test(BuiltInRegistries.ITEM.getKey(item));
    }

    public static boolean isItemUnificationAllowed(TagKey<Item> tag) {
        return !M_UNIFICATION_BLACKLIST_TAGS.get().test(tag.location());
    }

    public static boolean isBlockUnificationAllowed(Identifier blockId) {
        return !M_UNIFICATION_BLACKLIST_BLOCKS.get().test(blockId);
    }

    public static boolean canTweakRecipeType(Identifier recipeTypeId) {
        return !M_RECIPE_TYPE_BLACKLIST.get().test(recipeTypeId);
    }

    public static boolean canTweakRecipeId(Identifier recipeId) {
        return !M_RECIPE_ID_BLACKLIST.get().test(recipeId);
    }

    private static class WildcardedRLMatcher implements Predicate<Identifier> {
        private final Set<String> namespaces = new ObjectOpenHashSet<>();
        private final Set<Identifier> reslocs = new ObjectOpenHashSet<>();

        public WildcardedRLMatcher(Collection<String> toMatch) {
            for (String s : toMatch) {
                if (s.endsWith(":*")) {
                    namespaces.add(s.split(":")[0]);
                } else if ((Identifier.tryParse(s)) instanceof Identifier location) {
                    reslocs.add(location);
                }
            }
        }

        @Override
        public boolean test(Identifier loc) {
            return reslocs.contains(loc) || namespaces.contains(loc.getNamespace());
        }
    }
}
