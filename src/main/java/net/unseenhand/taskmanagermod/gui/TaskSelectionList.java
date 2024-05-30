package net.unseenhand.taskmanagermod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.network.C2SSwapEntryOnDraggingPacket;
import net.unseenhand.taskmanagermod.network.PacketHandler;
import net.unseenhand.taskmanagermod.network.S2CRefreshTSLOnTickPacket;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class TaskSelectionList extends ObjectSelectionList<TaskEntry> {
    private static final ResourceLocation MENU_LIST_BACKGROUND = new ResourceLocation("textures/gui/menu_list_background.png");
    private static final ResourceLocation INWORLD_MENU_LIST_BACKGROUND = new ResourceLocation("textures/gui/inworld_menu_list_background.png");
    public static final int LIST_ITEM_HEIGHT = 50;
    private final TaskManagerScreen screen;
    private static List<Task> tasks;
    private static int oSize;
    private TaskEntry focusedAtEntry;
    private TaskEntry releasedAtEntry;
    private int oldModCount;
    private int newModCount;
    private final List<String> ignoredTaskIds = new ArrayList<>();
    public Predicate<Task> filter = (t) -> !Objects.equals(t.id(), t.id() == null ? "" : t.id());
    private int dataSize;
    private boolean releasedAtTheEnd;

    public TaskSelectionList(TaskManagerScreen screen,
                             Minecraft mcClient,
                             int width,
                             int height,
                             int y,
                             int itemHeight) {
        super(mcClient, width, height, y, itemHeight);
        this.screen = screen;

        this.width = getRowWidth();
        this.height = screen.height + getX();

        this.setX(screen.width / 2);

        refreshList();
    }

    @Override
    public int addEntry(@NotNull TaskEntry entry) {
        return super.addEntry(entry);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderListBackground(guiGraphics);

        renderListItems(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderListBackground(@NotNull GuiGraphics guiGraphics) {
        RenderSystem.enableBlend();
        ResourceLocation resourcelocation = this.minecraft.level == null ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
        guiGraphics.blit(
                resourcelocation,
                this.getX(),
                this.getY(),
                (float)this.getRight(),
                (float)(this.getBottom() + (int)this.getScrollAmount()),
                this.getWidth(),
                this.getHeight(),
                32,
                32
        );
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderListItems(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < getItemCount(); ++i) {
            int top = getRowTop(i);
            int bottom = getRowBottom(i);
            if (bottom >= getY() && top <= getBottom()) {
                TaskEntry entry = getEntry(i);

                entry.render(guiGraphics,
                        i,
                        top,
                        getRowLeft(),
                        getRowWidth(),
                        itemHeight,
                        mouseX,
                        mouseY,
                        isMouseOver(mouseX, mouseY) &&
                                Objects.equals(getEntryAtPosition(mouseX, mouseY), entry),
                        partialTicks);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseBtn) {
        // TODO: When pressing beyond the element (clicking) so there is no element
        // TODO: the crush occurs as there is no focused entry
        focusedAtEntry = getEntryAtPosition(mouseX, mouseY);
        if (focusedAtEntry != null) {
            ignoredTaskIds.add(focusedAtEntry.getTask().id()); // Think about a way to ignore it

            dataSize = tasks.size(); // Store the true size of the data

            updateModCount();

            TaskManagerMod.LOGGER.info(String.valueOf(getItemCount()));
        }

        return super.mouseClicked(mouseX, mouseY, mouseBtn);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseBtn) {
        releasedAtEntry = getEntryAtPosition(mouseX, mouseY);

        if (releasedAtEntry == null) {
            if (this.getItemCount() != 0 && inListBoundsOrInRange(mouseX, mouseY)) {
                releasedAtEntry = getLastElement();
                releasedAtTheEnd = true;
            }
        }

        if (focusedAtEntry != null) {
            focusedAtEntry.resetDragCount();
        }

        ignoredTaskIds.clear();

        updateModCount();

        return super.mouseReleased(mouseX, mouseY, mouseBtn);
    }

    @Override
    public boolean mouseDragged(double initMouseX,
                                double initMouseY,
                                int mouseButton,
                                double draggedToX,
                                double draggedToY) {
        if (focusedAtEntry != null) {
            focusedAtEntry.incrementDragCount();
        }

        return super.mouseDragged(initMouseX, initMouseY, mouseButton, draggedToX, draggedToY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (isDragging()) {
            if (focusedAtEntry != null) {
                if (focusedAtEntry.getDragCount() == 0) {
                    removeEntry(focusedAtEntry);
                }
            }
        }

        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void setPosition(int pX, int pY) {
        super.setPosition(pX, pY);
    }

    public void refreshList() {
        if (tasks == null) return;

        if (tasks.size() != oSize || screen.isInitiated() || isModified()) {
            clearEntries();

            PacketHandler.sendToAllClients(new S2CRefreshTSLOnTickPacket(tasks, ignoredTaskIds, filter));

            oSize = tasks.size();
            ignoredTaskIds.clear();
        }

        setModified();
    }

    public void onEntryDragged() {
        if (focusedAtEntry != null && releasedAtEntry != null) {
            if (focusedAtEntry != releasedAtEntry) {
                int otherIndex;

                if (releasedAtTheEnd) {
                    otherIndex = releasedAtEntry.getIndex() + 1;
                    releasedAtTheEnd = false;
                } else {
                    otherIndex = releasedAtEntry.getIndex();
                }

                PacketHandler.sendToServer(new C2SSwapEntryOnDraggingPacket(focusedAtEntry.getIndex(), otherIndex));

                focusedAtEntry = null;
                releasedAtEntry = null;
                updateModCount();
            }
        }
    }

    public static void setTasks(List<Task> taskList) {
        tasks = taskList;
    }

    protected @NotNull TaskEntry getLastElement() {
        return getEntry(getItemCount() - 1);
    }

    public void updateModCount() {
        oldModCount = newModCount;
        newModCount++; // Update count
    }

    private boolean inListBoundsOrInRange(double mouseX, double mouseY) {
        TaskEntry firstElement = getFirstElement();
        int listMinX = firstElement.getX();
        int listMinY = firstElement.getY();

        TaskEntry lastElement = getLastElement();
        int listMaxX = lastElement.getX() + firstElement.getWidth();
        int listMaxY = lastElement.getY() + firstElement.getHeight();

        int approximateMouseX = (int) Mth.clamp(mouseX, Mth.floor(mouseX), Mth.ceil(mouseX));
        int approximateMouseY = (int) Mth.clamp(mouseY, Mth.floor(mouseY), Mth.ceil(mouseY));

        boolean inXBounds = listMinX <= approximateMouseX && approximateMouseX <= listMaxX;
        boolean inYBounds = listMinY <= approximateMouseY && approximateMouseY <= listMaxY;

        boolean inListBounds = inXBounds && inYBounds;

        boolean inRange = listMaxY < approximateMouseY && inXBounds;

        return inListBounds || inRange;
    }

    private void setModified() {
        oldModCount = newModCount;
    }

    private boolean isModified() {
        return oldModCount != newModCount;
    }

    public void setIgnoredAllFrom(List<String> filtered) {
        ignoredTaskIds.addAll(filtered);
    }
}
