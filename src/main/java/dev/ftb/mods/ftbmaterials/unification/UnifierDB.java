package dev.ftb.mods.ftbmaterials.unification;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.data.ItemTagsGenerator;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UnifierDB {
    public static final Codec<UnifierDB> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.unboundedMap(Codec.STRING, Codec.STRING)
                    .fieldOf("items").forGetter(db -> db.itemMap),
            Codec.unboundedMap(Codec.STRING, Codec.STRING)
                    .fieldOf("item_tags").forGetter(db -> db.itemTagMap),
            Codec.unboundedMap(Codec.STRING, Codec.STRING)
                    .xmap((Function<Map<String, String>, Map<String, String>>) ConcurrentHashMap::new, Map::copyOf)
                    .fieldOf("ore_blocks").forGetter(db -> db.blockMap)
    ).apply(builder, UnifierDB::new));

    public static final UnifierDB EMPTY = new UnifierDB(Map.of(), Map.of(), Map.of());

    private final Map<String,String> itemMap;
    private final Map<String,String> itemTagMap;
    private final Lazy<Map<Item,Item>> itemByItemMap = Lazy.of(this::buildItemByItemMap);

    private final Map<String,String> blockMap;
    private final Lazy<Map<Block,Block>> blockByBlockMap = Lazy.of(this::buildBlockByBlockMap);

    private UnifierDB() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    private UnifierDB(Map<String, String> itemMap, Map<String, String> itemTagMap, Map<String, String> blockMap) {
        this.itemMap = itemMap;
        this.itemTagMap = itemTagMap;
        this.blockMap = blockMap;
    }

    public static UnifierDB load(Path path) throws IOException {
        JsonElement json = JsonParser.parseString(Files.readString(path));
        return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }

    public static UnifierDB build() {
        CachedTagKeyLookup<Item> itemCache = new CachedTagKeyLookup<>(Registries.ITEM);
        CachedTagKeyLookup<Block> blockCache = new CachedTagKeyLookup<>(Registries.BLOCK);
        UnifierDB db = new UnifierDB();

        for (Resource resource : Resource.values()) {
            for (ResourceType type : ResourceType.values()) {
                var itemTags = collectTags(resource, type, itemCache);
                db.buildItemTags(itemTags);
            }
            db.buildOreBlockMap(resource, blockCache);
        }

        return db;
    }

    public Optional<String> lookupItem(String key) {
        return Optional.ofNullable(itemMap.get(key));
    }

    public Optional<Item> lookupItem(Item item) {
        return Optional.ofNullable(itemByItemMap.get().get(item));
    }

    public Optional<String> lookupItemTag(String key) {
        return Optional.ofNullable(itemTagMap.get(key));
    }

    public Optional<Block> lookupBlock(Block block) {
        return Optional.ofNullable(blockByBlockMap.get().get(block));
    }

    public BlockState lookupBlock(BlockState state) {
        Block res = blockByBlockMap.get().get(state.getBlock());
        return res == null ? state : res.defaultBlockState();
    }

    private void buildItemTags(Set<TagKey<Item>> itemTags) {
        for (TagKey<Item> tag : itemTags) {
            Item ftbMaterialsItem = null;
            Item vanillaFallbackItem = null;
            Set<Item> otherItems = new HashSet<>();
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                if (holder.unwrapKey().isPresent()) {
                    var k = holder.unwrapKey().get();
                    if (k.identifier().getNamespace().equals(FTBMaterials.MOD_ID)) {
                        if (ftbMaterialsItem == null) {
                            ftbMaterialsItem = holder.value();
                        }
                    } else if (k.identifier().getNamespace().equals("minecraft")) {
                        vanillaFallbackItem = holder.value();
                    } else {
                        otherItems.add(holder.value());
                    }
                }
            }
            Item item = ftbMaterialsItem == null ? vanillaFallbackItem : ftbMaterialsItem;
            if (item != null) {
                otherItems.forEach(other -> addItemMapping(other, item));
                addTagMapping(tag, item);
            }
        }
        // special cases for silicon & sawdust
        specialCase(Resource.SILICON, ResourceType.GEM, ItemTagsGenerator.SILICON);
        specialCase(Resource.SAW, ResourceType.DUST, ItemTagsGenerator.DUST_WOODS);
    }

    private void specialCase(Resource resource, ResourceType resourceType, TagKey<Item> tag) {
        ResourceRegistries.get(resource).getItemFromType(resourceType).ifPresent(itemHolder ->
                addTagMapping(tag, itemHolder.get()));

        BuiltInRegistries.ITEM.get(tag).ifPresent(items -> {
            Item[] ftbItem = new Item[] { null };
            List<Item> otherItems = new ArrayList<>();
            items.stream().forEach(holder -> holder.unwrapKey().ifPresent(key -> {
                if (key.identifier().getNamespace().equals(FTBMaterials.MOD_ID)) {
                    ftbItem[0] = holder.value();
                } else {
                    otherItems.add(holder.value());
                }
            }));
            if (ftbItem[0] != null) {
                otherItems.forEach(item -> addItemMapping(item, ftbItem[0]));
            }
        });
    }

    private void buildOreBlockMap(Resource resource, CachedTagKeyLookup<Block> blockCache) {
        EnumMap<ResourceType, String> ftbOreMap = new EnumMap<>(ResourceType.class);
        EnumMap<ResourceType, Set<String>> otherOreMap = new EnumMap<>(ResourceType.class);

        var tag = blockCache.getOrCreateUnifiedTag("c:ores", resource.name().toLowerCase());
        for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
            for (ResourceType type : ResourceType.ORE_TYPES) {
                TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, Identifier.parse(type.getExtraBlockTag()));
                if (holder.is(tagKey)) {
                    holder.unwrapKey().ifPresent(resKey -> {
                        Identifier blockId = resKey.identifier();
                        if (blockId.getNamespace().equals(FTBMaterials.MOD_ID)) {
                            ftbOreMap.put(type, blockId.toString());
                        } else {
                            otherOreMap.computeIfAbsent(type, k -> new HashSet<>()).add(blockId.toString());
                        }
                    });
                }
            }
        }

        for (ResourceType type : ftbOreMap.keySet()) {
            if (otherOreMap.containsKey(type)) {
                String ftbBlockName = ftbOreMap.get(type);
                otherOreMap.get(type).forEach(otherBlockName -> {
                    blockMap.put(otherBlockName, ftbBlockName);
                    itemMap.put(otherBlockName, ftbBlockName);
                });
            }
        }
    }

    public void addItemMapping(Item from, Item to) {
        itemMap.put(BuiltInRegistries.ITEM.getKey(from).toString(), BuiltInRegistries.ITEM.getKey(to).toString());
    }

    public void addTagMapping(TagKey<Item> from, Item to) {
        itemTagMap.put(from.location().toString(), BuiltInRegistries.ITEM.getKey(to).toString());
    }

    public void save(Path path) throws IOException {
        var res = CODEC.encodeStart(JsonOps.INSTANCE, this);
        if (res.isSuccess()) {
            var gson = new Gson().newBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            Files.writeString(path, gson.toJson(res.getOrThrow()));
        }
    }

    private static <T> Set<TagKey<T>> collectTags(Resource type, ResourceType resourceType, CachedTagKeyLookup<T> cacheTagKeyLookup) {
        var resourceName = type.name().toLowerCase();

        return resourceType.getTags().stream()
                .map(tagName -> cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, resourceType.getResourceNameMutator().apply(resourceName)))
                .collect(Collectors.toSet());
    }

    private Map<Item, Item> buildItemByItemMap() {
        Map<Item, Item> res = new HashMap<>();
        itemMap.forEach((in, out) ->
                BuiltInRegistries.ITEM.getOptional(Identifier.parse(in)).ifPresent(itemIn ->
                        BuiltInRegistries.ITEM.getOptional(Identifier.parse(out)).ifPresent(itemOut ->
                                res.put(itemIn, itemOut)
                        )
                ));
        return res;
    }

    private Map<Block, Block> buildBlockByBlockMap() {
        Map<Block, Block> res = new ConcurrentHashMap<>();
        blockMap.forEach((in, out) ->
                BuiltInRegistries.BLOCK.getOptional(Identifier.parse(in)).ifPresent(blockIn ->
                        BuiltInRegistries.BLOCK.getOptional(Identifier.parse(out)).ifPresent(blockOut ->
                                res.put(blockIn, blockOut)
                        )
                ));
        return res;
    }
}
