package net.unseenhand.taskmanagermod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.TabOrderedElement;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.model.Task;
import net.unseenhand.taskmanagermod.util.PlayerTaskUtil;
import net.unseenhand.taskmanagermod.util.ResourceLocationHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.function.Function;

public class TaskEntry extends ObjectSelectionList.Entry<TaskEntry> implements TabOrderedElement {
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
        return super.getTabOrderGroup();
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
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY);
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

    @Nullable
    @Override
    public ComponentPath nextFocusPath(@NotNull FocusNavigationEvent pEvent) {
        return super.nextFocusPath(pEvent);
    }

    @Nullable
    @Override
    public ComponentPath getCurrentFocusPath() {
        return super.getCurrentFocusPath();
    }

    @Override
    public @NotNull ScreenRectangle getRectangle() {
        return super.getRectangle();
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
                     new FontSet(Minecraft.getInstance().textureManager, ResourceLocationHelper.prefix(FONT_PATH))) {
            return fontSet;
        } catch (Exception e) {
            throw new ResourceLocationException(e.getMessage());
        }
    }

    @Override
    public @NotNull Component getNarration() {
        return Component.literal("Task #???");
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput output) {
        super.updateNarration(output);
    }

    @Override
    public void setFocused(boolean isFocused) {
        super.setFocused(isFocused);
    }

    @Override
    public boolean isFocused() {
        return super.isFocused();
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

    @Override
    public boolean isMouseOver(double p_93537_, double p_93538_) {
        return super.isMouseOver(p_93537_, p_93538_);
    }

    public Task getTask() {
        return task;
    }
}
