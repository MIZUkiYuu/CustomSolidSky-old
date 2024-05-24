package me.mizukiyuu.customsolidsky.render.draw;

import me.mizukiyuu.customsolidsky.render.color.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

import java.util.ArrayList;

/**
 * Specify vertex and color for bufferBuilder of various shapes
 */

public class VertexBuilder {
    public static void rectangle(Matrix4f matrix, BufferBuilder bufferBuilder, int pos_x, int pos_y, int width, int height, Color color, float alpha) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = (int) (alpha * 255.0f);

        bufferBuilder.vertex(matrix, pos_x + width, pos_y, 0).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, pos_x, pos_y, 0).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, pos_x, pos_y + height, 0).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, pos_x + width, pos_y + height, 0).color(r, g, b, a).next();
    }

    public static void rectangleGradient(Matrix4f matrix, BufferBuilder bufferBuilder, int pos_x, int pos_y, int width, int height, ArrayList<Color> colorList, float alpha, Direction.Type type) {
        if (colorList.size() < 2) return;

        int r;
        int g;
        int b;
        int a = (int) (alpha * 255.0f);
        float interval;
        float temp = 0;

        switch (type) {
            case HORIZONTAL:
                interval = (float) width / (colorList.size() - 1);
                for (Color color : colorList) {
                    r = color.getRed();
                    g = color.getGreen();
                    b = color.getBlue();
                    bufferBuilder.vertex(matrix, pos_x + temp, pos_y, 0).color(r, g, b, a).next();
                    bufferBuilder.vertex(matrix, pos_x + temp, pos_y + height, 0).color(r, g, b, a).next();
                    temp += interval;
                }
                break;

            case VERTICAL:
                interval = (float) height / (colorList.size() - 1);
                for (Color color : colorList) {
                    r = color.getRed();
                    g = color.getGreen();
                    b = color.getBlue();
                    bufferBuilder.vertex(matrix, pos_x + width, pos_y + temp, 0).color(r, g, b, a).next();
                    bufferBuilder.vertex(matrix, pos_x, pos_y + temp, 0).color(r, g, b, a).next();
                    temp += interval;
                }
                break;
        }
    }

    public static void roundedRectangle(Matrix4f matrix, BufferBuilder bufferBuilder, int pos_x, int pos_y, int width, int height, int radius, Color color, float alpha) {
        radius = MathHelper.clamp(radius, 0, Math.min(width / 2, height / 2));
        circularSector(matrix, bufferBuilder, pos_x + width - radius, pos_y + radius, radius, 0, 90, color, alpha, false); // upper right corner
        circularSector(matrix, bufferBuilder, pos_x + radius, pos_y + radius, radius, 90, 180, color, alpha, false);   // upper left corner
        circularSector(matrix, bufferBuilder, pos_x + radius, pos_y + height - radius, radius, 180, 270, color, alpha, false); // lower left corner
        circularSector(matrix, bufferBuilder, pos_x + width - radius, pos_y + height - radius, radius, 270, 360, color, alpha, false);  // lower right corner
    }

    public static void circularSector(Matrix4f matrix, BufferBuilder bufferBuilder, int pos_x, int pos_y, int radius, int degree_start, int degree_end, Color color, float alpha) {
        circularSector(matrix, bufferBuilder, pos_x, pos_y, radius, degree_start, degree_end, color, alpha, true);
    }

    public static void circularSector(Matrix4f matrix, BufferBuilder bufferBuilder, int pos_x, int pos_y, int radius, int degree_start, int degree_end, Color color, float alpha, boolean isNeededCenter) {
        if (isNeededCenter && (degree_end - degree_start) % 180 != 0) {
            bufferBuilder.vertex(matrix, pos_x, pos_y, 0).color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255.0f)).next();
        }

        double radians;

        if (degree_start < degree_end) {    // counterclockwise
            for (int i = degree_start; i <= degree_end; i++) {
                radians = Math.toRadians(i);
                bufferBuilder.vertex(matrix, (float) (radius * Math.cos(radians)) + pos_x, -(float) (radius * Math.sin(radians)) + pos_y, 0).color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255.0f)).next();
            }
        } else {    // clockwise
            for (int i = degree_start; i >= degree_end; i--) {
                radians = Math.toRadians(i);
                bufferBuilder.vertex(matrix, (float) (radius * Math.cos(radians)) + pos_x, -(float) (radius * Math.sin(radians)) + pos_y, 0).color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255.0f)).next();
            }
        }
    }

    public static void annulusWithDegree(Matrix4f matrix, BufferBuilder bufferBuilder, int pos_x, int pos_y, int radius_smaller, int width, int degree_start, int degree_end, Color color, float alpha) {
        int radius_larger = MathHelper.clamp(radius_smaller + width, radius_smaller, radius_smaller + width);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = (int) (alpha * 255.0f);
        double radians;

        for (int i = degree_start; i <= degree_end; i++) {
            radians = Math.toRadians(i);
            bufferBuilder.vertex(matrix, (float) (radius_smaller * Math.cos(radians)) + pos_x, -(float) (radius_smaller * Math.sin(radians)) + pos_y, 0).color(r, g, b, a).next();
            bufferBuilder.vertex(matrix, (float) (radius_larger * Math.cos(radians)) + pos_x, -(float) (radius_larger * Math.sin(radians)) + pos_y, 0).color(r, g, b, a).next();
        }
    }

    public static void isoscelesRightTriangle(Matrix4f matrix, BufferBuilder bufferBuilder, int pos_x, int pos_y, int height, int radius, Color color, float alpha, boolean isInverted){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = (int) (alpha * 255.0f);
        double radians;

        if(radius == 0){
            if(isInverted){
                bufferBuilder.vertex(matrix, pos_x - height, pos_y - height, 0).color(r, g, b, a).next();    // left point
                bufferBuilder.vertex(matrix, pos_x, pos_y, 0).color(r, g, b, a).next();     // point of the right angles
                bufferBuilder.vertex(matrix, pos_x + height, pos_y - height, 0).color(r, g, b, a).next();    // right point
            }else {
                bufferBuilder.vertex(matrix, pos_x - height, pos_y - height, 0).color(r, g, b, a).next();    // left point
                bufferBuilder.vertex(matrix, pos_x + height, pos_y - height, 0).color(r, g, b, a).next();    // right point
                bufferBuilder.vertex(matrix, pos_x, pos_y, 0).color(r, g, b, a).next();     // point of the right angles
            }
        }else {

            double pos_y_center_of_circle = pos_y - MathHelper.sqrt(2) * radius;

            if(isInverted){
                bufferBuilder.vertex(matrix, pos_x - height, pos_y - height, 0).color(r, g, b, a).next();    // left point
                for (int i = 225; i <= 315; i++){
                    radians = Math.toRadians(i);
                    bufferBuilder.vertex(matrix, (float) (radius * Math.cos(radians)) + pos_x, (float) (-radius * Math.sin(radians) + pos_y_center_of_circle), 0).color(r, g, b, a).next();  // rounded corner
                }
                bufferBuilder.vertex(matrix, pos_x + height, pos_y - height, 0).color(r, g, b, a).next();    // right point
            }else {
                bufferBuilder.vertex(matrix, pos_x - height, pos_y - height, 0).color(r, g, b, a).next();    // left point
                bufferBuilder.vertex(matrix, pos_x + height, pos_y - height, 0).color(r, g, b, a).next();    // right point

                for (int i = 45; i <= 135; i++){
                    radians = Math.toRadians(i);
                    bufferBuilder.vertex(matrix, (float) (radius * Math.cos(radians)) + pos_x, (float) (-radius * Math.sin(radians) + pos_y_center_of_circle), 0).color(r, g, b, a).next();  // rounded corner
                }
            }
        }


    }
}
