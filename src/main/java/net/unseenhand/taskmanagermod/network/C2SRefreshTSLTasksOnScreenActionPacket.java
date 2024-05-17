package net.unseenhand.taskmanagermod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;
import net.unseenhand.taskmanagermod.data.TaskManager;
import net.unseenhand.taskmanagermod.gui.TaskSelectionList;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.screen.TaskManagerScreen;

import java.util.List;

public class C2SRefreshTSLTasksOnScreenActionPacket implements SimplePacketBase {

    public C2SRefreshTSLTasksOnScreenActionPacket() {

    }

    public C2SRefreshTSLTasksOnScreenActionPacket(FriendlyByteBuf buf) {

    }

    // TODO: This packet has to much responsibility, try to separate the server-side and client-side processing
    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isClientSide()) {
            return false;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof TaskManagerScreen) {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(PlayerTasksProvider.PLAYER_TASKS).ifPresent((tasks) -> {
                    TaskSelectionList.setTasks(tasks.getTasks());
                });
                return true;
//                TaskManager taskManager = TaskManager.get(player.level());
//                if (taskManager != null) {
//                    List<Task> taskList = taskManager.getPlayerTaskList(player.getUUID());
//                    if (taskList != null) {
//                        TaskSelectionList.setTasks(taskList);
//                        return true;
//                    }
//                }
            }
        }

        return true;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {

    }
}
