package net.unseenhand.taskmanagermod.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.util.PlayerConstants;
import net.unseenhand.taskmanagermod.util.PlayerTaskUtil;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerTasks {
    private List<Task> tasks;

    public PlayerTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public PlayerTasks() {
        tasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void removeTaskById(String id) {
        tasks.removeIf(task -> Objects.equals(task.id(), id));
    }

    public void copyFrom(PlayerTasks playerTasks) {
        tasks = playerTasks.tasks;
    }

    public void saveNBTData(CompoundTag tag) {
        ListTag listTag = new ListTag();
        tasks.forEach((task) -> PlayerTaskUtil.addTaskTagToTaskListTag(task, listTag));
        tag.put(PlayerConstants.TAG_TASKS, listTag);
    }

    public void loadNBTData(CompoundTag tag) {
        ListTag listTag = tag.getList(PlayerConstants.TAG_TASKS, ListTag.TAG_COMPOUND);
        PlayerTaskUtil.addTaskToTaskList(listTag, tasks);
    }

    public void swap(int i1, int i2) {
        Collections.swap(tasks, i1, i2);
    }

    public void sortBy(Comparator<Task> comparator) {
        tasks.sort(comparator);
    }

    public ArrayList<Task> filter(Predicate<Task> predicate) {
        return tasks.stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }
}
