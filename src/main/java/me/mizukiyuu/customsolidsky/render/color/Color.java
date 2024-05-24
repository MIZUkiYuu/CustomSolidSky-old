package me.mizukiyuu.customsolidsky.render.color;

public class Color {

   int red;
   int green;
   int blue;


    public Color(Color color) {
        setColor(color);
    }

    public Color(int r, int g, int b) {
        setColor(r, g, b);
    }

    public Color(String s) {
        setColorInHEX(s);
    }

    public void setColor(Color color){
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public void setColor(int r, int g, int b){
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public void setColorInHEX(String hex){
        int i = Integer.decode(hex);
        setColor((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public Color setRed(int r) {
        this.red = r;
        return this;
    }

    public Color setGreen(int g) {
        this.green = g;
        return this;
    }

    public Color setBlue(int b) {
        this.blue = b;
        return this;
    }

    @Override
    public String toString() {
        return toString(Format.HEX);
    }

    public String toString(Format colorFormat) {
        switch (colorFormat) {
            case INT_R_G_B:
                return String.format("%d %d %d", getRed(), getGreen(), getBlue());
            case HEX:
                return String.format("#%02x%02x%02x", getRed(), getGreen(), getBlue());
            default:
                return toString();
        }
    }

    /**
     * The format to encode Color
     *
     * @see #toString(Format)
     */
    public enum Format {
        INT_R_G_B,     // like: 255 255 255
        HEX     // like: #ffffff
    }
}
