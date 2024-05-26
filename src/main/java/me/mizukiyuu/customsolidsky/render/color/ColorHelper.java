package me.mizukiyuu.customsolidsky.render.color;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class ColorHelper {
    public static Vec3i subtractToVec3i(Color c1, Color c2) {
        return new Vec3i(c1.getRed() - c2.getRed(), c1.getGreen() - c2.getGreen(), c1.getBlue() - c2.getBlue());
    }

    public static Vec3d divideToVec3d(Color c, int i) {
        return new Vec3d((double) c.getRed() / i, (double) c.getGreen() / i, (double) c.getBlue() / i);
    }

    public static int maxDifference(Color c1, Color c2) {
        return Math.max(Math.max(Math.abs(c1.getRed() - c2.getRed()), Math.abs(c1.getGreen() - c2.getGreen())), Math.abs(c1.getBlue() - c2.getBlue()));
    }
}
