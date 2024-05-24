package me.mizukiyuu.customsolidsky.render.color;

import net.minecraft.util.math.Vec3d;

public class ColorHelper {
    public static Vec3d subtractToVec3d(Color c1, Color c2) {
        return new Vec3d(c1.getRed() - c2.getRed(), c1.getGreen() - c2.getGreen(), c1.getBlue() - c2.getBlue());
    }
}
