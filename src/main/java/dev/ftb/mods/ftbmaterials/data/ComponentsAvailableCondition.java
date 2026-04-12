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
import java.util.Set;
import java.util.stream.Collectors;

public record ComponentsAvailableCondition(
        Set<String> usedMaterials
) implements ICondition {
    public static final MapCodec<ComponentsAvailableCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.listOf().fieldOf("used_materials")
                            .xmap(Set::copyOf, List::copyOf)
                            .forGetter(condition -> condition.usedMaterials)
            ).apply(instance, ComponentsAvailableCondition::new)
    );

    public static ComponentsAvailableCondition of(String... usedMaterials) {
        return new ComponentsAvailableCondition(Set.of(usedMaterials));
    }

    @SafeVarargs
    public static ComponentsAvailableCondition fromBlocks(DeferredHolder<Block, Block>... blocks) {
        return new ComponentsAvailableCondition(Set.of(blocks).stream().map(b -> b.getId().getPath()).collect(Collectors.toSet()));
    }

    @SafeVarargs
    public static ComponentsAvailableCondition fromItems(DeferredHolder<Item, Item>... items) {
        return new ComponentsAvailableCondition(Set.of(items).stream().map(b -> b.getId().getPath()).collect(Collectors.toSet()));
    }

    public static ComponentsAvailableCondition fromItemToBlock(DeferredHolder<Item, Item> item, DeferredHolder<Block , Block> block) {
        return new ComponentsAvailableCondition(Set.of(item.getId().getPath(), block.getId().getPath()));
    }

    public static ComponentsAvailableCondition fromBlockToItem(DeferredHolder<Block, Block> block, DeferredHolder<Item, Item> item) {
        return new ComponentsAvailableCondition(Set.of(item.getId().getPath(), block.getId().getPath()));
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
