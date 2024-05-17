package net.unseenhand.taskmanagermod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.unseenhand.taskmanagermod.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerTaskUtil {
    private static final Map<Byte, String> statusMap = loadDefaultStatuses();
    private static final String DEFAULT_VALUE = "No Status Value Reserved";

    public static void addTaskTagToTaskListTag(Task task, ListTag tasksListTag) {
        CompoundTag taskTag = new CompoundTag();
        taskTag.putString(PlayerConstants.TAG_TASK_ID, task.id());
        taskTag.putString(PlayerConstants.TAG_TASK_NAME, task.name());
        taskTag.putString(PlayerConstants.TAG_TASK_DESCRIPTION, task.description());
        taskTag.putByte(PlayerConstants.TAG_TASK_STATUS, task.status());
        tasksListTag.add(taskTag);
    }

    public static void addTaskToTaskList(ListTag listTag, List<Task> taskList) {
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag tag = listTag.getCompound(i);
            String id = tag.getString(PlayerConstants.TAG_TASK_ID);
            String name = tag.getString(PlayerConstants.TAG_TASK_NAME);
            String description = tag.getString(PlayerConstants.TAG_TASK_DESCRIPTION);
            byte status = tag.getByte(PlayerConstants.TAG_TASK_STATUS);
            taskList.add(new Task(id, name, description, status));
        }
    }

    private static Map<Byte, String> loadDefaultStatuses() {
        Map<Byte, String> map = new HashMap<>();
        map.put((byte) 0, "Open");
        map.put((byte) 1, "Closed");
        map.put((byte) 2, "In Progress");
        map.put((byte) 3, "Reviewing");
        map.put((byte) 4, "Rejected");
        return map;
    }

    public static String getStatusFromMap(byte status) {
        return statusMap.getOrDefault(status, DEFAULT_VALUE);
    }
}
