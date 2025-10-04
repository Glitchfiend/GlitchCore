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

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler
{
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow private boolean handledDebugKey;

    @Inject(method = "keyPress", at=@At("TAIL"))
    public void onKeyInput(long window, int action, KeyEvent event, CallbackInfo ci)
    {
        if (window != this.minecraft.getWindow().handle())
            return;

        var gcEvent = new InputEvent.Key(event.key(), event.scancode(), action, event.modifiers(), this.handledDebugKey);
        EventManager.fire(gcEvent);
        this.handledDebugKey = gcEvent.getHandledDebugKey();
    }
}
