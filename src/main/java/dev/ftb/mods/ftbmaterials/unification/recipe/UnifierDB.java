package dev.ftb.mods.ftbmaterials.unification.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbmaterials.FTBMaterials;
import dev.ftb.mods.ftbmaterials.config.Blacklists;
import dev.ftb.mods.ftbmaterials.data.ItemTagsGenerator;
import dev.ftb.mods.ftbmaterials.resources.Resource;
import dev.ftb.mods.ftbmaterials.resources.ResourceRegistries;
import dev.ftb.mods.ftbmaterials.resources.ResourceType;
import dev.ftb.mods.ftbmaterials.util.CachedTagKeyLookup;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.util.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UnifierDB {
    private static final Codec<Map<String, String>> MUTABLE_STRING_MAP = Codec.unboundedMap(Codec.STRING, Codec.STRING)
            .xmap((Function<Map<String, String>, Map<String, String>>) ConcurrentHashMap::new, Function.identity());

    public static final Codec<UnifierDB> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            MUTABLE_STRING_MAP.optionalFieldOf("items", new ConcurrentHashMap<>()).forGetter(db -> db.itemMap),
            MUTABLE_STRING_MAP.optionalFieldOf("item_tags", new ConcurrentHashMap<>()).forGetter(db -> db.itemTagMap),
            MUTABLE_STRING_MAP.optionalFieldOf("fluids", new ConcurrentHashMap<>()).forGetter(db -> db.fluidMap),
            MUTABLE_STRING_MAP.optionalFieldOf("fluid_tags", new ConcurrentHashMap<>()).forGetter(db -> db.fluidTagMap),
            MUTABLE_STRING_MAP.optionalFieldOf("ore_blocks", new ConcurrentHashMap<>()).forGetter(db -> db.blockMap)
    ).apply(builder, UnifierDB::new));

    public static final UnifierDB EMPTY = new UnifierDB(Map.of(), Map.of(), Map.of(), Map.of(), Map.of());

    private final Map<String,String> itemMap;
    private final Map<String,String> itemTagMap;
    private final Map<String,String> fluidMap;
    private final Map<String,String> fluidTagMap;
    private final Map<String,String> blockMap;

    private final Lazy<Map<Item,Item>> itemByItemMap = Lazy.of(this::buildItemByItemMap);
    private final Lazy<Map<Fluid,Fluid>> fluidByFluidMap = Lazy.of(this::buildFluidByFluidMap);
    private final Lazy<Map<Block,Block>> blockByBlockMap = Lazy.of(this::buildBlockByBlockMap);

    private UnifierDB() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    private UnifierDB(Map<String, String> itemMap, Map<String, String> itemTagMap, Map<String, String> fluidMap, Map<String, String> fluidTagMap, Map<String, String> blockMap) {
        this.itemMap = itemMap;
        this.itemTagMap = itemTagMap;
        this.fluidMap = fluidMap;
        this.fluidTagMap = fluidTagMap;
        this.blockMap = blockMap;
    }

    static UnifierDB load(Path path) throws IOException {
        JsonElement json = JsonParser.parseString(Files.readString(path));
        UnifierDB unifierDB = CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        UnifierManager.loadExtraJsonFiles(UnifierManager.UNIFIER_DIR, el ->
                unifierDB.addExtraUnifierEntries(CODEC.parse(JsonOps.INSTANCE, el).getOrThrow()));
        return unifierDB;
    }

    private void addExtraUnifierEntries(UnifierDB extra) {
        itemMap.putAll(extra.itemMap);
        itemTagMap.putAll(extra.itemTagMap);
        fluidMap.putAll(extra.fluidMap);
        fluidTagMap.putAll(extra.fluidTagMap);
        blockMap.putAll(extra.blockMap);
    }

    static UnifierDB build() {
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

    public Optional<String> lookupFluid(String key) {
        return Optional.ofNullable(fluidMap.get(key));
    }

    public Optional<Fluid> lookupFluid(Fluid fluid) {
        return Optional.ofNullable(fluidByFluidMap.get().get(fluid));
    }

    public Optional<String> lookupFluidTag(String key) {
        return Optional.ofNullable(fluidTagMap.get(key));
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
                    if (k.location().getNamespace().equals(FTBMaterials.MOD_ID)) {
                        if (ftbMaterialsItem == null) {
                            ftbMaterialsItem = holder.value();
                        }
                    } else if (k.location().getNamespace().equals("minecraft")) {
                        vanillaFallbackItem = holder.value();
                    } else {
                        otherItems.add(holder.value());
                    }
                }
            }
            Item item = ftbMaterialsItem == null ? vanillaFallbackItem : ftbMaterialsItem;
            if (item != null) {
                otherItems.forEach(other -> addItemMapping(other, item));
                addItemTagMapping(tag, item);
            }
        }
        // special cases for silicon & sawdust
        specialCase(Resource.SILICON, ResourceType.GEM, ItemTagsGenerator.C_SILICON);
        specialCase(Resource.SAW, ResourceType.DUST, ItemTagsGenerator.C_DUSTS_WOOD);
    }

    private void specialCase(Resource resource, ResourceType resourceType, TagKey<Item> tag) {
        ResourceRegistries.get(resource).getItemFromType(resourceType).ifPresent(itemHolder ->
                addItemTagMapping(tag, itemHolder.get()));

        BuiltInRegistries.ITEM.getTag(tag).ifPresent(items -> {
            Item[] ftbItem = new Item[] { null };
            List<Item> otherItems = new ArrayList<>();
            items.stream().forEach(holder -> holder.unwrapKey().ifPresent(key -> {
                if (key.location().getNamespace().equals(FTBMaterials.MOD_ID)) {
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
                TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, ResourceLocation.parse(type.getExtraBlockTag()));
                if (holder.is(tagKey)) {
                    holder.unwrapKey().ifPresent(resKey -> {
                        ResourceLocation blockId = resKey.location();
                        if (blockId.getNamespace().equals(FTBMaterials.MOD_ID)) {
                            ftbOreMap.put(type, blockId.toString());
                        } else if (Blacklists.isBlockUnificationAllowed(blockId)) {
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
        if (Blacklists.isItemUnificationAllowed(from)) {
            itemMap.put(BuiltInRegistries.ITEM.getKey(from).toString(), BuiltInRegistries.ITEM.getKey(to).toString());
        }
    }

    public void addItemTagMapping(TagKey<Item> from, Item to) {
        if (Blacklists.isItemUnificationAllowed(from)) {
            itemTagMap.put(from.location().toString(), BuiltInRegistries.ITEM.getKey(to).toString());
        }
    }

    public void save(Path path) throws IOException {
        var res = CODEC.encodeStart(JsonOps.INSTANCE, this);
        if (res.isSuccess() && res.getOrThrow() instanceof JsonObject json) {
            var gson = new Gson().newBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            Files.writeString(path, gson.toJson(sortJsonObject(json)));
        }
    }

    private static JsonObject sortJsonObject(JsonObject jsonObject) {
        List<String> keySet = jsonObject.keySet().stream().sorted().toList();
        JsonObject res = new JsonObject();
        for (String key : keySet) {
            JsonElement ele = jsonObject.get(key);
            if (ele.isJsonObject()) {
                ele = sortJsonObject(ele.getAsJsonObject());
                res.add(key, ele);
            } else if (ele.isJsonArray()) {
                res.add(key, ele.getAsJsonArray());
            } else
                res.add(key, ele.getAsJsonPrimitive());
        }
        return res;
    }

    private static <T> Set<TagKey<T>> collectTags(Resource type, ResourceType resourceType, CachedTagKeyLookup<T> cacheTagKeyLookup) {
        var resourceName = type.name().toLowerCase();

        return resourceType.getTags().stream()
                .map(tagName -> cacheTagKeyLookup.getOrCreateUnifiedTag(tagName, resourceType.getResourceNameMutator().apply(resourceName)))
                .collect(Collectors.toSet());
    }

    private Map<Item, Item> buildItemByItemMap() {
        return buildXbyXMap(itemMap, BuiltInRegistries.ITEM);
    }

    private Map<Fluid, Fluid> buildFluidByFluidMap() {
        return buildXbyXMap(fluidMap, BuiltInRegistries.FLUID);
    }

    private Map<Block, Block> buildBlockByBlockMap() {
        return buildXbyXMap(blockMap, BuiltInRegistries.BLOCK);
    }

    private static <T> Map<T, T> buildXbyXMap(Map<String,String> unifierMap, Registry<T> registry) {
        Map<T, T> res = new HashMap<>();
        unifierMap.forEach((in, out) ->
                registry.getOptional(ResourceLocation.parse(in)).ifPresent(objIn ->
                        registry.getOptional(ResourceLocation.parse(out)).ifPresent(objOut ->
                                res.put(objIn, objOut)
                        )
                ));
        return res;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
