/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.mixin.client;

import glitchcore.event.EventManager;
import glitchcore.event.client.InputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler
{
    @Shadow(remap = false)
    @Final
    private Minecraft minecraft;

    @Inject(method = "handleDebugKeys", at=@At("RETURN"), cancellable = true, remap = false)
    public void onKeyInput(KeyEvent event, CallbackInfoReturnable<Boolean> cir)
    {
        var gcEvent = new InputEvent.Key(event.key(), event.scancode(), event.modifiers(), cir.getReturnValue());
        EventManager.fire(gcEvent);

        if (gcEvent.getHandledDebugKey())
            cir.setReturnValue(true);
    }
}
