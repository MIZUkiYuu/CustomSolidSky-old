package me.mizukiyuu.customsolidsky;

import me.mizukiyuu.customsolidsky.render.color.Color;
import me.mizukiyuu.customsolidsky.render.color.Colors;
import net.minecraft.util.math.Vec2f;

public class SolidSky {
    public static Color DEFAULT_SKY_COLOR = Colors.GRAY.color;
    public boolean enable = false;
    public Color skyColor = new Color(DEFAULT_SKY_COLOR);

    // value
    public double hueBarHandlePosValue;
    public Vec2f BSMapHandlePosValue = new Vec2f(0.5f, 0.5f);
    public boolean isBSMapExtended;

    // details
    public Boolean canRenderSky = true;
    public Boolean canRenderFog = true;

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
