package me.mizukiyuu.customsolidsky;

import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.ColorPreset;

public class SolidSky {
    public boolean enable = false;

    public static Color DEFAULT_SKY_COLOR = ColorPreset.GRAY.color;
    public Color skyColor = new Color(DEFAULT_SKY_COLOR);

    // details
    public Boolean canRenderSky = true;
    public Boolean canRenderFog = true;

    // value
    public double hueBarHandlePosValue;
    public double BSMapHandlePosValue;


    // gui
    public Boolean hideHudAndPlayer = false;

    public void setEnable(boolean b) {
        enable = b;
        canRenderSky = !b;
        canRenderFog = !b;
    }

    public void resetSkyColor() {
        skyColor = new Color(DEFAULT_SKY_COLOR);
    }
}
