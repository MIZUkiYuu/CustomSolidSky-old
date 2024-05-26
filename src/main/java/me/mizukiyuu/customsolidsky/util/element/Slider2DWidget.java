package me.mizukiyuu.customsolidsky.util.element;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

@Environment(value = EnvType.CLIENT)

public abstract class Slider2DWidget extends InteractiveWidget {

    protected Vec2f value;

    protected Vec2f handlePos;

    public Slider2DWidget(float x, float y, float width, float height, Text message, Vec2f value) {
        super(x, y, width, height, message);
        this.value = value;
        setHandlePos(width / 2f, height / 2f);
    }

    /**
     * Must be called during initialization.
     *
     * @param x between 0 and {@link #width}
     * @param y between 0 and {@link #height}
     */
    protected void setHandlePos(float x, float y) {
        this.handlePos = new Vec2f(
                MathHelper.clamp(x, 0, this.width),
                MathHelper.clamp(y, 0, this.height)
        );
    }

    public Vec2f getValue() {
        return this.value;
    }

    @Override
    protected MutableText getNarrationMessage() {
        return new TranslatableText("gui.narrate.slider", this.getMessage());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        super.renderBackground(matrices, client, mouseX, mouseY);
    }

    protected void setValue(double mouseX, double mouseY) {
        setHandlePos((float) (mouseX - this.x), (float) (mouseY - this.y));
        this.value = new Vec2f(handlePos.x / width, handlePos.y / height);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValue(mouseX, mouseY);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValue(mouseX, mouseY);
    }

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        return this.active && this.visible
                && mouseX >= (double) (this.x)
                && mouseY >= (double) (this.y)
                && mouseX < (double) (this.x + this.width)
                && mouseY < (double) (this.y + this.height);
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

    public Vec2f getStepValue() {
        return Vec2f.SOUTH_EAST_UNIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode % 262 > 3) return false;

        double x = this.x + this.handlePos.x;
        double y = this.y + this.handlePos.y;
        // 262: GLFW_KEY_RIGHT
        // 263: GLFW_KEY_LEFT
        // 264: GLFW_KEY_DOWN
        // 265: GLFW_KEY_UP
        switch (keyCode) {
            case 262:
                x += getStepValue().x;
                break;
            case 263:
                x -= getStepValue().x;
                break;
            case 264:
                y += getStepValue().y;
                break;
            case 265:
                y -= getStepValue().y;
                break;
        }
        setValue(x, y);
        return true;
    }
}
