package net.unseenhand.taskmanagermod.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class FilterButton extends ImageButton {
    public static final ResourceLocation FILTER_SELECTED =
            new ResourceLocation(TaskManagerMod.MOD_ID, "button/filter_selected");
    public static final ResourceLocation FILTER =
            new ResourceLocation(TaskManagerMod.MOD_ID, "button/filter");
    public static final ResourceLocation FILTER_SELECTED_HIGHLIGHTED =
            new ResourceLocation(TaskManagerMod.MOD_ID, "button/filter_selected_highlighted");
    public static final ResourceLocation FILTER_HIGHLIGHTED =
            new ResourceLocation(TaskManagerMod.MOD_ID, "button/filter_highlighted");

    public static final WidgetSprites FILTER_BTN_SPRITES =
            new WidgetSprites(FILTER_SELECTED, FILTER, FILTER_SELECTED_HIGHLIGHTED, FILTER_HIGHLIGHTED);
    private final TaskManagerScreen screen;
    private final int hoverOffset;
    private boolean isPressed;

    public FilterButton(TaskManagerScreen screen, int pX, int pY, int pWidth, int pHeight, WidgetSprites pSprites,
                        OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pSprites, pOnPress);
        this.hoverOffset = 0;
        this.screen = screen;
    }

    @Override
    public boolean isActive() {
        return super.isActive() && isPressed;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        ResourceLocation location = FILTER_BTN_SPRITES.get(this.isActive(), this.isHoveredOrFocused());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        int i;
        int j;

        if (isPressed) {
            i = 0;
            j = 0xCCCCCC;
        } else if (isHovered) {
            i = 2;
            j = 0xFFFFFF;
        } else {
            i = 1;
            j = 0xFFFFFF;
        }

        int cr = (j >> 16) & 255;
        int cg = (j >> 8) & 255;
        int cb = (j) & 255;
        int ca = (int) alpha;

        int w = width;
        int h = height;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        guiGraphics.blitSprite(location, this.getX() + this.hoverOffset, this.getY(),
                (int) (TaskManagerScreen.FILTER_BTN_WIDTH / 1),
                (int) (TaskManagerScreen.FILTER_BTN_HEIGHT / 1));
        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 100.0F);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, location);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        Matrix4f m = matrixStack.last().pose();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        builder.vertex(m, 1F, h - 1F, 0F).color(cr, cg, cb, ca).uv(0F, 1F).endVertex();
        builder.vertex(m, w - 1F, h - 1F, 0F).color(cr, cg, cb, ca).uv(1F, 1F).endVertex();
        builder.vertex(m, w - 1F, 1F, 0F).color(cr, cg, cb, ca).uv(1F, 0F).endVertex();
        builder.vertex(m, 1F, 1F, 0F).color(cr, cg, cb, ca).uv(0F, 0F).endVertex();
        tessellator.end();
        matrixStack.popPose();
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }
}
