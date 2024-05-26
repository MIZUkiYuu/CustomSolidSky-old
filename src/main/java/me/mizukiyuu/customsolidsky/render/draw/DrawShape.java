package me.mizukiyuu.customsolidsky.render.draw;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.mizukiyuu.customsolidsky.render.color.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class DrawShape extends VertexBuilder {

    /**
     * @param pos_x x coordinate of the upper left corner of the rectangle.
     * @param pos_y y coordinate of the upper left corner of the rectangle.
     * @param alpha a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawRectangle(MatrixStack matrices, float pos_x, float pos_y, float width, float height, Color color, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        rectangle(matrices.peek().getModel(), bufferBuilder, pos_x, pos_y, width, height, color, alpha);
        draw(tessellator);
    }

    /**
     * @param pos_x x coordinate of the upper left corner of the rectangle.
     * @param pos_y y coordinate of the upper left corner of the rectangle.
     * @param alpha a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawGradientRectangle(MatrixStack matrices, float pos_x, float pos_y, float width, float height, ArrayList<Color> colorList, float alpha, Direction.Type type) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        gradientRectangle(matrices.peek().getModel(), bufferBuilder, pos_x, pos_y, width, height, colorList, alpha, type);
        draw(tessellator);
    }

    /**
     * @param pos_x      x coordinate of the upper left corner of the rectangle.
     * @param pos_y      y coordinate of the upper left corner of the rectangle.
     * @param alpha      a float number between 0 and 1. The transparency of this shape.
     * @param colorArray the length of the array must be 4, [0]-> upper right, [1] -> upper left, [2] -> lower left, [3] -> lower right.
     */
    public static void drawQuadColorGradientRectangle(MatrixStack matrices, float pos_x, float pos_y, float width, float height, Color[] colorArray, float alpha) {
        if (colorArray.length != 4) return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        quadColorGradientRectangle(matrices.peek().getModel(), bufferBuilder, pos_x, pos_y, width, height, colorArray, alpha);
        draw(tessellator);
    }

    /**
     * @param pos_x  x coordinate of the upper left corner of the rectangle.
     * @param pos_y  y coordinate of the upper left corner of the rectangle.
     * @param radius corner radius, up to half of the short side.
     * @param alpha  a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawRoundedRectangle(MatrixStack matrices, float pos_x, float pos_y, float width, float height, float radius, Color color, float alpha) {
        Matrix4f matrix = matrices.peek().getModel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_POLYGON, VertexFormats.POSITION_COLOR);
        roundedRectangle(matrix, bufferBuilder, pos_x, pos_y, width, height, radius, color, alpha);
        draw(tessellator);
    }

    /**
     * @param pos_x x coordinate of circle center.
     * @param pos_y y coordinate of circle center.
     * @param alpha a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawCircle(MatrixStack matrices, float pos_x, float pos_y, float radius, Color color, float alpha) {
        drawCircleSector(matrices, pos_x, pos_y, radius, 0, 360, color, alpha);
    }

    /**
     * @param pos_x        x coordinate of circle center.
     * @param pos_y        y coordinate of circle center.
     * @param degree_start when {@code degree_start} >= {@code degree_end}, draw clockwise.
     * @param degree_end   when {@code degree_end} >= {@code degree_start}, draw counterclockwise.
     * @param alpha        a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawCircleSector(MatrixStack matrices, float pos_x, float pos_y, float radius, int degree_start, int degree_end, Color color, float alpha) {
        Matrix4f matrix = matrices.peek().getModel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_POLYGON, VertexFormats.POSITION_COLOR);
        circularSector(matrix, bufferBuilder, pos_x, pos_y, radius, degree_start, degree_end, color, alpha);
        draw(tessellator, GL11.GL_POLYGON_SMOOTH);
    }

    /**
     * @param pos_x x coordinate of circle center.
     * @param pos_y y coordinate of circle center.
     * @param alpha a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawAnnulus(MatrixStack matrices, float pos_x, float pos_y, float radius_smaller, float width, Color color, float alpha) {
        drawAnnulus(matrices, pos_x, pos_y, radius_smaller, width, 0, 360, color, alpha);
    }

    /**
     * @param pos_x        x coordinate of circle center.
     * @param pos_y        y coordinate of circle center.
     * @param degree_start when {@code degree_start} >= {@code degree_end}, draw clockwise.
     * @param degree_end   when {@code degree_end} >= {@code degree_start}, draw counterclockwise.
     * @param alpha        a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawAnnulus(MatrixStack matrices, float pos_x, float pos_y, float radius_smaller, float width, int degree_start, int degree_end, Color color, float alpha) {
        Matrix4f matrix = matrices.peek().getModel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUAD_STRIP, VertexFormats.POSITION_COLOR);
        annulusWithDegree(matrix, bufferBuilder, pos_x, pos_y, radius_smaller, width, degree_start, degree_end, color, alpha);
        draw(tessellator);
    }

    /**
     * @param pos_x  x coordinate of the right angles.
     * @param pos_y  y coordinate of the right angles.
     * @param radius radius of right angle of circle.
     * @param alpha  a float number between 0 and 1. The transparency of this shape.
     */
    public static void drawIsoscelesRightTriangle(MatrixStack matrices, float pos_x, float pos_y, float height, float radius, Color color, float alpha, boolean isInverted) {
        Matrix4f matrix = matrices.peek().getModel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_POLYGON, VertexFormats.POSITION_COLOR);
        isoscelesRightTriangle(matrix, bufferBuilder, pos_x, pos_y, height, radius, color, alpha, isInverted);
        draw(tessellator);
    }

    private static void draw(Tessellator tessellator) {
        draw(tessellator, 0);
    }

    private static void draw(Tessellator tessellator, int smooth_type) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA_SATURATE, GlStateManager.DstFactor.ONE);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        enableSmooth(smooth_type);
        tessellator.draw();
        disableSmooth(smooth_type);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.enableTexture();
    }

    private static void enableSmooth(int smooth_type) {
        if (smooth_type == 0) return;
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        GL11.glEnable(smooth_type);
    }

    private static void disableSmooth(int smooth_type) {
        if (smooth_type == 0) return;
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        GL11.glDisable(smooth_type);
    }
}
