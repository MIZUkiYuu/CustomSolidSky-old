package me.mizukiyuu.customsolidsky.render.gui;

import com.google.common.collect.Lists;
import me.mizukiyuu.customsolidsky.CustomSolidSky;
import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.ColorPreset;
import me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker.BrightnessAndSaturationMapWidget;
import me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker.HueBarWidget;
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
    private static final ArrayList<Color> HUE_BAR_COLORS = Lists.newArrayList(ColorPreset.RED.color, ColorPreset.MAGENTA.color, ColorPreset.BLUE.color, ColorPreset.CYAN.color, ColorPreset.GREEN.color, ColorPreset.YELLOW.color, ColorPreset.RED.color);

    // widget
    HueBarWidget hueBar;
    BrightnessAndSaturationMapWidget BSMap;
    ButtonWidget colorStringButton;

    public CustomSolidSkyOptionsScreen() {
        super(SCREEN_TITLE);
    }

    @Override
    protected void init() {
        hueBar = new HueBarWidget(this.width / 2 - 120, this.height - MARGIN_BOTTOM - 25, 240, 2, CustomSolidSky.SKY_OPTIONS.hueBarHandlePosValue)
                .setSliderWithBorder(HUE_BAR_COLORS, 2, 3, ColorPreset.WHITE.color, 0.8f)
                .setHollowHandle(1, 3, ColorPreset.WHITE.color, 1.0f)
                .initial();

        // The base coordinate is located at the right angle of the triangle arrow.
        BSMap = new BrightnessAndSaturationMapWidget(hueBar.getHandlePosX(), hueBar.y - hueBar.getFullHeight() / 2 - 2, hueBar.getSelectedColor(), ColorPreset.WHITE.color, 1.0f, CustomSolidSky.SKY_OPTIONS.BSMapHandlePosValue)
                .setMiniDisplayPlaneWithBorder(15, 15, 3, 2, 3)
                .setTriangularArrow(2, 2)
                .setBSMapWithBorder(80, 100, 3, 2, 3)
                .initial();

        colorStringButton = new ButtonWidget(this.width / 2 - 40, this.height - MARGIN_BOTTOM - 20, 80, 20, setButtonMessage(), b -> {
            CustomSolidSky.SKY_OPTIONS.setEnable(!CustomSolidSky.SKY_OPTIONS.enable);
        });

        this.addButton(hueBar);
        this.addButton(BSMap);
        this.addButton(colorStringButton);
    }

    @Override
    public void tick() {
        CustomSolidSky.SKY_OPTIONS.skyColor.setColor(hueBar.getSelectedColor());
        colorStringButton.setMessage(setButtonMessage());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        BSMap.setPosX(hueBar.getHandlePosX());
        BSMap.setColor(hueBar.getSelectedColor());
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.client != null) {
            CustomSolidSky.SKY_OPTIONS.skyColor = hueBar.getSelectedColor();
            CustomSolidSky.SKY_OPTIONS.hueBarHandlePosValue = hueBar.getValue();
            CustomSolidSky.SKY_OPTIONS.hideHudAndPlayer = false;
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
