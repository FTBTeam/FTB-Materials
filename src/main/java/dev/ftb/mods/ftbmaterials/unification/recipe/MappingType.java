package dev.ftb.mods.ftbmaterials.unification.recipe;

import dev.ftb.mods.ftbmaterials.config.StartupConfig;

import java.util.function.BiFunction;

enum MappingType {
    NONE(
            (_, in) -> new Mappings(in, in),
            (in, _, _) -> in
    ),
    ITEM(
            (db, in) -> new Mappings(db.lookupItem(in).orElse(in), db.lookupItemTag(in).orElse(in)),
            (in, mappings, modId) -> in
                    .replace("<item_map>", StartupConfig.itemOverride(mappings.objMapping, modId))
                    .replace("<item_tag_map>", StartupConfig.itemTagOverride(mappings.tagMapping, modId))
    ),
    FLUID(
            (db, in) -> new Mappings(db.lookupFluid(in).orElse(in), db.lookupFluidTag(in).orElse(in)),
            (in, mappings, _) -> in
                    .replace("<fluid_map>", mappings.objMapping)
                    .replace("<fluid_tag_map>", mappings.tagMapping)
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

    record Mappings(String objMapping, String tagMapping) {
        boolean differsFromInput(String input) {
            return !objMapping.equals(input) || !tagMapping.equals(input);
        }
    }
}
