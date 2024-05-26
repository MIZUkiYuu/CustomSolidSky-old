package me.mizukiyuu.customsolidsky.render.gui.widget.colorPicker;

import me.mizukiyuu.customsolidsky.CustomSolidSky;
import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.Colors;
import me.mizukiyuu.customsolidsky.render.draw.DrawShape;
import me.mizukiyuu.customsolidsky.util.element.Border;
import me.mizukiyuu.customsolidsky.util.element.Slider2DWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class BrightnessAndSaturationMapWidget extends Slider2DWidget {

    private static final Color[] BS_MAP_COLOR_ARRAY = new Color[]{CustomSolidSky.SKY_OPTIONS.skyColor, Colors.WHITE.color, Colors.BLACK.color, Colors.BLACK.color};
    Color color;

    // mini plane
    float miniPlaneWidth;
    float miniPlaneHeight;
    float miniPlaneRadius;
    Border miniPlaneBorder;

    // triangle
    Vec2f trianglePos;
    float triangleHeight;
    float triangleRadius;

    // bs map
    float bsMapWidth;
    float bsMapHeight;
    float bsMapRadius;
    Border bsMapBorder;

    // handle
    float handleRadius;
    Border handleBorder;

    // value of the hue bar
    float hueBarLeftmostPosX;
    float hueBarRightmostPosX;

    // value
    boolean isExtended;

    //region initialization
    public BrightnessAndSaturationMapWidget(float x, float y, Color color, Vec2f value) {
        super(0, 0, 0, 0, LiteralText.EMPTY, value);
        this.trianglePos = new Vec2f(x, y);
        this.color = color;
    }

    public BrightnessAndSaturationMapWidget setMiniPlaneWithBorder(float width, float height, float radius, Border border) {
        this.miniPlaneWidth = width;
        this.miniPlaneHeight = height;
        this.miniPlaneRadius = radius;
        this.miniPlaneBorder = border;
        return this;
    }

    public BrightnessAndSaturationMapWidget setTriangularArrow(float height, float radius) {
        this.triangleHeight = height;
        this.triangleRadius = radius;
        return this;
    }

    public BrightnessAndSaturationMapWidget setBSMapWithBorder(float width, float height, float radius, Border border) {
        this.bsMapWidth = width;
        this.bsMapHeight = height;
        this.bsMapRadius = radius;
        this.bsMapBorder = border;
        return this;
    }

    public BrightnessAndSaturationMapWidget setHollowHandle(float radius, Border border) {
        this.handleRadius = radius;
        this.handleBorder = border;
        return this;
    }

    public BrightnessAndSaturationMapWidget getValueFromHueBar(float hueBarPosX, float hueBarRightmostPosX) {
        this.hueBarLeftmostPosX = hueBarPosX;
        this.hueBarRightmostPosX = hueBarRightmostPosX;
        return this;
    }

    public BrightnessAndSaturationMapWidget init() {
        this.isExtended = CustomSolidSky.SKY_OPTIONS.isBSMapExtended;
        this.width = isExtended ? bsMapWidth : miniPlaneWidth;
        this.height = isExtended ? bsMapHeight : miniPlaneHeight;
        setPos();
        setHandlePos(CustomSolidSky.SKY_OPTIONS.BSMapHandlePosValue.x * this.width, CustomSolidSky.SKY_OPTIONS.BSMapHandlePosValue.y * this.height);
        return this;
    }
    //endregion

    //region setter and getter
    private void setPos() {
        // Constrains the movement range in the x direction
        this.x = MathHelper.clamp(trianglePos.x - this.width / 2f, hueBarLeftmostPosX, hueBarRightmostPosX - this.width);
        this.y = trianglePos.y - triangleHeight - this.height - (isExtended ? bsMapBorder.down : miniPlaneBorder.down);
    }

    public void setTrianglePosX(float x) {
        this.trianglePos = new Vec2f(x, trianglePos.y);
        setPos();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getMiniPlaneWidthWithBorder() {
        return miniPlaneWidth + miniPlaneBorder.getHorizontal();
    }

    public float getMiniPlaneHeightWithBorder() {
        return miniPlaneHeight + miniPlaneBorder.getVertical();
    }

    public float getBSMapWidthWithBorder() {
        return bsMapWidth + bsMapBorder.getHorizontal();
    }

    public float getBSMapHeightWithBorder() {
        return bsMapHeight + bsMapBorder.getVertical();
    }

    public float getBorderRadius() {
        return isExtended ? bsMapBorder.radius : miniPlaneBorder.radius;
    }
    //endregion

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        if (isExtended) {
            // Draw the border of the BS map.
            DrawShape.drawRoundedRectangle(matrices, this.x - bsMapBorder.left, this.y - bsMapBorder.top, getBSMapWidthWithBorder(), getBSMapHeightWithBorder(), bsMapBorder.radius, bsMapBorder.color, bsMapBorder.alpha);

            BS_MAP_COLOR_ARRAY[0] = CustomSolidSky.SKY_OPTIONS.skyColor;

            // Draw the BS Map.
            DrawShape.drawQuadColorGradientRectangle(matrices, this.x, this.y, bsMapWidth, bsMapHeight, BS_MAP_COLOR_ARRAY, 1.0f);

            // Draw handle
            DrawShape.drawAnnulus(matrices, this.handlePos.x + this.x, this.handlePos.y + this.y, handleRadius, handleBorder.value, handleBorder.color, handleBorder.alpha);

            // Draw the triangular arrow under the mini plane.
            DrawShape.drawIsoscelesRightTriangle(matrices, trianglePos.x, trianglePos.y, triangleHeight, triangleRadius, bsMapBorder.color, bsMapBorder.alpha, true);

        } else {
            // Draw the border of the mini Color plane.
            DrawShape.drawRoundedRectangle(matrices, this.x - miniPlaneBorder.left, this.y - miniPlaneBorder.top, getMiniPlaneWidthWithBorder(), getMiniPlaneHeightWithBorder(), miniPlaneBorder.radius, miniPlaneBorder.color, miniPlaneBorder.alpha);

            // Draw the mini plane.
            DrawShape.drawRoundedRectangle(matrices, this.x, this.y, miniPlaneWidth, miniPlaneHeight, miniPlaneRadius, color, 1.0f);

            // Draw the triangular arrow under the mini Color plane.
            DrawShape.drawIsoscelesRightTriangle(matrices, trianglePos.x, trianglePos.y, triangleHeight, triangleRadius, miniPlaneBorder.color, miniPlaneBorder.alpha, true);
        }
    }

    @Override
    public Vec2f getStepValue() {
        return new Vec2f(this.width / 255f, this.height / 255f);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        CustomSolidSky.SKY_OPTIONS.isBSMapExtended = true;
        CustomSolidSky.SKY_OPTIONS.BSMapHandlePosValue = this.value;
        if (!isExtended) {
            init();
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.onClick(mouseX, mouseY);
    }
}
