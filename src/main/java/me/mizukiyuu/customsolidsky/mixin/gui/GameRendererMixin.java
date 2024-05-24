package me.mizukiyuu.customsolidsky.mixin.gui;

import me.mizukiyuu.customsolidsky.CustomSolidSky;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Redirect(
            method = "renderWorld",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z"
            )
    )
    private boolean renderHoldItem(GameRenderer gr){
        return !CustomSolidSky.SKY_OPTIONS.hideHudAndPlayer;
    }
}
