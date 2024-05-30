package net.unseenhand.taskmanagermod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;
import net.unseenhand.taskmanagermod.gui.TaskSelectionList;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;

public class C2SRefreshTSLTasksOnScreenActionPacket implements SimplePacketBase {

    public C2SRefreshTSLTasksOnScreenActionPacket() {

    }

    public C2SRefreshTSLTasksOnScreenActionPacket(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isClientSide()) {
            return false;
        }

        Minecraft mc = Minecraft.getInstance();
        Screen screen = mc.screen;
        if (screen instanceof TaskManagerScreen || screen instanceof TaskManagerScreen.PopupScreen) {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(PlayerTasksProvider.PLAYER_TASKS).ifPresent((tasks) -> {
                    TaskSelectionList.setTasks(tasks.getTasks());
                });
                return true;
            }
        }

        return true;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {

    }
}
