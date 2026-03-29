/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import glitchcore.event.EventManager;
import glitchcore.event.client.RenderGuiEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui
{
    @Shadow(remap = false) protected abstract int getAirBubbleYLine(int $$0, int $$1);

    @Unique
    private DeltaTracker deltaTracker;

    @Inject(method="extractRenderState", at=@At(value="HEAD"), remap = false)
    public void onExtractRenderState(GuiGraphicsExtractor p_282884_, DeltaTracker deltaTracker, CallbackInfo ci)
    {
        this.deltaTracker = deltaTracker;
    }

    @Inject(method="extractCameraOverlays", at=@At(value="INVOKE", target="net/minecraft/client/player/LocalPlayer.getTicksFrozen()I"), remap = false)
    private void onExtractCameraOverlays(GuiGraphicsExtractor guiGraphics, DeltaTracker p_348538_, CallbackInfo ci)
    {
        EventManager.fire(new RenderGuiEvent.Pre(RenderGuiEvent.Type.FROSTBITE, (Gui)(Object)this, guiGraphics, this.deltaTracker, guiGraphics.guiWidth(), guiGraphics.guiHeight()));
    }

    @Inject(method="extractPlayerHealth", at=@At(value="INVOKE", target="net/minecraft/client/gui/Gui.getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"), remap = false)
    private void onExtractFoodLevel(GuiGraphicsExtractor guiGraphics, CallbackInfo ci)
    {
        EventManager.fire(new RenderGuiEvent.Pre(RenderGuiEvent.Type.FOOD, (Gui)(Object)this, guiGraphics, this.deltaTracker, guiGraphics.guiWidth(), guiGraphics.guiHeight()));
    }

    @ModifyVariable(method="extractAirBubbles", at=@At(value="HEAD"), ordinal = 1, require = 1, argsOnly = true, remap = false)
    private int onBeginRenderAir(int rightTop, GuiGraphicsExtractor guiGraphics, Player player, int vehicleHearts, int y, int x) {
        int yLine = this.getAirBubbleYLine(vehicleHearts, y);
        var event = new RenderGuiEvent.Pre(RenderGuiEvent.Type.AIR, (Gui) (Object) this, guiGraphics, this.deltaTracker, guiGraphics.guiWidth(), guiGraphics.guiHeight(), yLine);
        EventManager.fire(event);
        return event.getRowTop() - 10;
    }
}
