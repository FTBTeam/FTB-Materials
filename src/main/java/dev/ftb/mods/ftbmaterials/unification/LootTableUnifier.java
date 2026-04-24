package dev.ftb.mods.ftbmaterials.unification;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbmaterials.config.StartupConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class LootTableUnifier extends LootModifier {
    public static final Supplier<MapCodec<LootTableUnifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(
            builder -> codecStart(builder).apply(builder, LootTableUnifier::new))
    );

    public LootTableUnifier(LootItemCondition[] lootItemConditions, int priority) {
        super(lootItemConditions, priority);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> itemsIn, LootContext lootContext) {
        if (!StartupConfig.TWEAK_LOOT_TABLES.get()) {
            return itemsIn;
        }

        ObjectArrayList<ItemStack> itemsOut = new ObjectArrayList<>();

        itemsIn.forEach(stack ->
                UnifierManager.INSTANCE.unifierDB().lookupItem(stack.getItem()).ifPresentOrElse(
                        mapped -> itemsOut.add(mapped.getDefaultInstance()),
                        () -> itemsOut.add(stack)
                ));

        return itemsOut;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
