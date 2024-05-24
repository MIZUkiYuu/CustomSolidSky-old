package me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker;

import me.mizukiyuu.customsolidsky.CustomSolidSky;
import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.ColorHelper;
import me.mizukiyuu.customsolidsky.render.color.ColorPreset;
import me.mizukiyuu.customsolidsky.render.draw.DrawShape;
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

import java.util.ArrayList;

public class HueBarWidget extends SliderWidget {

    // slider
    ArrayList<Color> colorList;
    int slider_border_width;
    int slider_border_radius;
    Color slider_border_color;
    float slider_border_alpha;

    // handle
    int handle_radius;
    int handle_border_width;
    Color handle_border_color;
    float handle_border_alpha;

    // value
    Color selectedColor;
    Vec3d colorStepValue;


    public HueBarWidget(int x, int y, int width, int height, double value) {
        super(x, y, width, height, LiteralText.EMPTY, value);
    }

    public HueBarWidget setSliderWithBorder(ArrayList<Color> colorList, int border_width, int border_radius, Color border_color, float border_alpha){
        this.colorList = colorList;
        this.slider_border_width = border_width;
        this.slider_border_radius = border_radius;
        this.slider_border_color = border_color;
        this.slider_border_alpha = border_alpha;
        return this;
    }

    public HueBarWidget setHollowHandle(int radius, int border_width, Color border_color, float border_alpha){
        this.handle_radius = radius;
        this.handle_border_width = border_width;
        this.handle_border_color = border_color;
        this.handle_border_alpha = border_alpha;
        return this;
    }

    public HueBarWidget initial(){
        setSelectedColor();
        return this;
    }

    public int getFullWidth(){
        return this.width + 2 * (handle_radius + handle_border_width);
    }

    public int getFullHeight(){
        return this.height + 2 * slider_border_width;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        setSelectedColor();

        // slider border
        DrawShape.drawRoundedRectangle(matrices, x - slider_border_width - 1, y - slider_border_width, width + 2 * (slider_border_width + 1), height + 2 * slider_border_width, slider_border_radius, slider_border_color, slider_border_alpha);

        // slider
        DrawShape.drawRectangleGradient(matrices, x, y, width, height, colorList, alpha, Direction.Type.HORIZONTAL);

        // Two extra areas on the left and right of the hue bar.
        // 0xffff0000 -> red
        DrawShape.drawRectangle(matrices, this.x - handle_radius, this.y, handle_radius, this.height, colorList.get(0), 1.0f);
        DrawShape.drawRectangle(matrices, this.x + this.width, this.y, handle_radius, this.height, colorList.get(colorList.size() - 1), 1.0f);

        // Draw handle
        DrawShape.drawAnnulus(matrices, getHandlePosX(), this.y + this.height / 2, handle_radius, handle_border_width, handle_border_color, handle_border_alpha);
    }

    public int getHandlePosX() {
        return (int) this.value + this.x;
    }

    public void setSelectedColor() {
        selectedColor = linearPosToColor();
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    private Color linearPosToColor() {
        int colors = colorList.size() - 1;
        int colorWidth = this.width / colors;
        int index = (int) this.value / colorWidth;
        double interval = this.value - index * colorWidth;
        Vec3d color;

        if (index == colors) {
            color = ColorHelper.subtractToVec3d(colorList.get(index), colorList.get(index - 1));
        } else {
            color = ColorHelper.subtractToVec3d(colorList.get(index + 1), colorList.get(index));
        }

        // add 1 to 255, such as: 0, 0, 255 -> 0, 0, 256
        colorStepValue = new Vec3d(color.getX() / colorWidth, color.getY() / colorWidth,  color.getZ() / colorWidth);

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
                && mouseX >= (double) (this.x - handle_radius - handle_border_width)
                && mouseY >= (double) (this.y - handle_border_width)
                && mouseX < (double) (this.x + this.width + handle_radius + handle_border_width)
                && mouseY < (double) (this.y + this.height + handle_border_width);
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
        // 263: GLFW_KEY_LEFT
        // 262: GLFW_KEY_RIGHT
        boolean bl = keyCode == 263;
        if (bl || keyCode == 262) {
            step(bl);
        }
        return false;
    }

    /**
     * Control the color value to increase or decrease
     *
     * @param bl true: move to the left, false: move to the right
     */
    public void step(boolean bl) {
        // Set the color value in steps of 1
        double colorValueStepFactor = 1 / Math.max(Math.abs(colorStepValue.x), Math.max(Math.abs(colorStepValue.y), Math.abs(colorStepValue.z)));
        double f = bl ? -colorValueStepFactor : colorValueStepFactor;
        double pos = this.value + f;

        if (pos > this.width) {
            this.setValue(this.x);  // When the slider is at the end, press the right arrow key to jump to the beginning.
        } else if (pos < 0) {
            this.setValue(this.x + this.width); // When the slider is at the beginning, press the left arrow key to jump to the end.
        } else {
            this.setValue(this.x + pos);
        }
    }

    private void setValue(double mouseX) {
        this.value = MathHelper.clamp(mouseX - this.x, 0, this.width);
        CustomSolidSky.SKY_OPTIONS.hueBarHandlePosValue = this.value;
    }

    public double getValue() {
        return this.value;
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
