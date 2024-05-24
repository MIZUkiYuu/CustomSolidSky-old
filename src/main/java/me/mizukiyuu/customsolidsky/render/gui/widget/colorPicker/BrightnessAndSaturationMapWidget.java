package me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker;

import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.ColorHelper;
import me.mizukiyuu.customsolidsky.render.draw.DrawShape;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BrightnessAndSaturationMapWidget extends SliderWidget {

    Color color;
    Color border_color;
    float border_alpha;

    // mini display plane
    int mini_plane_width;
    int mini_plane_height;
    int mini_plane_radius;
    int mini_plane_border_width;
    int mini_plane_border_radius;

    // triangular arrow
    int triangular_arrow_height;
    int triangular_arrow_radius;

    // bs map
    int bs_map_width;
    int bs_map_height;
    int bs_map_radius;
    int bs_map_border_width;
    int bs_map_border_radius;

    public BrightnessAndSaturationMapWidget(int x, int y, Color color, Color border_color, float border_alpha, double value) {
        super(x, y, 0, 0, LiteralText.EMPTY, value);
        this.color = color;
        this.border_color = border_color;
        this.border_alpha = border_alpha;
    }

    public BrightnessAndSaturationMapWidget setMiniDisplayPlaneWithBorder(int width, int height, int radius, int border_width, int border_radius) {
        this.mini_plane_width = width;
        this.mini_plane_height = height;
        this.mini_plane_radius = radius;
        this.mini_plane_border_width = border_width;
        this.mini_plane_border_radius = border_radius;
        return this;
    }

    public BrightnessAndSaturationMapWidget setTriangularArrow(int height, int radius) {
        this.triangular_arrow_height = height;
        this.triangular_arrow_radius = radius;
        return this;
    }

    public BrightnessAndSaturationMapWidget setBSMapWithBorder(int width, int height, int radius, int border_width, int border_radius) {
        this.bs_map_width = width;
        this.bs_map_height = height;
        this.bs_map_radius = radius;
        this.bs_map_border_width = border_width;
        this.bs_map_border_radius = border_radius;
        return this;
    }

    public BrightnessAndSaturationMapWidget initial(){
        return this;
    }

    public int getFullMiniDisplayPlaneWidth() {
        return mini_plane_width + 2 * mini_plane_border_width;
    }

    public int getFullMiniDisplayPlaneHeight() {
        return triangular_arrow_height + mini_plane_height + 2 * mini_plane_border_width;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Draw the border of the mini display plane.
        DrawShape.drawRoundedRectangle(matrices, this.x - mini_plane_width / 2 - mini_plane_border_width, this.y - getFullMiniDisplayPlaneHeight(), getFullMiniDisplayPlaneWidth(), getFullMiniDisplayPlaneHeight() - triangular_arrow_height, mini_plane_border_radius, border_color, border_alpha);

        // Draw the mini display plane.
        DrawShape.drawRoundedRectangle(matrices, this.x - mini_plane_width / 2, this.y - getFullMiniDisplayPlaneHeight() + mini_plane_border_width, mini_plane_width, mini_plane_height, mini_plane_radius, color, 1.0f);

        // Draw the triangular arrow under the mini display plane.
        DrawShape.drawIsoscelesRightTriangle(matrices, this.x, this.y, triangular_arrow_height, triangular_arrow_radius, border_color, border_alpha, true);
    }

    public void setPosX(int x){
        this.x = x;
    }

    public void setColor(Color color){
        this.color = color;
    }

    @Override
    protected void updateMessage() {}

    @Override
    protected void applyValue() {}
}
