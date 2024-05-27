package net.unseenhand.taskmanagermod.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.util.PlayerTaskUtil;
import net.unseenhand.taskmanagermod.util.RSHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.function.Function;

public class TaskEntry extends ObjectSelectionList.Entry<TaskEntry> implements DraggableElement {
    public static final int TASK_NAME_TEXT_COLOR = Color.BLACK.getRGB();
    public static final int TASK_NAME_X = 5;
    public static final int TASK_NAME_Y = 1;
    private static final int TASK_DESCRIPTION_X = TASK_NAME_X;
    public static final int TASK_DESCRIPTION_Y = TASK_NAME_Y + 30;
    public static final int TASK_STATUS_X = TASK_NAME_X + 200;
    public static final int TASK_STATUS_Y = TASK_NAME_Y;
    public static final String FONT_PATH = "font/monainn_regular";
    private static final ResourceLocation FONT = new ResourceLocation(TaskManagerMod.MOD_ID, FONT_PATH);
    private static final Style STYLE = Style.EMPTY.withFont(FONT);
    public static final int BACKGROUND_FILL_COLOR = 0xffff0000;
    public static final int TEXT_COLOR = 0xfcba03;
    private final Minecraft mc;
    private final Task task;
    private TaskSelectionList taskSelectionList;
    private int index;
    private int x;
    private int y;
    private int width;
    private int height;
    private int dragCount;

    private TaskEntry(Task task) {
        this.mc = Minecraft.getInstance();
        this.task = task;
    }

    public TaskEntry(TaskSelectionList taskSelectionList, Task task) {
        this(task);
        this.taskSelectionList = taskSelectionList;
    }

    @Override
    public int getTabOrderGroup() {
        return 1;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics,
                       int index,
                       int top,
                       int left,
                       int width,
                       int height,
                       int mouseX,
                       int mouseY,
                       boolean hovering,
                       float partialTick) {
        this.index = index;
        this.x = left;
        this.y = top;
        this.width = width;
        this.height = height;

        renderBack(guiGraphics, index, top, left, width, height, mouseX, mouseY, hovering, partialTick);

        // new Font(this::genFontSet, true);

        guiGraphics.drawString(mc.font,
                //mcClient.font
                Component.literal(task.name()).withColor(TEXT_COLOR),
                left + TASK_NAME_X,
                top + TASK_NAME_Y,
                TASK_NAME_TEXT_COLOR);

        guiGraphics.drawString(mc.font,
                Component.literal(task.description()).withColor(TextColor.fromRgb(TEXT_COLOR).getValue()),
                left + TASK_DESCRIPTION_X,
                top + TASK_DESCRIPTION_Y,
                TASK_NAME_TEXT_COLOR);

        guiGraphics.drawString(mc.font,
                getFormattedComponent(width / 2 - 6),
                left + width / 2 + 2,
                top + 2,
                TASK_NAME_TEXT_COLOR);

        if (hovering) {
            Component toolTip = Component.literal("Dead").withStyle(getHoveredTooltipTextStyle());
            guiGraphics.renderComponentHoverEffect(mc.font, getHoveredTooltipStyle(toolTip), mouseX, mouseY);
        }

        // RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public @NotNull Component getNarration() {
        return Component.literal("Task #???");
    }

    @Override
    public void renderBack(@NotNull GuiGraphics guiGraphics,
                           int index,
                           int top,
                           int left,
                           int width,
                           int height,
                           int mouseX,
                           int mouseY,
                           boolean isMouseOver,
                           float partialTick) {
        // guiGraphics.fill(left, top, left + width, top + height, BACKGROUND_FILL_COLOR);

        guiGraphics.renderOutline(left, top, width, height, BACKGROUND_FILL_COLOR);

        guiGraphics.fill(left + width / 2,
                top + height / 3,
                left + width,
                top + height / 3 + 1,
                BACKGROUND_FILL_COLOR);

        guiGraphics.fill(left + width / 2,
                top,
                left + width / 2 + 1,
                top + height / 3,
                BACKGROUND_FILL_COLOR);

        // isFocused()
        // guiGraphics.fill(width / 2, height / 3, width, height / 3 + 1, BACKGROUND_FILL_COLOR);
    }

    public Task getTask() {
        return task;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private @NotNull MutableComponent getFormattedComponent(int containerWidth) {
        String text = PlayerTaskUtil.getStatusFromMap(task.status());
        MutableComponent component;

        if (mc.font.width(text) > containerWidth) {
            component = Component.literal(mc.font.plainSubstrByWidth(text+ "...", containerWidth) + "...");
        } else {
            component = Component.literal(text);
        }

        return component.withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    private Style getHoveredTooltipTextStyle() {
        return Style.EMPTY.withItalic(true)
                .withColor(0x28D7D7); // Turquoise Color
    }

    private static @NotNull Style getHoveredTooltipStyle(Component toolTip) {
        return Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, toolTip));
    }

    private Function<ResourceLocation, FontSet> fonts;

    private FontSet genFontSet(ResourceLocation resourceLocation) {
        try (FontSet fontSet =
                     new FontSet(Minecraft.getInstance().textureManager, RSHelper.mod(FONT_PATH))) {
            return fontSet;
        } catch (Exception e) {
            throw new ResourceLocationException(e.getMessage());
        }
    }

    public void incrementDragCount() {
        dragCount++;
    }

    public void resetDragCount() {
        dragCount = 0;
    }

    public int getDragCount() {
        return dragCount;
    }

    public void setDragCount(int dragCount) {
        this.dragCount = dragCount;
    }
}
