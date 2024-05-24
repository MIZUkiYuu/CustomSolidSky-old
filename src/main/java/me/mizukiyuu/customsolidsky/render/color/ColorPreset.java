package me.mizukiyuu.customsolidsky.render.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ColorPreset {

    WHITE(new Color(255, 255, 255)),
    BLACK(new Color(0, 0, 0)),
    GRAY(new Color(64, 64, 64)),

    RED(new Color(255, 0, 0)),
    GREEN(new Color(0, 255, 0)),
    BLUE(new Color(0, 0, 255)),

    YELLOW(new Color(255, 255, 0)),
    MAGENTA(new Color(255, 0, 255)),
    CYAN(new Color(0, 255, 255));

    public static final List<String> STRING_VALUES = new ArrayList<>();

    static {
        Arrays.stream(ColorPreset.values()).map(c -> c.name().toLowerCase()).forEach(STRING_VALUES::add);
    }

    public final Color color;

    ColorPreset(Color color) {
        this.color = color;
    }

}
