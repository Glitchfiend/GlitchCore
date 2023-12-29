/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.mixin.client;

import glitchcore.event.EventManager;
import glitchcore.event.client.RenderGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExtendedGui.class)
public abstract class MixinExtendedGui extends Gui
{
    @Shadow
    public int rightHeight;

    public MixinExtendedGui(Minecraft p_232355_, ItemRenderer p_232356_)
    {
        super(p_232355_, p_232356_);
    }

    @Unique
    private float partialTicks;

    @Inject(method="render", at=@At(value="HEAD"))
    public void onRender(GuiGraphics guiGraphics, float partialTicks, CallbackInfo ci)
    {
        this.partialTicks = partialTicks;
    }

    @Inject(method="renderFood", at=@At(value="HEAD"), remap = false)
    public void onBeginRenderFood(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci)
    {
        EventManager.fire(new RenderGuiEvent.Pre(RenderGuiEvent.Type.FOOD, this, guiGraphics, this.partialTicks, this.screenWidth, this.screenHeight));
    }

    @Inject(method="renderAir", at=@At(value="HEAD"), remap = false)
    private void onBeginRenderAir(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci)
    {
        int rightTop = height - this.rightHeight;
        var event = new RenderGuiEvent.Pre(RenderGuiEvent.Type.AIR, this, guiGraphics, this.partialTicks, this.screenWidth, this.screenHeight, rightTop);
        EventManager.fire(event);
        this.rightHeight = height - event.getRowTop();
    }
}
