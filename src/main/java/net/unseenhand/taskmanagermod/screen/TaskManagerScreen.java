package net.unseenhand.taskmanagermod.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.Music;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.unseenhand.taskmanagermod.gui.GuiTranslationKeys;
import net.unseenhand.taskmanagermod.gui.TaskEntry;
import net.unseenhand.taskmanagermod.gui.TaskSelectionList;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.network.C2SAddTaskToPlayerOnButtonClickPacket;
import net.unseenhand.taskmanagermod.network.C2SRemoveTaskToPlayerOnButtonClickPacket;
import net.unseenhand.taskmanagermod.network.PacketHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskManagerScreen extends Screen {
    public static final int TASK_SELECTION_LIST_PADDING = 20;
    public static final int T_NAME_TF_X = 10;
    public static final int T_NAME_TF_Y = 60;
    public static final int T_DESCRIPTION_TF_X = T_NAME_TF_X;
    public static final int T_DESCRIPTION_TF_Y = T_NAME_TF_Y + 20;
    public static final int T_STATUS_TF_X = T_NAME_TF_X;
    public static final int T_STATUS_TF_Y = T_DESCRIPTION_TF_Y + 20;
    public static final int T_INPUT_TF_WIDTH = 100;
    public static final int T_INPUT_TF_HEIGHT = 20;
    public static final int ADD_BTN_WIDTH = 110;
    public static final int ADD_BTN_HEIGHT = 20;
    public static final int ADD_BTN_X = 10;
    public static final int ADD_BTN_Y = 180;
    private static final int REMOVE_BTN_X = ADD_BTN_X;
    private static final int REMOVE_BTN_Y = ADD_BTN_Y + 40;
    private static final int REMOVE_BTN_WIDTH = ADD_BTN_WIDTH;
    private static final int REMOVE_BTN_HEIGHT = ADD_BTN_HEIGHT;
    public Level level;
    private Player player;
    private TaskSelectionList taskSelectionList;
    private Task taskToAdd;
    private EditBox taskNameTextField;
    private EditBox taskDescriptionTextField;
    private EditBox taskStatusTextField;
    private AbstractButton addTaskButton;
    private AbstractButton removeTaskButton;
    private boolean isInitiated;

    public TaskManagerScreen(Component pTitle) {
        super(pTitle);
    }

    public TaskManagerScreen(Level level, Player player) {
        super(Component.empty());
        this.level = level;
        this.player = player;
        this.isInitiated = true;
    }

    @Override
    public @NotNull Component getTitle() {
        return super.getTitle();
    }

    @Override
    public @NotNull Component getNarrationMessage() {
        return super.getNarrationMessage();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return super.charTyped(pCodePoint, pModifiers);
    }

    @Override
    protected void setInitialFocus(@NotNull GuiEventListener pListener) {
        super.setInitialFocus(pListener);
    }

    @Override
    public void clearFocus() {
        super.clearFocus();
    }

    @Override
    protected void changeFocus(@NotNull ComponentPath pPath) {
        super.changeFocus(pPath);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    protected <T extends GuiEventListener & Renderable & NarratableEntry> @NotNull T addRenderableWidget(@NotNull T widget) {
        return super.addRenderableWidget(widget);
    }

    @Override
    protected <T extends Renderable> @NotNull T addRenderableOnly(@NotNull T pRenderable) {
        return super.addRenderableOnly(pRenderable);
    }

    @Override
    protected <T extends GuiEventListener & NarratableEntry> @NotNull T addWidget(@NotNull T pListener) {
        return super.addWidget(pListener);
    }

    @Override
    protected void removeWidget(@NotNull GuiEventListener pListener) {
        super.removeWidget(pListener);
    }

    @Override
    protected void clearWidgets() {
        super.clearWidgets();
    }

    @Override
    protected void insertText(@NotNull String pText, boolean pOverwrite) {
        super.insertText(pText, pOverwrite);
    }

    @Override
    public boolean handleComponentClicked(@Nullable Style pStyle) {
        return super.handleComponentClicked(pStyle);
    }

    @Override
    protected void rebuildWidgets() {
        super.rebuildWidgets();
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return super.children();
    }

    @Override
    public @NotNull Optional<GuiEventListener> getChildAt(double pMouseX, double pMouseY) {
        return super.getChildAt(pMouseX, pMouseY);
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
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
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return taskSelectionList.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    protected void init() {
        setupWidgets();
    }

    @Override
    public void tick() {
//        removeWidget(selectionList);
//        selectionList = new BiomeSearchList(this, minecraft, width + 110, height - 40, 40, 45);
//        addRenderableWidget(selectionList);
        taskSelectionList.refreshList();
        // taskSelectionList.renderWidget();
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public void added() {
        super.added();
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderTransparentBackground(@NotNull GuiGraphics pGuiGraphics) {
        super.renderTransparentBackground(pGuiGraphics);
    }

    @Override
    protected void renderPanorama(@NotNull GuiGraphics guiGraphics, float flag) {
        super.renderPanorama(guiGraphics, flag);
    }

    @Override
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
    }

    @Override
    public void resize(@NotNull Minecraft pMinecraft, int pWidth, int pHeight) {
        super.resize(pMinecraft, pWidth, pHeight);
    }

    @Override
    protected boolean isValidCharacterForName(@NotNull String pText, char pCharTyped, int pCursorPos) {
        return super.isValidCharacterForName(pText, pCharTyped, pCursorPos);
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return super.isMouseOver(pMouseX, pMouseY);
    }

    @Override
    public void onFilesDrop(@NotNull List<Path> pPacks) {
        super.onFilesDrop(pPacks);
    }

    @Override
    public @NotNull Minecraft getMinecraft() {
        return super.getMinecraft();
    }

    @Override
    public void afterMouseMove() {
        super.afterMouseMove();
    }

    @Override
    public void afterMouseAction() {
        super.afterMouseAction();
    }

    @Override
    public void afterKeyboardAction() {
        super.afterKeyboardAction();
    }

    @Override
    public void handleDelayedNarration() {
        super.handleDelayedNarration();
    }

    @Override
    public void triggerImmediateNarration(boolean pOnlyNarrateNew) {
        super.triggerImmediateNarration(pOnlyNarrateNew);
    }

    @Override
    protected boolean shouldNarrateNavigation() {
        return super.shouldNarrateNavigation();
    }

    @Override
    protected void updateNarrationState(@NotNull NarrationElementOutput narrationElementOutput) {
        super.updateNarrationState(narrationElementOutput);
    }

    @Override
    protected void updateNarratedWidget(@NotNull NarrationElementOutput pNarrationElementOutput) {
        super.updateNarratedWidget(pNarrationElementOutput);
    }

    @Override
    protected @NotNull Component getUsageNarration() {
        return super.getUsageNarration();
    }

    @Override
    public void narrationEnabled() {
        super.narrationEnabled();
    }

    @Override
    public void setTooltipForNextRenderPass(@NotNull List<FormattedCharSequence> pTooltip) {
        super.setTooltipForNextRenderPass(pTooltip);
    }

    @Override
    public void setTooltipForNextRenderPass(@NotNull List<FormattedCharSequence> pTooltip,
                                            @NotNull ClientTooltipPositioner pPositioner,
                                            boolean pOverride) {
        super.setTooltipForNextRenderPass(pTooltip, pPositioner, pOverride);
    }

    @Override
    protected void clearTooltipForNextRenderPass() {
        super.clearTooltipForNextRenderPass();
    }

    @Override
    public void setTooltipForNextRenderPass(@NotNull Component pTooltip) {
        super.setTooltipForNextRenderPass(pTooltip);
    }

    @Override
    public void setTooltipForNextRenderPass(@NotNull Tooltip pTooltip,
                                            @NotNull ClientTooltipPositioner pPositioner,
                                            boolean pOverride) {
        super.setTooltipForNextRenderPass(pTooltip, pPositioner, pOverride);
    }

    @Override
    public @NotNull ScreenRectangle getRectangle() {
        return super.getRectangle();
    }

    @Nullable
    @Override
    public Music getBackgroundMusic() {
        return super.getBackgroundMusic();
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return super.getFocused();
    }

    @Override
    public void setFocused(@Nullable GuiEventListener pListener) {
        super.setFocused(pListener);
    }

    @Override
    public void setFocused(boolean pFocused) {
        super.setFocused(pFocused);
    }

    @Override
    public boolean isFocused() {
        return super.isFocused();
    }

    @Nullable
    @Override
    public ComponentPath getCurrentFocusPath() {
        return super.getCurrentFocusPath();
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(@NotNull FocusNavigationEvent pEvent) {
        return super.nextFocusPath(pEvent);
    }

    @Override
    public int getTabOrderGroup() {
        return super.getTabOrderGroup();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private void setupWidgets() {
        clearWidgets();

        taskNameTextField = addRenderableWidget(new EditBox(font,
                T_NAME_TF_X,
                T_NAME_TF_Y,
                T_INPUT_TF_WIDTH,
                T_INPUT_TF_HEIGHT,
                Component.literal(I18n.get(GuiTranslationKeys.GUI_BUTTON_ADD_TASK))));

        taskDescriptionTextField = addRenderableWidget(new EditBox(font,
                T_DESCRIPTION_TF_X,
                T_DESCRIPTION_TF_Y,
                T_INPUT_TF_WIDTH,
                T_INPUT_TF_HEIGHT,
                Component.literal(I18n.get(GuiTranslationKeys.GUI_BUTTON_ADD_TASK))));

        taskStatusTextField = addRenderableWidget(new EditBox(font,
                T_STATUS_TF_X,
                T_STATUS_TF_Y,
                T_INPUT_TF_WIDTH,
                T_INPUT_TF_HEIGHT,
                Component.literal(I18n.get(GuiTranslationKeys.GUI_BUTTON_ADD_TASK))));

        addTaskButton = addRenderableWidget(new ExtendedButton(ADD_BTN_X,
                ADD_BTN_Y,
                ADD_BTN_WIDTH,
                ADD_BTN_HEIGHT,
                Component.literal(I18n.get(GuiTranslationKeys.GUI_BUTTON_ADD_TASK)),
                this::onAddBtnPress));

        removeTaskButton = addRenderableWidget(new ExtendedButton(REMOVE_BTN_X,
                REMOVE_BTN_Y,
                REMOVE_BTN_WIDTH,
                REMOVE_BTN_HEIGHT,
                Component.literal(I18n.get(GuiTranslationKeys.GUI_BUTTON_REMOVE_TASK)),
                this::onRemoveBtnPress));

        if (taskSelectionList == null) {
            taskSelectionList = new TaskSelectionList(this,
                    minecraft,
                    width,
                    height - TASK_SELECTION_LIST_PADDING * 2,
                    TASK_SELECTION_LIST_PADDING,
                    TaskSelectionList.LIST_ITEM_HEIGHT,
                    player);
        }

        addRenderableWidget(taskSelectionList); // TODO: adding the task list widget
    }

    private void onAddBtnPress(Button onPress) {
        // TODO: Check if the taskToAdd is actually fully create
        String extTaskName = taskNameTextField.getValue();
        String extTaskDescription = taskDescriptionTextField.getValue();
        String extTaskStatus = taskStatusTextField.getValue();

        if (!Task.inputsAreValid(extTaskName, extTaskDescription, extTaskStatus)) {
            return;
        }

        taskToAdd = new Task(generateNewTaskId(), extTaskName, extTaskDescription, Byte.parseByte(extTaskStatus));

        PacketHandler.sendToServer(new C2SAddTaskToPlayerOnButtonClickPacket(taskToAdd));
    }

    private void onRemoveBtnPress(Button onPress) {
        TaskEntry selectedTE = taskSelectionList.getFocused();
        if (selectedTE == null) {
            return;
        }

        Task task = selectedTE.getTask();

        PacketHandler.sendToServer(new C2SRemoveTaskToPlayerOnButtonClickPacket(task.id()));
    }

    private String generateNewTaskId() {
        return String.valueOf(UUID.randomUUID());
    }

    public GuiEventListener getAddTaskButton() {
        return addTaskButton;
    }

    public Player getPlayer() {
        return player;
    }

    public TaskSelectionList getTaskSelectionList() {
        return taskSelectionList;
    }

    public void setTaskSelectionList(TaskSelectionList taskSelectionList) {
        this.taskSelectionList = taskSelectionList;
    }

    public boolean isInitiated() {
        if (isInitiated) {
            isInitiated = false;
            return true;
        }
        return false;
    }
}
