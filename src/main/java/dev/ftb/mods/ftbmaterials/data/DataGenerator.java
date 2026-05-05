package dev.ftb.mods.ftbmaterials.data;

import dev.ftb.mods.ftbmaterials.FTBMaterials;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = FTBMaterials.MOD_ID)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent event) {
        var packOutput = event.getGenerator().getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        event.addProvider(new LanguageGenerator(packOutput));
        BlockTagsGenerator blockTagGenerator = new BlockTagsGenerator(packOutput, lookupProvider, existingFileHelper);
        event.addProvider(blockTagGenerator);
        event.addProvider(new ItemTagsGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        event.addProvider(new LootTableGenerator(packOutput, lookupProvider));
        event.addProvider(new RecipesGenerator(packOutput, lookupProvider));
        event.addProvider(new LootModifiersGenerator(packOutput, lookupProvider));
        event.addProvider(new FTBMaterialsModelProvider.BlockState(event.getGenerator().getPackOutput(), existingFileHelper));
        event.addProvider(new FTBMaterialsModelProvider.ItemModel(event.getGenerator().getPackOutput(), existingFileHelper));
    }
}
