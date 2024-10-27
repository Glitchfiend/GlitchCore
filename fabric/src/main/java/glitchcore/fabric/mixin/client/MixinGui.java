/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import glitchcore.event.EventManager;
import glitchcore.event.client.RenderGuiEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui
{
    @Unique
    private DeltaTracker deltaTracker;

    @Inject(method="render", at=@At(value="HEAD"))
    public void onRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci)
    {
        this.deltaTracker = deltaTracker;
    }

    @Inject(method="renderCameraOverlays", at=@At(value="INVOKE", target="net/minecraft/client/player/LocalPlayer.getTicksFrozen()I"))
    private void onBeginRenderFrozenOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci)
    {
        EventManager.fire(new RenderGuiEvent.Pre(RenderGuiEvent.Type.FROSTBITE, (Gui)(Object)this, guiGraphics, this.deltaTracker, guiGraphics.guiWidth(), guiGraphics.guiHeight()));
    }

    @Inject(method="renderPlayerHealth", at=@At(value="INVOKE", target="net/minecraft/client/gui/Gui.getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private void onRenderPlayerHealth(GuiGraphics guiGraphics, CallbackInfo ci)
    {
        EventManager.fire(new RenderGuiEvent.Pre(RenderGuiEvent.Type.FOOD, (Gui)(Object)this, guiGraphics, this.deltaTracker, guiGraphics.guiWidth(), guiGraphics.guiHeight()));
    }

    // TODO: Will require testing against Tough As Nails
//    @ModifyVariable(method="renderAirBubbles", at=@At(value="INVOKE", target="net/minecraft/world/entity/player/Player.getMaxAirSupply()I"), ordinal = 4, require = 1)
//    private int onBeginRenderAir(int rightTop, GuiGraphics guiGraphics, Player player, int i, int j, int k)
//    {
//        var event = new RenderGuiEvent.Pre(RenderGuiEvent.Type.AIR, (Gui)(Object)this, guiGraphics, this.deltaTracker, guiGraphics.guiWidth(), guiGraphics.guiHeight(), rightTop - 10);
//        EventManager.fire(event);
//        return event.getRowTop() + 10;
//    }
}
