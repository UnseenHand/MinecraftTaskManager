package net.unseenhand.taskmanagermod.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.data.TaskListFilter;
import net.unseenhand.taskmanagermod.gui.GuiTranslationKeys;
import net.unseenhand.taskmanagermod.gui.TaskEntry;
import net.unseenhand.taskmanagermod.gui.TaskSelectionList;
import net.unseenhand.taskmanagermod.gui.button.ButtonDefaults;
import net.unseenhand.taskmanagermod.gui.button.FilterButton;
import net.unseenhand.taskmanagermod.gui.components.SwitchCheckbox;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.network.*;
import net.unseenhand.taskmanagermod.util.RSHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskManagerScreen extends Screen {
    public static final ResourceLocation FRAME = RSHelper.mod("textures/gui/screen/frame.png");
    public static final int TASK_SELECTION_LIST_PADDING = 20;
    public static final int T_NAME_TF_X = 10;
    public static final int T_NAME_TF_Y = 60;
    public static final int T_DESCRIPTION_TF_X = T_NAME_TF_X;
    public static final int T_DESCRIPTION_TF_Y = T_NAME_TF_Y + 20;
    public static final int T_STATUS_TF_X = T_NAME_TF_X;
    public static final int T_STATUS_TF_Y = T_DESCRIPTION_TF_Y + 20;
    public static final int T_INPUT_TF_WIDTH = 100;
    public static final int T_INPUT_TF_HEIGHT = 20;
    public static final int ADD_BTN_WIDTH = ButtonDefaults.DEFAULT_BTN_WIDTH;
    public static final int ADD_BTN_HEIGHT = ButtonDefaults.DEFAULT_BTN_HEIGHT;
    public static final int ADD_BTN_X = 10;
    public static final int ADD_BTN_Y = 180;
    private static final int REMOVE_BTN_X = ADD_BTN_X;
    private static final int REMOVE_BTN_Y = ADD_BTN_Y + 40;
    private static final int REMOVE_BTN_WIDTH = ButtonDefaults.DEFAULT_BTN_WIDTH;
    private static final int REMOVE_BTN_HEIGHT = ButtonDefaults.DEFAULT_BTN_HEIGHT;
    public static final int FILTER_BTN_X = 10;
    public static final int FILTER_BTN_Y = 140;
    public static final int FILTER_BTN_WIDTH = 20;
    public static final int FILTER_BTN_HEIGHT = 20;
    public static TaskListFilter filter = TaskListFilter.EMPTY;
    public Level level;
    private Player player;
    private TaskSelectionList taskSelectionList;
    private Task taskToAdd;
    private EditBox searchTextField;
    private EditBox taskNameTextField;
    private EditBox taskDescriptionTextField;
    private EditBox taskStatusTextField;
    private AbstractButton filterButton;
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
    public void mouseMoved(double mouseX, double mouseY) {
        taskSelectionList.mouseMoved(mouseX, mouseY);
        // super.mouseMoved(mouseX, mouseY);
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
        taskSelectionList.refreshList();
        taskSelectionList.onEntryDragged();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // guiGraphics.blit(FRAME, 0, 0,0, 0, width, height, width, height * 2);
    }

    public EditBox getSearchTextField() {
        return searchTextField;
    }

    private void setupWidgets() {
        clearWidgets();

        searchTextField = addRenderableWidget(new EditBox(font,
                10,
                10,
                T_INPUT_TF_WIDTH,
                T_INPUT_TF_HEIGHT,
                Component.literal(I18n.get(GuiTranslationKeys.GUI_EDIT_BOX_SEARCH_TASK))) {
            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                String oldSearchTerm = searchTextField.getValue();
                String newSearchTerm = oldSearchTerm + getNameIfSingleChar(keyCode, scanCode);

                if (InputConstants.KEY_BACKSPACE == keyCode && !oldSearchTerm.isEmpty()) {
                    newSearchTerm = oldSearchTerm.substring(0, oldSearchTerm.length() - 1);
                }

                filter.setSearchTerm(newSearchTerm);

                PacketHandler.sendToServer(new C2SSetFilterOnBtnClickPacket(filter));

                taskSelectionList.updateModCount();

                return super.keyPressed(keyCode, scanCode, modifiers);
            }

            private String getNameIfSingleChar(int keyCode, int scanCode) {
                InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
                Component component = key.getDisplayName();
                String text = component.getString();

                return text.length() == 1 ? text : "";
            }
        });

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

        filterButton = addRenderableWidget(new FilterButton(this,
                FILTER_BTN_X,
                FILTER_BTN_Y,
                FILTER_BTN_WIDTH,
                FILTER_BTN_HEIGHT,
                FilterButton.FILTER_BTN_SPRITES,
                this::onFilterBtnPress));

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
                    TaskSelectionList.LIST_ITEM_HEIGHT
            );
        }

        addRenderableWidget(taskSelectionList); // TODO: adding the task list widget

        TaskManagerMod.LOGGER.info("Screen TAB Order Group: {}", getTabOrderGroup());
    }

    private void onFilterBtnPress(Button b) {
        if (b instanceof FilterButton button && minecraft != null) {
            this.minecraft.setScreen(new PopupScreen(this,
                    Component.translatable("gui.taskManagerScreen.popUpScreen"),
                    level,
                    player));

            button.setPressed(true);
        }
    }

    private void onAddBtnPress(Button onPress) {
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

    public static class PopupScreen extends Screen {
        public static final List<Checkbox> CHECKBOXES = new ArrayList<>();
        private TaskManagerScreen parentScreen;
        private Level level;
        private Player player;
        private int x;
        private int y;
        private AbstractButton closeBtn;
        private SwitchCheckbox sortByNameCheckbox;
        private SwitchCheckbox sortByStatusCheckbox;
        private ExtendedButton submitFilterBtn;

        protected PopupScreen(Component pTitle) {
            super(pTitle);
        }

        public PopupScreen(TaskManagerScreen parentScreen, Component pTitle, Level level, Player player) {
            this(pTitle);
            this.parentScreen = parentScreen;
            this.level = level;
            this.player = player;
            this.x = parentScreen.width / 3;
            this.y = 0;
        }

        @Override
        protected void init() {
            renderWidgets();
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int outlineWidth = width / 3;
            int outlineHeight = height;
            guiGraphics.renderOutline(x, y, outlineWidth, outlineHeight, 0xffff0000);

            super.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        private void renderWidgets() {
            clearWidgets();

            closeBtn = addRenderableWidget(new ExtendedButton(x + 20,
                    y + 10,
                    50,
                    30,
                    Component.literal("Close"),
                    this::onClose));

            sortByNameCheckbox = addRenderableWidget(
                    SwitchCheckbox.builder(Component.literal("Sort By Name"), font)
                            .pos(x + 20, y + 50)
                            .onValueChange(this::OnValueChange)
                            .selected(false)
                            .tooltip(Tooltip.create(Component.literal("Off")))
                            .build());

            sortByStatusCheckbox = addRenderableWidget(
                    SwitchCheckbox.builder(Component.literal("Sort By Status"), font)
                            .pos(x + 20, y + 70)
                            .onValueChange(this::OnValueChange)
                            .selected(false)
                            .tooltip(Tooltip.create(Component.literal("Off")))
                            .build());

            sortByNameCheckbox.connectWidget(sortByStatusCheckbox);

            submitFilterBtn = addRenderableWidget(new ExtendedButton(x + width / 2,
                    y + height / 2 + 5,
                    50,
                    80,
                    Component.literal("Submit\nFilter"),
                    this::onSubmitFilter));
        }

        private void onClose(Button button) {
            if (this.minecraft != null) {
                this.minecraft.setScreen(parentScreen);
            }
        }

        private void OnValueChange(SwitchCheckbox checkbox, boolean b) {
        }

        private void onSubmitFilter(Button button) {
            TaskListFilter filter = new TaskListFilter(sortByNameCheckbox.selected(), sortByStatusCheckbox.selected());
            filter.setSearchTerm("");
            PacketHandler.sendToServer(new C2SSetFilterOnBtnClickPacket(filter));
        }
    }
}
