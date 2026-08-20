package dev.ftb.mods.ftbmaterials.unification.recipe;

import dev.ftb.mods.ftbmaterials.config.StartupConfig;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

enum MappingType {
    NONE(
            (_, in) -> Mappings.none(in),
            (in, _, _) -> in
    ),
    ITEM(
            (db, in) -> Mappings.create(in, db::lookupItem, db::lookupItemTag),
            (in, mappings, modId) -> in
                    .replace("<item_map>", StartupConfig.itemOverride(mappings.objMapping, modId))
                    .replace("<item_tag_map>", StartupConfig.itemTagOverride(mappings.tagMapping, modId))
                    .replace("<item_or_tag_map>", StartupConfig.itemTagOverride(mappings.objOrTagMapping(), modId))
    ),
    FLUID(
            (db, in) -> Mappings.create(in, db::lookupFluid, db::lookupFluidTag),
            (in, mappings, _) -> in
                    .replace("<fluid_map>", mappings.objMapping)
                    .replace("<fluid_tag_map>", mappings.tagMapping)
                    .replace("<fluid_or_tag_map>", mappings.objOrTagMapping())
    );

    private final BiFunction<UnifierDB, String, Mappings> mappingFactory;
    private final ReplacerFunc replacer;

    MappingType(BiFunction<UnifierDB, String, Mappings> mappingFactory, ReplacerFunc replacer) {
        this.mappingFactory = mappingFactory;
        this.replacer = replacer;
    }

    static MappingType fromReplacementString(String str) {
        if (str.contains("<item_")) {
            return ITEM;
        } else if (str.contains("<fluid_")) {
            return FLUID;
        } else {
            return NONE;
        }
    }

    Mappings createMappings(UnifierDB db, String input) {
        return mappingFactory.apply(db, input);
    }

    String doReplacement(String input, Mappings mappings, String modId) {
        return replacer.replace(input, mappings, modId);
    }

    @FunctionalInterface
    private interface ReplacerFunc {
        String replace(String input, Mappings mappings, String modId);
    }

    record Mappings(boolean isTag, String objMapping, String tagMapping) {
        public static Mappings none(String original) {
            return new Mappings(false, original, original);
        }

        public static Mappings create(String original, Function<String, Optional<String>> objMapper, Function<String, Optional<String>> tagMapper) {
            return original.startsWith("#") ?
                    new Mappings(true, original, tagMapper.apply(original.substring(1)).orElse(original)) :
                    new Mappings(false, objMapper.apply(original).orElse(original), original);
        }

        String objOrTagMapping() {
            return isTag ? tagMapping : objMapping;
        }

        boolean differsFromInput(String input) {
            return !objMapping.equals(input) || !tagMapping.equals(input);
        }
    }
}
