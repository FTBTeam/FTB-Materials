package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(modid = FTBMaterials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent event) {
        var packOutput = event.getGenerator().getPackOutput();
        var lookupProvider = event.getLookupProvider();

        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new LanguageGenerator(packOutput));
        BlockTagsGenerator blockTagGenerator = new BlockTagsGenerator(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTagGenerator);
        generator.addProvider(true, new ItemTagsGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        generator.addProvider(true, new LootTableGenerator(packOutput, lookupProvider));
        generator.addProvider(true, new RecipesGenerator(packOutput));
        generator.addProvider(true, new LootModifiersGenerator(packOutput, lookupProvider));

        generator.addProvider(true, new FTBMaterialsModelProvider.BlockState(packOutput, existingFileHelper));
        generator.addProvider(true, new FTBMaterialsModelProvider.ItemModel(packOutput, existingFileHelper));
    }
}
