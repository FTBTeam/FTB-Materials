package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = FTBMaterials.MOD_ID)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        var packOutput = event.getGenerator().getPackOutput();
        var lookupProvider = event.getLookupProvider();

        event.addProvider(new LanguageGenerator(packOutput));
        event.addProvider(new BlockTagsGenerator(packOutput, lookupProvider));
        event.addProvider(new ItemTagsGenerator(packOutput, lookupProvider));
        event.addProvider(new LootTableGenerator(packOutput, lookupProvider));
        event.addProvider(new RecipesGenerator.Runner(packOutput, lookupProvider));
        event.addProvider(new LootModifiersGenerator(packOutput, lookupProvider));
    }

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.addProvider(new FTBMaterialsModelProvider(event.getGenerator().getPackOutput()));
    }
}
