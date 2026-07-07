package dev.ftb.mods.ftbmaterials.config;

import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.snbt.config.BaseValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StringStringMapValue extends BaseValue<Map<String, Map<String,String>>> {
    protected StringStringMapValue(@Nullable SNBTConfig c, String n, Map<String, Map<String, String>> def) {
        super(c, n, def);

        super.set(new HashMap<>(def));
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        SNBTCompoundTag mapTag = new SNBTCompoundTag();
        get().forEach((k, subMap) -> {
            mapTag.put(k, Util.make(new CompoundTag(), t -> subMap.forEach(t::putString)));
        });

        tag.put(this.key, mapTag);
        comment.forEach(c -> tag.comment(key, c));
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        Map<String, Map<String, String>> map = new HashMap<>();

        SNBTCompoundTag mapTag = tag.getCompound(key);
        for (String key : mapTag.getAllKeys()) {
            Map<String, String> subMap = new HashMap<>();
            map.put(key, subMap);
            CompoundTag subTag = mapTag.getCompound(key);
            subTag.getAllKeys().forEach(k1 -> subMap.put(k1, subTag.getString(k1)));
        }

        set(map);
    }
}
