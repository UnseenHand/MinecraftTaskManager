package net.unseenhand.taskmanagermod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.unseenhand.taskmanagermod.TaskManagerMod;

@Mod.EventBusSubscriber(modid = TaskManagerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindings {
    public static final String KEY_OPEN_TASK_MANAGER_SCREEN = "key.openTaskManagerScreen";
    public static final String KEY_CATEGORIES_TASK_MANAGER = "key.categories.taskmanager";
    public static KeyMapping openTaskManagerScreenKeyMapping;

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        openTaskManagerScreenKeyMapping = new KeyMapping(KEY_OPEN_TASK_MANAGER_SCREEN, KeyConflictContext.IN_GAME,
                InputConstants.getKey(InputConstants.KEY_G, -1), KEY_CATEGORIES_TASK_MANAGER);
        event.register(openTaskManagerScreenKeyMapping);
    }
}
