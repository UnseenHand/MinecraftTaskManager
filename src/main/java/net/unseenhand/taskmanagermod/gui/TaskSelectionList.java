package net.unseenhand.taskmanagermod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.network.PacketHandler;
import net.unseenhand.taskmanagermod.network.S2CRefreshTSLOnTickPacket;
import net.unseenhand.taskmanagermod.screen.TaskManagerScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TaskSelectionList extends ObjectSelectionList<TaskEntry> {
    public static final int LIST_ITEM_HEIGHT = 50;
    private final TaskManagerScreen screen;
    private Player player;
    private static List<Task> tasks;
    private static int oSize;

    public TaskSelectionList(TaskManagerScreen screen,
                             Minecraft mcClient,
                             int width,
                             int height,
                             int y,
                             int itemHeight,
                             Player player) {
        super(mcClient, width, height, y, itemHeight);
        this.player = player;
        this.screen = screen;
        this.setX(20);

        refreshList();
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(@NotNull FocusNavigationEvent fnEvent) {
        return super.nextFocusPath(fnEvent);
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput neo) {
        super.updateWidgetNarration(neo);
    }

    @Override
    protected void setRenderHeader(boolean pRenderHeader, int pHeaderHeight) {
        super.setRenderHeader(pRenderHeader, pHeaderHeight);
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth();
    }

    @Nullable
    @Override
    public TaskEntry getSelected() {
        return super.getSelected();
    }

    @Override
    public void setSelected(@Nullable TaskEntry pSelected) {
        super.setSelected(pSelected);
    }

    @Override
    public @NotNull TaskEntry getFirstElement() {
        return super.getFirstElement();
    }

    @Override
    protected void renderListBackground(@NotNull GuiGraphics guiGraphics) {
        super.renderListBackground(guiGraphics);

    }

    @Nullable
    @Override
    public TaskEntry getFocused() {
        return super.getFocused();
    }

    @Override
    protected void clearEntries() {
        super.clearEntries();
    }

    @Override
    protected void replaceEntries(@NotNull Collection<TaskEntry> pEntries) {
        super.replaceEntries(pEntries);
    }

    @Override
    protected @NotNull TaskEntry getEntry(int pIndex) {
        return super.getEntry(pIndex);
    }

    @Override
    public int addEntry(@NotNull TaskEntry pEntry) {
        return super.addEntry(pEntry);
    }

    @Override
    protected void addEntryToTop(@NotNull TaskEntry pEntry) {
        super.addEntryToTop(pEntry);
    }

    @Override
    protected boolean removeEntryFromTop(@NotNull TaskEntry pEntry) {
        return super.removeEntryFromTop(pEntry);
    }

    @Override
    protected int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected boolean isSelectedItem(int pIndex) {
        return super.isSelectedItem(pIndex);
    }

    @Override
    protected int getMaxPosition() {
        return super.getMaxPosition();
    }

    @Override
    protected boolean clickedHeader(int pX, int pY) {
        return super.clickedHeader(pX, pY);
    }

    @Override
    protected void renderHeader(@NotNull GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderHeader(pGuiGraphics, pX, pY);
    }

    @Override
    protected void renderDecorations(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        super.renderDecorations(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderListItems(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void centerScrollOn(@NotNull TaskEntry pEntry) {
        super.centerScrollOn(pEntry);
    }

    @Override
    protected void ensureVisible(@NotNull TaskEntry pEntry) {
        super.ensureVisible(pEntry);
    }

    @Override
    public double getScrollAmount() {
        return super.getScrollAmount();
    }

    @Override
    public void setScrollAmount(double pScroll) {
        super.setScrollAmount(pScroll);
    }

    @Override
    public int getMaxScroll() {
        return super.getMaxScroll();
    }

    @Override
    protected void updateScrollingState(double pMouseX, double pMouseY, int pButton) {
        super.updateScrollingState(pMouseX, pMouseY, pButton);
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition();
    }

    @Override
    protected boolean isValidMouseClick(int pButton) {
        return super.isValidMouseClick(pButton);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY);
    }

    @Override
    public void setFocused(@Nullable GuiEventListener pFocused) {
        super.setFocused(pFocused);
    }

    @Nullable
    @Override
    protected TaskEntry nextEntry(@NotNull ScreenDirection pDirection) {
        return super.nextEntry(pDirection);
    }

    @Nullable
    @Override
    protected TaskEntry nextEntry(@NotNull ScreenDirection pDirection, @NotNull Predicate<TaskEntry> pPredicate) {
        return super.nextEntry(pDirection, pPredicate);
    }

    @Nullable
    @Override
    protected TaskEntry nextEntry(@NotNull ScreenDirection pDirection,
                                  @NotNull Predicate<TaskEntry> pPredicate,
                                  @Nullable TaskEntry pSelected) {
        return super.nextEntry(pDirection, pPredicate, pSelected);
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return super.isMouseOver(pMouseX, pMouseY);
    }

    @Override
    protected void renderListItems(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < getItemCount(); ++i) {
            int top = getRowTop(i);
            int bottom = getRowBottom(i);
            if (bottom >= getY() && top <= getBottom()) {
                TaskEntry entry = getEntry(i);

                if (isSelectedItem(i)) {
                    // If I select do some rendering ...
                }

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
    protected void renderItem(@NotNull GuiGraphics pGuiGraphics,
                              int pMouseX,
                              int pMouseY,
                              float pPartialTick,
                              int pIndex,
                              int pLeft,
                              int pTop,
                              int pWidth,
                              int pHeight) {
        super.renderItem(pGuiGraphics, pMouseX, pMouseY, pPartialTick, pIndex, pLeft, pTop, pWidth, pHeight);
    }

    @Override
    protected void renderSelection(@NotNull GuiGraphics pGuiGraphics,
                                   int pTop,
                                   int pWidth,
                                   int pHeight,
                                   int pOuterColor,
                                   int pInnerColor) {
        super.renderSelection(pGuiGraphics, pTop, pWidth, pHeight, pOuterColor, pInnerColor);
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft();
    }

    @Override
    public int getRowRight() {
        return super.getRowRight();
    }

    @Override
    protected int getRowTop(int pIndex) {
        return super.getRowTop(pIndex);
    }

    @Override
    protected int getRowBottom(int itemIndex) {
        return getRowTop(itemIndex) + itemHeight;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return super.narrationPriority();
    }

    @Nullable
    @Override
    protected TaskEntry remove(int pIndex) {
        return super.remove(pIndex);
    }

    @Override
    protected boolean removeEntry(@NotNull TaskEntry pEntry) {
        return super.removeEntry(pEntry);
    }

    @Nullable
    @Override
    protected TaskEntry getHovered() {
        return super.getHovered();
    }

    @Override
    protected void narrateListElementPosition(@NotNull NarrationElementOutput pNarrationElementOutput,
                                              @NotNull TaskEntry pEntry) {
        super.narrateListElementPosition(pNarrationElementOutput, pEntry);
    }

    @Override
    public boolean isFocused() {
        return super.isFocused();
    }

    @Override
    public void setFocused(boolean flag) {
        super.setFocused(flag);
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public void setTooltip(@Nullable Tooltip pTooltip) {
        super.setTooltip(pTooltip);
    }

    @Nullable
    @Override
    public Tooltip getTooltip() {
        return super.getTooltip();
    }

    @Override
    public void setTooltipDelay(Duration p_334848_) {
        super.setTooltipDelay(p_334848_);
    }

    @Override
    protected @NotNull MutableComponent createNarrationMessage() {
        return super.createNarrationMessage();
    }

    @Override
    protected void renderScrollingString(@NotNull GuiGraphics pGuiGraphics,
                                         @NotNull Font pFont,
                                         int pWidth,
                                         int pColor) {
        super.renderScrollingString(pGuiGraphics, pFont, pWidth, pColor);
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
    }

    @Override
    public void onRelease(double pMouseX, double pMouseY) {
        super.onRelease(pMouseX, pMouseY);
    }

    @Override
    protected void onDrag(double pMouseX, double pMouseY, double pDragX, double pDragY) {
        super.onDrag(pMouseX, pMouseY, pDragX, pDragY);
    }

    @Override
    protected boolean isValidClickButton(int pButton) {
        return super.isValidClickButton(pButton);
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return super.clicked(pMouseX, pMouseY);
    }

    @Override
    public void playDownSound(@NotNull SoundManager pHandler) {
        super.playDownSound(pHandler);
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public void setWidth(int pWidth) {
        super.setWidth(pWidth);
    }

    @Override
    public void setHeight(int pHeight) {
        super.setHeight(pHeight);
    }

    @Override
    public void setAlpha(float pAlpha) {
        super.setAlpha(pAlpha);
    }

    @Override
    public void setMessage(@NotNull Component pMessage) {
        super.setMessage(pMessage);
    }

    @Override
    public @NotNull Component getMessage() {
        return super.getMessage();
    }

    @Override
    public boolean isHovered() {
        return super.isHovered();
    }

    @Override
    public boolean isHoveredOrFocused() {
        return super.isHoveredOrFocused();
    }

    @Override
    public boolean isActive() {
        return super.isActive();
    }

    @Override
    public int getFGColor() {
        return super.getFGColor();
    }

    @Override
    public void setFGColor(int color) {
        super.setFGColor(color);
    }

    @Override
    public void clearFGColor() {
        super.clearFGColor();
    }

    @Override
    protected void defaultButtonNarrationText(@NotNull NarrationElementOutput pNarrationElementOutput) {
        super.defaultButtonNarrationText(pNarrationElementOutput);
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public void setX(int pX) {
        super.setX(pX);
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public void setY(int pY) {
        super.setY(pY);
    }

    @Override
    public int getRight() {
        return super.getRight();
    }

    @Override
    public int getBottom() {
        return super.getBottom();
    }

    @Override
    public void visitWidgets(@NotNull Consumer<AbstractWidget> pConsumer) {
        super.visitWidgets(pConsumer);
    }

    @Override
    public void setSize(int pWidth, int pHeight) {
        super.setSize(pWidth, pHeight);
    }

    @Override
    public @NotNull ScreenRectangle getRectangle() {
        return super.getRectangle();
    }

    @Override
    public void setRectangle(int pWidth, int pHeight, int pX, int pY) {
        super.setRectangle(pWidth, pHeight, pX, pY);
    }

    @Override
    public int getTabOrderGroup() {
        return super.getTabOrderGroup();
    }

    @Override
    public void setTabOrderGroup(int pTabOrderGroup) {
        super.setTabOrderGroup(pTabOrderGroup);
    }

    public void refreshList() {
        if (tasks == null) {
            return;
        }

        if (tasks.size() != oSize ||screen.isInitiated()) {
            clearEntries();

            PacketHandler.sendToAllClients(new S2CRefreshTSLOnTickPacket(tasks));

            oSize = tasks.size();
        }
    }

    public static void setTasks(List<Task> taskList) {
        tasks = taskList;
    }
}
