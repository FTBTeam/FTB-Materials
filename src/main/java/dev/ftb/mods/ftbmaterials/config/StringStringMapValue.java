package dev.ftb.mods.ftbmaterials.config;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftblibrary.config.value.AbstractMapValue;
import dev.ftb.mods.ftblibrary.config.value.Config;

import java.util.Map;

public class StringStringMapValue extends AbstractMapValue<Map<String,String>> {
    protected StringStringMapValue(Config parent, String key, Map<String, Map<String, String>> defaultValue) {
        super(parent, key, defaultValue, Codec.unboundedMap(Codec.STRING, Codec.STRING));
    }
}
