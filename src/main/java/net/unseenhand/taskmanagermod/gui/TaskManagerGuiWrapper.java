package net.unseenhand.taskmanagermod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;

public class TaskManagerGuiWrapper {
    public static void openGUI(Minecraft mc, Level level, Player player) {
        mc.setScreen(new TaskManagerScreen(level, player));
    }
}
