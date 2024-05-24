package me.mizukiyuu.customsolidsky;

import me.mizukiyuu.customsolidsky.command.CommandRegister;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomSolidSky implements ClientModInitializer {

    public static final String MOD_ID = "customsolidsky";
    public static final String MOD_NAME = "CustomSolidSky";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final SolidSky SKY_OPTIONS = new SolidSky();

    @Override
    public void onInitializeClient() {
        CommandRegister.register();
    }

    public static Identifier registryResources(String path){
        return new Identifier(MOD_ID, path);
    }
}
