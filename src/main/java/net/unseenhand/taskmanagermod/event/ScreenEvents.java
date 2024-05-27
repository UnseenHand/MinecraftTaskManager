package net.unseenhand.taskmanagermod.event;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.network.C2SRefreshTSLTasksOnScreenActionPacket;
import net.unseenhand.taskmanagermod.network.C2SSetFilterOnBtnClickPacket;
import net.unseenhand.taskmanagermod.network.PacketHandler;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = TaskManagerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ScreenEvents {
    @SubscribeEvent
    public static void onScreenBtnClick(ScreenEvent.MouseButtonReleased.Post event) {
        if (event.getScreen() instanceof TaskManagerScreen screen) {
            Optional<GuiEventListener> optional = screen.getChildAt(event.getMouseX(), event.getMouseY());
            if (optional.isPresent()) {
                if (optional.get() instanceof AbstractButton button) {
                    int clickedMouseBtn = event.getButton();
                    if (button.isMouseOver(event.getMouseX(), event.getMouseY()) &&
                            clickedMouseBtn == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        PacketHandler.sendToServer(new C2SRefreshTSLTasksOnScreenActionPacket());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof TaskManagerScreen) {
            PacketHandler.sendToServer(new C2SRefreshTSLTasksOnScreenActionPacket());
        }
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        if (event.getScreen() instanceof TaskManagerScreen.PopupScreen) {
            PacketHandler.sendToServer(new C2SRefreshTSLTasksOnScreenActionPacket());
        }
    }

    @SubscribeEvent
    public static void onScreenKeyPressed(ScreenEvent.KeyPressed event) {
//        if (event.getScreen() instanceof TaskManagerScreen taskManagerScreen) {
//            EditBox editBox = taskManagerScreen.getSearchTextField();
//            if (editBox != null && editBox.isFocused()) {
//                taskManagerScreen.getTaskSelectionList().updateModCount();
//            }
//        }
    }
}
