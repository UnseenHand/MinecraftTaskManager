package net.unseenhand.taskmanagermod.event;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.screen.TaskManagerScreen;

/**
 * Handles the mouse button clicks events
 */
@Mod.EventBusSubscriber(modid = TaskManagerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ButtonClickEvents {
    /**
     * The default constructor
     */
    public ButtonClickEvents() {
    }

    /**
     * The method intends to efficiently handle the incoming event.
     * Here shouldn't be any hard logic, but instead some validation
     *
     * @param event the preemptive event that can prevent unnecessary handling
     */
    @SubscribeEvent
    public static void onButtonClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getScreen() instanceof TaskManagerScreen screen) {
            GuiEventListener button = screen.getAddTaskButton();
            if (screen.getFocused() == button) {
                // PacketHandler.sendToServer(new C2SAddTaskToPlayerOnButtonClickPacket());
            }
        }
    }
}
