package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.config.DisabledMaterialList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.stream.Stream;

public record ComponentsAvailableCondition(
        List<String> usedMaterials
) implements ICondition {
    public static final MapCodec<ComponentsAvailableCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.listOf().fieldOf("used_materials")
                            .forGetter(condition -> condition.usedMaterials)
            ).apply(instance, ComponentsAvailableCondition::new)
    );

    public static ComponentsAvailableCondition of(String... usedMaterials) {
        return new ComponentsAvailableCondition(List.of(usedMaterials));
    }

    @SafeVarargs
    public static ComponentsAvailableCondition fromBlocks(DeferredHolder<Block, Block>... blocks) {
        return new ComponentsAvailableCondition(Stream.of(blocks).map(b -> b.getId().getPath()).toList());
    }

    @SafeVarargs
    public static ComponentsAvailableCondition fromItems(DeferredHolder<Item, Item>... items) {
        return new ComponentsAvailableCondition(Stream.of(items).map(b -> b.getId().getPath()).toList());
    }

    public static ComponentsAvailableCondition fromItemToBlock(DeferredHolder<Item, Item> item, DeferredHolder<Block , Block> block) {
        return new ComponentsAvailableCondition(List.of(item.getId().getPath(), block.getId().getPath()));
    }

    public static ComponentsAvailableCondition fromBlockToItem(DeferredHolder<Block, Block> block, DeferredHolder<Item, Item> item) {
        return new ComponentsAvailableCondition(List.of(item.getId().getPath(), block.getId().getPath()));
    }

    @Override
    public boolean test(IContext context) {
        var config = DisabledMaterialList.DISABLED_MATERIALS.get();
        for (String material : usedMaterials) {
            if (config.contains(material)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
