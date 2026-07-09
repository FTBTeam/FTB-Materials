package dev.ftb.mods.ftbmaterials.config;

import dev.ftb.mods.ftblibrary.util.Lazy;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

    public static boolean isItemUnificationAllowed(Item item) {
        return !M_UNIFICATION_BLACKLIST_ITEMS.get().test(BuiltInRegistries.ITEM.getKey(item));
    }

    public static boolean isItemUnificationAllowed(TagKey<Item> tag) {
        return !M_UNIFICATION_BLACKLIST_TAGS.get().test(tag.location());
    }

    public static boolean isBlockUnificationAllowed(ResourceLocation blockId) {
        return !M_UNIFICATION_BLACKLIST_BLOCKS.get().test(blockId);
    }

    private static class WildcardedRLMatcher implements Predicate<ResourceLocation> {
        private final Set<String> namespaces = new ObjectOpenHashSet<>();
        private final Set<ResourceLocation> reslocs = new ObjectOpenHashSet<>();

        public WildcardedRLMatcher(Collection<String> toMatch) {
            for (String s : toMatch) {
                if (s.endsWith(":*")) {
                    namespaces.add(s.split(":")[0]);
                } else if ((ResourceLocation.tryParse(s)) instanceof ResourceLocation location) {
                    reslocs.add(location);
                }
            }
        }

        @Override
        public boolean test(ResourceLocation loc) {
            return reslocs.contains(loc) || namespaces.contains(loc.getNamespace());
        }
    }
}
