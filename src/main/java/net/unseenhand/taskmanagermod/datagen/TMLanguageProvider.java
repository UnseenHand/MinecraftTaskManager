package net.unseenhand.taskmanagermod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.client.KeyBindings;

public class TMLanguageProvider extends LanguageProvider {
    public TMLanguageProvider(PackOutput output, String locale) {
        super(output, TaskManagerMod.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add(KeyBindings.KEY_CATEGORIES_TASK_MANAGER, "Task Manager Keys");
        add(KeyBindings.KEY_OPEN_TASK_MANAGER_SCREEN, "Open Task Manager Screen");
    }
}
