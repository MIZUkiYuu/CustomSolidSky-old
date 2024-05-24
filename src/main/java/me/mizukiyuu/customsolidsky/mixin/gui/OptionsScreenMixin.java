package me.mizukiyuu.customsolidsky.mixin.gui;

import me.mizukiyuu.customsolidsky.CustomSolidSky;
import me.mizukiyuu.customsolidsky.render.gui.CustomSolidSkyOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    @Unique
    private static final Identifier BTN_SKY_COLOR_ICON = new Identifier(CustomSolidSky.MOD_ID, "textures/gui/sky_color_button.png");

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/option/OptionsScreen;addButton(Lnet/minecraft/client/gui/widget/ClickableWidget;)Lnet/minecraft/client/gui/widget/ClickableWidget;",
                    ordinal = 6
            )
    )
    private void addSkyColorOptionButton(CallbackInfo ci){
        this.addButton(new TexturedButtonWidget(this.width / 2 - 180, this.height / 6 + 72 - 6, 20, 20, 0, 0, 20, BTN_SKY_COLOR_ICON, 32, 64, button -> {
            this.client.openScreen(new CustomSolidSkyOptionsScreen());
            CustomSolidSky.SKY_OPTIONS.hideHudAndPlayer = true;
        }));
    }
}
