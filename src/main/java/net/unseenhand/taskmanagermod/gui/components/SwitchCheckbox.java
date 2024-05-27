package net.unseenhand.taskmanagermod.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.unseenhand.taskmanagermod.gui.components.checkbox.CheckBoxSprites;
import net.unseenhand.taskmanagermod.gui.components.checkbox.CheckboxConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SwitchCheckbox extends AbstractCheckbox implements WidgetConnectable<SwitchCheckbox> {
    protected final Tooltip tooltip = Tooltip.create(Component.literal(this.selected() ? "On" : "Off"));
    protected final List<SwitchCheckbox> connectedCheckboxes;

    protected SwitchCheckbox(int x,
                             int y,
                             Component msg,
                             Font font,
                             boolean selected,
                             SwitchCheckbox.OnValueChange onChange) {
        super(x, y, getWidth(msg, font), getBoxSize(font), msg, onChange);
        this.selected = selected;
        this.connectedCheckboxes = new ArrayList<>();
    }

    @Override
    public void onPress() {
        this.selected = !this.selected;
        this.onValueChange.onValueChange(this, this.selected);

        if (this.selected()) {
            for (SwitchCheckbox checkbox : connectedCheckboxes) {
                checkbox.setSelected(!selected);
                checkbox.setTooltip(tooltip);
            }
        }
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOut) {
        narrationElementOut.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            narrationElementOut.add(NarratedElementType.USAGE,
                    Component.translatable(this.isFocused()
                            ? "narration.checkbox.usage.focused"
                            : "narration.checkbox.usage.hovered"));
        }
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        Font font = mc.font;
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        int i = getBoxSize(font);
        int j = this.getX() + i + CheckboxConstants.SPACING;
        int k = this.getY() + (this.height >> 1) - (9 >> 1);
        guiGraphics.blitSprite(getSprite(), this.getX(), this.getY(), i, i);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.drawString(font, this.getMessage(), j, k, getARGB32Color(this.alpha));
    }

    @Override
    public boolean selected() {
        return this.selected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    @Override
    public void connectWidget(SwitchCheckbox checkbox) {
        if (!connectedCheckboxes.contains(checkbox)) {
            connectedCheckboxes.add(checkbox);
            checkbox.connectWidget(this); // Ensure bidirectional connection
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull Builder builder(Component msg, Font font) {
        return new Builder(msg, font);
    }

    public static int getBoxSize(Font font) {
        return 9 + CheckboxConstants.BOX_PADDING;
    }

    private @NotNull ResourceLocation getSprite() {
        return this.selected ?
                (this.isFocused() ? CheckBoxSprites.CB_SL_H_S : CheckBoxSprites.CB_SL_S) :
                (this.isFocused() ? CheckBoxSprites.CB_H_S : CheckBoxSprites.CB_S);
    }

    private static int getWidth(Component msg, Font font) {
        return getBoxSize(font) + CheckboxConstants.SPACING + font.width(msg);
    }

    private int getARGB32Color(float alpha) {
        return FastColor.ARGB32.color(FastColor.as8BitChannel(alpha), CheckboxConstants.TEXT_COLOR);
    }

    public static class Builder extends AbstractCheckbox.BaseBuilder<SwitchCheckbox, Builder> {
        private final Component msg;
        private final Font font;

        protected Builder(Component pMessage, Font pFont) {
            this.msg = pMessage;
            this.font = pFont;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public SwitchCheckbox build() {
            SwitchCheckbox.OnValueChange cb$onchange = getCheckbox$onvaluechange();
            SwitchCheckbox cb = new SwitchCheckbox(this.x, this.y, this.msg, this.font, this.selected, cb$onchange);
            cb.setTooltip(this.tooltip);
            return cb;
        }

        private OnValueChange getCheckbox$onvaluechange() {
            return this.option == null ?
                    this.onValueChange :
                    (checkbox, isSelected) -> {
                        this.option.set(isSelected);
                        this.onValueChange.onValueChange(checkbox, isSelected);
                    };
        }
    }

    public interface OnValueChange {
        OnValueChange NOP = (checkbox, isSelected) -> {
        };

        void onValueChange(SwitchCheckbox checkbox, boolean isSelected);
    }
}
