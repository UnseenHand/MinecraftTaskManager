package net.unseenhand.taskmanagermod.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.util.PlayerConstants;
import net.unseenhand.taskmanagermod.util.PlayerTaskUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManager extends SavedData {

    private final Map<UUID, List<Task>> playerTaskListMap = new ConcurrentHashMap<>();

    public TaskManager() {
    }

    public TaskManager(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = tag.getList(PlayerConstants.TAG_PLAYER_TASK_DATA, ListTag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag playerTaskDataTag = list.getCompound(i);
            UUID uuid = UUID.fromString(playerTaskDataTag.getString(PlayerConstants.TAG_PLAYER_UUID));
            List<Task> taskList = new ArrayList<>();

            ListTag taskListTag = playerTaskDataTag.getList(PlayerConstants.TAG_TASKS, ListTag.TAG_COMPOUND);
            for (int j = 0; j < taskListTag.size(); j++) {
                CompoundTag taskTag = taskListTag.getCompound(j);
                String id = taskTag.getString(PlayerConstants.TAG_TASK_ID);
                String name = taskTag.getString(PlayerConstants.TAG_TASK_NAME);
                String description = taskTag.getString(PlayerConstants.TAG_TASK_DESCRIPTION);
                byte status = taskTag.getByte(PlayerConstants.TAG_TASK_STATUS);
                taskList.add(new Task(id, name, description, status));
            }

            playerTaskListMap.put(uuid, taskList);
        }
        TaskManagerMod.LOGGER.info("Loading TM data");
    }

    public static TaskManager get(Level level) {
        if (level.isClientSide) {
            return null;
        }

        DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
        // TaskManagerMod.LOGGER.info("Getting the level TD");
        return storage.computeIfAbsent(
                new SavedData.Factory<>(TaskManager::new,
                        TaskManager::new,
                        DataFixTypes.LEVEL), // TODO: Maybe should be Level instead of Player
                TaskManagerMod.MOD_NAME);
    }

    // TODO: Maybe make it not static, because you will have to process everything here on the server side
    public List<Task> getPlayerTaskList(UUID uuid) {
        return playerTaskListMap.get(uuid);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ListTag playerTaskDataTag = new ListTag();

        playerTaskListMap.forEach((uuid, taskList) -> {
            CompoundTag playerTasksTag = new CompoundTag();
            ListTag tasksListTag = new ListTag();
            taskList.forEach(task -> PlayerTaskUtil.addTaskTagToTaskListTag(task, tasksListTag));
            playerTasksTag.putString(PlayerConstants.TAG_PLAYER_UUID, uuid.toString());
            playerTasksTag.put(PlayerConstants.TAG_TASKS, tasksListTag);
            playerTaskDataTag.add(playerTasksTag);
        });

        tag.put(PlayerConstants.TAG_PLAYER_TASK_DATA, playerTaskDataTag);

        TaskManagerMod.LOGGER.info("Saving TM");

        return tag;
    }

    @Override
    public void setDirty() {
        TaskManagerMod.LOGGER.info("Setting Dirty");
        super.setDirty();
    }

    public void tick(Level level) {
        TaskManagerMod.LOGGER.info("Ticking");
    }

    // TODO: when adding 2 tasks the second is not saved to the WD
    public boolean addTaskToPlayer(Player player, Task task) {
        UUID uuid = player.getUUID();
        List<Task> oldTaskList = playerTaskListMap.getOrDefault(uuid, Collections.emptyList());
        List<Task> newTaskList = new ArrayList<>(oldTaskList);
        newTaskList.add(task);

        // If unable to replace - create
        if (!playerTaskListMap.replace(uuid, oldTaskList, newTaskList)) {
            playerTaskListMap.put(uuid, newTaskList);
        }

        // Successfully replaced data should be updated
        setDirty();
        TaskManagerMod.LOGGER.info("Add task to player");
        return true;
    }

    public boolean removeTaskFromPlayer(Player player, String id) {
        List<Task> taskList = playerTaskListMap.get(player.getUUID());
        taskList.removeIf(t -> Objects.equals(t.id(), id));
        playerTaskListMap.replace(player.getUUID(), taskList);
        setDirty();
        return true;
    }
}
