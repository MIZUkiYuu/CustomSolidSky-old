package me.mizukiyuu.customsolidsky.render.gui;

import com.google.common.collect.Lists;
import me.mizukiyuu.customsolidsky.CustomSolidSky;
import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.Colors;
import me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker.BrightnessAndSaturationMapWidget;
import me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker.HueBarWidget;
import me.mizukiyuu.customsolidsky.util.element.Border;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class CustomSolidSkyOptionsScreen extends Screen {

    private static final Text SCREEN_TITLE = new LiteralText("custom solid sky options screen");
    private static final int MARGIN_BOTTOM = 10;

    // hue bar
    private static final ArrayList<Color> HUE_BAR_COLORS = Lists.newArrayList(Colors.RED.color, Colors.MAGENTA.color, Colors.BLUE.color, Colors.CYAN.color, Colors.GREEN.color, Colors.YELLOW.color, Colors.RED.color);

    // widget
    HueBarWidget hueBar;
    BrightnessAndSaturationMapWidget BSMap;
    ButtonWidget colorStringButton;

    public CustomSolidSkyOptionsScreen() {
        super(SCREEN_TITLE);
    }

    @Override
    protected void init() {
        hueBar = new HueBarWidget(this.width / 2f - 120, this.height - MARGIN_BOTTOM - 25, 240, 2, CustomSolidSky.SKY_OPTIONS.hueBarHandlePosValue)
                .setSliderWithBorder(HUE_BAR_COLORS, new Border(2, 3, Colors.WHITE.color, 0.8f))
                .setHollowHandle(1, new Border(3, 0, Colors.WHITE.color, 1.0f))
                .init();

        // The base coordinate is located at the right angle of the triangle arrow.
        BSMap = new BrightnessAndSaturationMapWidget(hueBar.getHandlePosX(), hueBar.y - hueBar.getFullHeight() / 2 - 2, hueBar.getSelectedColor(), CustomSolidSky.SKY_OPTIONS.BSMapHandlePosValue)
                .setMiniPlaneWithBorder(16, 16, 3, new Border(2, 3, Colors.WHITE.color, 1.0f))
                .setTriangularArrow(3, 2)
                .setBSMapWithBorder(60, 60, 3, new Border(2, 3, Colors.WHITE.color, 1.0f))
                .setHollowHandle(1, new Border(1, 0, Colors.WHITE.color, 1.0f))
                .getValueFromHueBar(hueBar.getLeftmostPosX(), hueBar.getRightmostPosX())
                .init();

        colorStringButton = new ButtonWidget(this.width / 2 - 40, this.height - MARGIN_BOTTOM - 20, 80, 20, setButtonMessage(), b -> {
            CustomSolidSky.SKY_OPTIONS.setEnable(!CustomSolidSky.SKY_OPTIONS.enable);
        });

        this.addButton(hueBar);
        this.addChild(BSMap);
        this.addButton(colorStringButton);
    }

    @Override
    public void tick() {
        CustomSolidSky.SKY_OPTIONS.skyColor.setColor(hueBar.getSelectedColor());
        colorStringButton.setMessage(setButtonMessage());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        BSMap.setTrianglePosX(hueBar.getHandlePosX());
        BSMap.setColor(hueBar.getSelectedColor());
        BSMap.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.client != null) {
            CustomSolidSky.SKY_OPTIONS.skyColor = hueBar.getSelectedColor();
            CustomSolidSky.SKY_OPTIONS.hueBarHandlePosValue = hueBar.getValue();
            CustomSolidSky.SKY_OPTIONS.hideHudAndPlayer = false;
            CustomSolidSky.SKY_OPTIONS.BSMapHandlePosValue = BSMap.getValue();
            super.onClose();
        }
    }

//    public void renderBackground(MatrixStack matrices) {
//        if (this.client != null && this.client.world != null) {
//            DrawableHelper.fill(matrices, this.width - BACKGROUND_WIDTH, 0, this.width, this.height, -1072689136);
//        }
//    }

    private Text setButtonMessage() {
        return new LiteralText(hueBar.getSelectedColor().toString(Color.Format.INT_R_G_B));
    }
}
