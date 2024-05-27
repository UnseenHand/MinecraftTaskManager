package net.unseenhand.taskmanagermod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.gui.TaskEntry;
import net.unseenhand.taskmanagermod.gui.TaskSelectionList;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class S2CRefreshTSLOnTickPacket implements SimplePacketBase {
    private final List<Task> tasks;
    private final List<String> ignoredTaskIds;

    public S2CRefreshTSLOnTickPacket(List<Task> tasks, List<String> ignoredTaskIds, Predicate<Task> predicate) {
        // .stream().filter(predicate).toList()
        this(tasks, ignoredTaskIds); // Used filtering
    }

    public S2CRefreshTSLOnTickPacket(List<Task> tasks, List<String> ignoredTaskIds) {
        this.tasks = tasks;
        this.ignoredTaskIds = ignoredTaskIds;
    }

    public S2CRefreshTSLOnTickPacket(FriendlyByteBuf buf) {
        tasks = buf.readList(b -> new Task(b.readUtf(), b.readUtf(), b.readUtf(), b.readByte()));
        ignoredTaskIds = buf.readList(FriendlyByteBuf::readUtf);
    }

    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isServerSide()) {
            return false;
        }

        TaskManagerMod.LOGGER.info(String.valueOf(tasks.size()));

        ignoredTaskIds.forEach((ignored) -> tasks.removeIf((t) -> Objects.equals(t.id(), ignored)));

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
        buf.writeCollection(ignoredTaskIds, FriendlyByteBuf::writeUtf);
    }
}
