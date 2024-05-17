package net.unseenhand.taskmanagermod.event;


import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.datagen.TMLanguageProvider;

@Mod.EventBusSubscriber(modid = TaskManagerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorsEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        if (event.includeClient()) {
            generator.addProvider(event.includeClient(), new TMLanguageProvider(output, "en_us"));
        }
    }
}
