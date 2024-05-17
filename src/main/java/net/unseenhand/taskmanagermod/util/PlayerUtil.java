package net.unseenhand.taskmanagermod.util;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.level.storage.WorldData;
import net.unseenhand.taskmanagermod.model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerUtil {
    private static final Map<UUID, List<Task>> playerTaskListMap = new HashMap<>();

    private PlayerUtil() {
    }

    public static void updatePlayerAdditionalData(final Player player, CompoundTag additionalData) {
        if (playerTaskListMap.isEmpty()) { // If there is no data yet
            playerTaskListMap.put(player.getUUID(), toListFromNbtTag(additionalData)); // First player data
        } else if (playerTaskListMap.getOrDefault(player.getUUID(), null) == null) { // No player with this UUID is
            // mapped yet
            playerTaskListMap.put(player.getUUID(), toListFromNbtTag(additionalData)); // First player data
        } else if (playerTaskListMap.get(player.getUUID()) != null) {
            List<Task> oldTaskList = playerTaskListMap.get(player.getUUID());
            List<Task> newTaskList = toListFromNbtTag(additionalData);

            if (!oldTaskList.equals(newTaskList)) {
                newTaskList.addAll(oldTaskList);

                Map<String, Task> mapById = newTaskList.stream().collect(Collectors.toMap(Task::id, obj -> obj,
                        (existing, replacement) -> existing));

                playerTaskListMap.put(player.getUUID(), new ArrayList<>(mapById.values()));
            }
        }
    }

    public static CompoundTag getPlayerAdditionalData(final Player player) {
        return new CompoundTag();// toNbtTagFromList(playerTaskListMap.get(player.getUUID()));
    }

//    private static CompoundTag toNbtTagFromList(List<Task> tasks) {
//        CompoundTag tasksListTagObj = new CompoundTag();
//        ListTag tasksTag = new ListTag();
//        for (Task task : tasks) {
//            CompoundTag taskTag = new CompoundTag();
//            taskTag.putString(PlayerConstants.TAG_TASK_ID, task.id());
//            taskTag.putString(PlayerConstants.TAG_TASK_NAME, task.name());
//            taskTag.putString(PlayerConstants.TAG_TASK_DESCRIPTION, task.description());
//            taskTag.putByte(PlayerConstants.TAG_TASK_STATUS, (byte) task.status());
//            tasksTag.add(taskTag);
//        }
//
//        tasksListTagObj.put(PlayerConstants.TAG_TASKS, tasksTag);
//
//        return tasksListTagObj;
//    }

    private static List<Task> toListFromNbtTag(CompoundTag playerData) {
        CompoundTag tasksData = playerData.getCompound(PlayerConstants.TAG_TASKS_DATA);

        List<Task> taskList = new ArrayList<>();

        ListTag listTag = playerData.getList(PlayerConstants.TAG_TASKS, Tag.TAG_COMPOUND);
        PlayerTaskUtil.addTaskToTaskList(listTag, taskList);

        return taskList;
    }

    public static void loadPlayerAdditionalData(final Player player, CompoundTag additionalData) {
        playerTaskListMap.put(player.getUUID(), toListFromNbtTag(additionalData)); // First player data
    }
}
