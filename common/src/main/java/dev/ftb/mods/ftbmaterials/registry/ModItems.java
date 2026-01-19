package dev.ftb.mods.ftbmaterials.registry;

import dev.ftb.mods.ftbmaterials.xplat.registry.XRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final XRegistry<Item> REGISTRY = XRegistry.create(Registries.ITEM);

    // TODO: Creative tab!
    public static final Item.Properties DEFAULT_PROPS = new Item.Properties();
}
