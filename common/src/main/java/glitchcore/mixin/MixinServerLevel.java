/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.mixin;

import glitchcore.event.EventManager;
import glitchcore.event.player.PlayerEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class MixinServerLevel
{
    @Inject(method="addPlayer", at=@At(value="HEAD"))
    public void onAddPlayer(ServerPlayer player, CallbackInfo ci)
    {
        EventManager.fire(new PlayerEvent.JoinLevel(player));
    }
}
