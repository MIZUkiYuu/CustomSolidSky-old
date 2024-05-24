package me.mizukiyuu.customsolidsky.mixin.render;


import com.mojang.blaze3d.systems.RenderSystem;
import me.mizukiyuu.customsolidsky.CustomSolidSky;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    // fog
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/BackgroundRenderer;applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZ)V",
                    ordinal = 1
            )
    )
    private void applyFogRedirect(Camera c, BackgroundRenderer.FogType ft, float f, boolean b) {
        if (CustomSolidSky.SKY_OPTIONS.canRenderFog) {
            BackgroundRenderer.applyFog(c, ft, f, b);
        }
    }

    // sky bg Color
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V"
            )
    )
    private void setSkyBackgroundColor(CallbackInfo ci) {
        if (CustomSolidSky.SKY_OPTIONS.canRenderSky) return;

        RenderSystem.clearColor(
                CustomSolidSky.SKY_OPTIONS.skyColor.getRed() / 255f,
                CustomSolidSky.SKY_OPTIONS.skyColor.getGreen() / 255f,
                CustomSolidSky.SKY_OPTIONS.skyColor.getBlue() / 255f,
                1.0f
        );
    }

    // sky renderer: sky, stars, sun and moon, part of fog
    @Redirect(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/option/GameOptions;viewDistance:I",
                    ordinal = 1
            )
    )
    private int renderSky(GameOptions gp) {
        return CustomSolidSky.SKY_OPTIONS.canRenderSky ? MinecraftClient.getInstance().options.viewDistance : 0;
    }
}
