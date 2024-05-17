package net.unseenhand.taskmanagermod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.gui.TaskEntry;
import net.unseenhand.taskmanagermod.gui.TaskSelectionList;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.screen.TaskManagerScreen;

import java.util.List;

public class S2CRefreshTSLOnTickPacket implements SimplePacketBase {
    private final List<Task> tasks;

    public S2CRefreshTSLOnTickPacket(List<Task> tasks) {
        this.tasks = tasks;
    }

    public S2CRefreshTSLOnTickPacket(FriendlyByteBuf buf) {
        tasks = buf.readList(b -> new Task(b.readUtf(), b.readUtf(), b.readUtf(), b.readByte()));
    }

    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isServerSide()) {
            return false;
        }

        if (Minecraft.getInstance().screen instanceof TaskManagerScreen screen) {
            TaskSelectionList taskSelectionList = screen.getTaskSelectionList();

            // Render condition (if new size is not the same)
            for (Task task : tasks) {
                taskSelectionList.addEntry(new TaskEntry(taskSelectionList, task));
            }
        }

        return true;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(tasks, (b, task) -> {
            buf.writeUtf(task.id());
            buf.writeUtf(task.name());
            buf.writeUtf(task.description());
            buf.writeByte(task.status());
        });
    }
}
