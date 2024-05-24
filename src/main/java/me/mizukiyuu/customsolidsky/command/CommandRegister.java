package me.mizukiyuu.customsolidsky.command;

import me.mizukiyuu.customsolidsky.CustomSolidSky;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static me.mizukiyuu.customsolidsky.command.MutableColorArgumentType.getColor;
import static me.mizukiyuu.customsolidsky.command.MutableColorArgumentType.mutableColorArg;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class CommandRegister {

    private static final String SKY_COLOR = "skyColor";

    private static final String ENABLE = "enable";
    private static final String DISABLE = "disable";

    private static final String SET = "set";
    private static final String COLOR_R_OR_STRING = "color_R_or_string";
    private static final String COLOR_G = "color_G";
    private static final String COLOR_B = "color_B";

    private static final String FOG = "fog";

    private static final String RESET = "reset";

    public static void register() {
        ClientCommandManager.DISPATCHER.register(
                literal(SKY_COLOR)
                        .then(literal(ENABLE)
                                .executes(c -> {
                                    CustomSolidSky.SKY_OPTIONS.setEnable(true);
                                    return 1;
                                })
                        )
                        .then(literal(DISABLE)
                                .executes(c -> {
                                    CustomSolidSky.SKY_OPTIONS.setEnable(false);
                                    return 1;
                                })
                        )
                        .then(literal(SET)
                                .then(argument(COLOR_R_OR_STRING, mutableColorArg(0, 255))
                                        .executes(c -> {
                                            CustomSolidSky.SKY_OPTIONS.skyColor.setColor(getColor(c, COLOR_R_OR_STRING));
                                            return 1;
                                        })
                                        .then(argument(COLOR_G, integer(0, 255))
                                                .executes(c -> {
                                                    CustomSolidSky.SKY_OPTIONS.skyColor
                                                            .setRed(getColor(c, COLOR_R_OR_STRING).getRed())
                                                            .setGreen(getInteger(c, COLOR_G));
                                                    return 1;
                                                })
                                                .then(argument(COLOR_B, integer(0, 255))
                                                        .executes(c -> {
                                                            CustomSolidSky.SKY_OPTIONS.skyColor.setColor(getColor(c, COLOR_R_OR_STRING).getRed(), getInteger(c, COLOR_G), getInteger(c, COLOR_B));
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(literal(FOG)
                                .then(argument(FOG, bool())
                                        .executes(c -> {
                                            CustomSolidSky.SKY_OPTIONS.canRenderFog = getBool(c, FOG);
                                            return 1;
                                        })
                                )
                        )
                        .then(literal(RESET)
                                .executes((c) -> {
                                    CustomSolidSky.SKY_OPTIONS.resetSkyColor();
                                    return 1;
                                })
                        )
        );
    }
}
