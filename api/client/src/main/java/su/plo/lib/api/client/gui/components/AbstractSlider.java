package su.plo.lib.api.client.gui.components;

import org.jetbrains.annotations.NotNull;
import su.plo.lib.api.MathLib;
import su.plo.lib.api.client.MinecraftClientLib;
import su.plo.lib.api.client.gui.GuiRender;
import su.plo.lib.api.client.gui.widget.GuiAbstractWidget;

public abstract class AbstractSlider extends GuiAbstractWidget {

    protected double value;
    protected boolean dragging;

    public AbstractSlider(@NotNull MinecraftClientLib minecraft, int x, int y, int width, int height) {
        super(minecraft, x, y, width, height);
    }

    @Override
    protected int getYImage(boolean hovered) {
        return 0;
    }

    @Override
    public void renderButton(@NotNull GuiRender render, int mouseX, int mouseY, float delta) {
        renderBackground(render, mouseX, mouseY);
        renderTrack(render, mouseX, mouseY);
        renderText(render, mouseX, mouseY);
    }

    @Override
    protected void renderBackground(@NotNull GuiRender render, int mouseX, int mouseY) {
        int width = getSliderWidth();

        render.setShaderTexture(0, WIDGETS_LOCATION);
        render.setShaderColor(1F, 1F, 1F, alpha);

        render.enableBlend();
        render.defaultBlendFunc();
        render.enableDepthTest();

        int textureV = getYImage(hovered);

        render.blit(x, y, 0, 46 + textureV * 20, width / 2, height);
        render.blit(x + width / 2, y, 200 - width / 2, 46 + textureV * 20, width / 2, height);
    }

    @Override
    protected boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y &&
                mouseX < x + getSliderWidth() - 1 &&
                mouseY < y + height;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isHovered(mouseX, mouseY)) {
            this.dragging = true;
            setValueFromMouse(mouseX);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean rightPressed = keyCode == 263; // GLFW_KEY_RIGHT
        if (rightPressed || keyCode == 262) { // GLFW_KEY_LEFT
            float delta = rightPressed ? -1.0F : 1.0F;
            setValue(value + (double) (delta / (float) (getSliderWidth() - 8)));
        }

        return false;
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (isHovered(mouseX, mouseY)) {
            setValueFromMouse(mouseX);
            this.dragging = true;
            super.onDrag(mouseX, mouseY, deltaX, deltaY);
        }
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (dragging) {
            this.dragging = false;
            super.playDownSound();
        }
    }

    @Override
    protected void playDownSound() {
    }

    protected void renderTrack(@NotNull GuiRender render, int mouseX, int mouseY) {
        render.setShaderTexture(0, WIDGETS_LOCATION);
        render.setShaderColor(1F, 1F, 1F, 1F);
        int k = (isHoveredOrFocused() ? 2 : 1) * 20;
        render.blit(x + (int) (value * (double) (getSliderWidth() - 8)), y, 0, 46 + k, 4, 20);
        render.blit(x + (int) (value * (double) (getSliderWidth() - 8)) + 4, y, 196, 46 + k, 4, 20);
    }

    protected void renderText(@NotNull GuiRender render, int mouseX, int mouseY) {
        int textColor = active ? COLOR_WHITE : COLOR_GRAY;
        render.drawCenteredString(
                getText(),
                x + getSliderWidth() / 2,
                y + height / 2 - minecraft.getFont().getLineHeight() / 2,
                textColor | ((int) Math.ceil(this.alpha * 255.0F)) << 24
        );
    }

    protected int getSliderWidth() {
        return width;
    }

    private void setValueFromMouse(double mouseX) {
        setValue((mouseX - (double) (x + 4)) / (double) (getSliderWidth() - 8));
    }

    private void setValue(double value) {
        double oldValue = this.value;
        this.value = MathLib.clamp(value, 0.0, 1.0);
        if (oldValue != this.value) applyValue();

        updateText();
    }

    protected abstract void updateText();

    protected abstract void applyValue();
}
