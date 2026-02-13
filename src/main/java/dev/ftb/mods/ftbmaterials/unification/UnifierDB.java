package dev.ftb.mods.ftbmaterials.unification;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.util.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class UnifierDB {
    public static final Codec<UnifierDB> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("items").forGetter(db -> db.itemMap),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("tags").forGetter(db -> db.tagMap)
    ).apply(builder, UnifierDB::new));

    public static final UnifierDB EMPTY = new UnifierDB(Map.of(), Map.of());

    private final Map<String,String> itemMap;
    private final Map<String,String> tagMap;
    private final Lazy<Map<Item,Item>> itemByItemMap = Lazy.of(this::buildItemByItemMap);

    public UnifierDB() {
        this(new HashMap<>(), new HashMap<>());
    }

    public UnifierDB(Map<String, String> itemMap, Map<String, String> tagMap) {
        this.itemMap = itemMap;
        this.tagMap = tagMap;
    }

    public static UnifierDB load(Path path) throws IOException {
        JsonElement json = JsonParser.parseString(Files.readString(path));
        return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }

    public Optional<String> lookupItem(String key) {
        return Optional.ofNullable(itemMap.get(key));
    }

    public Optional<Item> lookupItem(Item item) {
        return Optional.ofNullable(itemByItemMap.get().get(item));
    }

    public Optional<String> lookupTag(String key) {
        return Optional.ofNullable(tagMap.get(key));
    }

    public static UnifierDB build() {
        CachedTagKeyLookup<Item> cache = new CachedTagKeyLookup<>(Registries.ITEM);
        UnifierDB db = new UnifierDB();

        for (Resource resource : Resource.values()) {
            for (ResourceType type : ResourceType.values()) {
                var tags = collectTags(resource, type, cache);
                for (TagKey<Item> tag : tags) {
                    Item ftbMaterialsItem = null;
                    Item vanillaFallbackItem = null;
                    Set<Item> otherItems = new HashSet<>();
                    for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                        if (holder.unwrapKey().isPresent()) {
                            var k = holder.unwrapKey().get();
                            if (k.location().getNamespace().equals(FTBMaterials.MOD_ID)) {
                                ftbMaterialsItem = holder.value();
                            } else if (k.location().getNamespace().equals("minecraft")) {
                                vanillaFallbackItem = holder.value();
                            } else {
                                otherItems.add(holder.value());
                            }
                        }
                    }
                    Item item = ftbMaterialsItem == null ? vanillaFallbackItem : ftbMaterialsItem;
                    if (item != null) {
                        otherItems.forEach(other -> db.addItemMapping(other, item));
                        db.addTagMapping(tag, item);
                    }
                }
            }
        }

        return db;
    }

    public void addItemMapping(Item from, Item to) {
        itemMap.put(BuiltInRegistries.ITEM.getKey(from).toString(), BuiltInRegistries.ITEM.getKey(to).toString());
    }

    public void addTagMapping(TagKey<Item> from, Item to) {
        tagMap.put(from.location().toString(), BuiltInRegistries.ITEM.getKey(to).toString());
    }

    public void save(Path path) throws IOException {
        var res = CODEC.encodeStart(JsonOps.INSTANCE, this);
        if (res.isSuccess()) {
            Files.writeString(path, res.getOrThrow().toString());
        }
    }

    private static <T> Set<TagKey<T>> collectTags(Resource type, ResourceType resourceType, CachedTagKeyLookup<T> cacheTagKeyLookup) {
        Set<TagKey<T>> tags = new HashSet<>();

        var resourceName = type.name().toLowerCase();
        var prefixRaw = resourceType.getUnifiedTagPrefix();

        if (!prefixRaw.isEmpty()) {
            String tagNames = resourceType.getTags().isEmpty() ? "c:" + prefixRaw : resourceType.getTags().getLast();
            for (String tagName : tagNames.split("\\|")) {
                var tag = cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, resourceName);
                tags.add(tag);
            }
        }

        return tags;
    }

    private Map<Item, Item> buildItemByItemMap() {
        Map<Item, Item> res = new HashMap<>();
        itemMap.forEach((in, out) ->
                BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(in)).ifPresent(itemIn ->
                        BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(out)).ifPresent(itemOut ->
                                res.put(itemIn, itemOut)
                        )
                ));
        return res;
    }
}
