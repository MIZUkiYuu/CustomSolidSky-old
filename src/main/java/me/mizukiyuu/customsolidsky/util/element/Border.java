package me.mizukiyuu.customsolidsky.util.element;

import me.mizukiyuu.customsolidsky.render.color.Color;

public class Border {
    public int top;
    public int down;
    public int left;
    public int right;

    /**
     * Default value is {@link #top}, or the value when four sides are equal.
     */
    public int value;

    public int radius;

    public Color color;
    public float alpha;

    int horizontal;
    int vertical;

    public Border(int top, int down, int left, int right, int radius, Color color, float alpha) {
        this.top = top;
        this.down = down;
        this.left = left;
        this.right = right;
        this.value = top;

        this.radius = radius;
        this.color = color;
        this.alpha = alpha;

        this.horizontal = left + right;
        this.vertical = top + down;
    }

    public Border(int i, int radius, Color color, float alpha) {
        this(i, i, i, i, radius, color, alpha);
        this.value = i;
    }

    /**
     * The sum of the values of the horizontal border.
     *
     * @return {@link #left} + {@link #right}
     */
    public int getHorizontal() {
        return horizontal;
    }

    /**
     * The sum of the values of the vertical border.
     *
     * @return {@link #top} + {@link #down}
     */
    public int getVertical() {
        return vertical;
    }
}
