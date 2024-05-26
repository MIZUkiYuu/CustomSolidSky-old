package me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker;

import me.mizukiyuu.customsolidsky.CustomSolidSky;
import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.ColorHelper;
import me.mizukiyuu.customsolidsky.render.draw.DrawShape;
import me.mizukiyuu.customsolidsky.util.element.Border;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;

public class HueBarWidget extends SliderWidget {

    // slider
    ArrayList<Color> colorList;
    Border sliderBorder;

    // handle
    float handle_radius;
    Border handleBorder;

    // value
    Color selectedColor;
    Vec3d colorStepValue;


    public HueBarWidget(float x, float y, float width, float height, double value) {
        super((int) x, (int) y, (int) width, (int) height, LiteralText.EMPTY, value);
    }

    public HueBarWidget setSliderWithBorder(ArrayList<Color> colorList, Border border) {
        this.colorList = colorList;
        this.sliderBorder = border;
        return this;
    }

    public HueBarWidget setHollowHandle(float radius, Border border) {
        this.handle_radius = radius;
        this.handleBorder = border;
        return this;
    }

    public HueBarWidget init() {
        setSelectedColor();
        return this;
    }

    public float getFullWidth() {
        return this.width + 2 * handle_radius + handleBorder.getHorizontal();
    }

    public float getFullHeight() {
        return this.height + sliderBorder.getVertical();
    }

    public float getLeftmostPosX() {
        return this.x - handle_radius - sliderBorder.left;
    }

    public float getRightmostPosX() {
        return this.x + this.width + handle_radius + sliderBorder.right;
    }

    public float getHandlePosX() {
        return (float) (this.value + this.x);
    }

    public void setSelectedColor() {
        selectedColor = linearPosToColor();
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public double getValue() {
        return this.value;
    }

    private void setValue(double mouseX) {
        this.value = MathHelper.clamp(mouseX - this.x, 0, this.width);
        CustomSolidSky.SKY_OPTIONS.hueBarHandlePosValue = this.value;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        setSelectedColor();

        // slider border
        DrawShape.drawRoundedRectangle(matrices, this.x - sliderBorder.left - 1, this.y - sliderBorder.top, width + sliderBorder.getHorizontal() + 2, height + sliderBorder.getVertical(), sliderBorder.radius, sliderBorder.color, sliderBorder.alpha);

        // slider
        DrawShape.drawGradientRectangle(matrices, this.x, this.y, width, height, colorList, alpha, Direction.Type.HORIZONTAL);

        // Two extra areas on the left and right of the hue bar.
        // 0xffff0000 -> red
        DrawShape.drawRectangle(matrices, this.x - handle_radius, this.y, handle_radius, this.height, colorList.get(0), 1.0f);
        DrawShape.drawRectangle(matrices, this.x + this.width, this.y, handle_radius, this.height, colorList.get(colorList.size() - 1), 1.0f);

        // Draw handle
        DrawShape.drawAnnulus(matrices, getHandlePosX(), this.y + this.height / 2f, handle_radius, handleBorder.value, handleBorder.color, handleBorder.alpha);
    }

    private Color linearPosToColor() {
        int colors = colorList.size() - 1;
        float colorWidth = (float) this.width / colors;
        int index = (int) (this.value / colorWidth);
        double interval = this.value - index * colorWidth;
        Vec3i color;

        if (index == colors) {
            color = ColorHelper.subtractToVec3i(colorList.get(index), colorList.get(index - 1));
        } else {
            color = ColorHelper.subtractToVec3i(colorList.get(index + 1), colorList.get(index));
        }

        colorStepValue = new Vec3d(color.getX() / colorWidth, color.getY() / colorWidth, color.getZ() / colorWidth);

        return new Color(
                colorList.get(index).getRed() + (int) (colorStepValue.x * interval),
                colorList.get(index).getGreen() + (int) (colorStepValue.y * interval),
                colorList.get(index).getBlue() + (int) (colorStepValue.z * interval)
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (this.isValidClickButton(button) && this.clicked(mouseX, mouseY)) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.onClick(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        return this.active && this.visible
                && mouseX >= (double) (this.x - handle_radius - handleBorder.left)
                && mouseY >= (double) (this.y - handleBorder.top)
                && mouseX < (double) (this.x + this.width + handle_radius + handleBorder.right)
                && mouseY < (double) (this.y + this.height + handleBorder.down);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValue(mouseX);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValue(mouseX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // 262: GLFW_KEY_RIGHT
        // 263: GLFW_KEY_LEFT
        boolean isLeft = keyCode == 263;
        if (isLeft || keyCode == 262) {
            step(isLeft);
        }
        return false;
    }

    /**
     * Control the color value to increase or decrease
     *
     * @param isLeft true: move to the left, false: move to the right
     */
    public void step(boolean isLeft) {
        // Set the color value in steps of 1
        double colorValueStepFactor = 1 / Math.max(Math.abs(colorStepValue.x), Math.max(Math.abs(colorStepValue.y), Math.abs(colorStepValue.z)));
        double f = isLeft ? -colorValueStepFactor : colorValueStepFactor;
        double pos = this.value + f;

        if (pos > this.width) {
            this.setValue(this.x);  // When the slider is at the end, press the right arrow key to jump to the beginning.
        } else if (pos < 0) {
            this.setValue(this.x + this.width); // When the slider is at the beginning, press the left arrow key to jump to the end.
        } else {
            this.setValue(this.x + pos);
        }
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
    }

    @Override
    protected void updateMessage() {
    }

    @Override
    protected void applyValue() {
    }
}
