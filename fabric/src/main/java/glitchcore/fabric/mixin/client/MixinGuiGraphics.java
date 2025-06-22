/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import glitchcore.core.GlitchCore;
import glitchcore.event.EventManager;
import glitchcore.event.client.RenderTooltipEvent;
import glitchcore.fabric.gui.IExtendedGuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics implements IExtendedGuiGraphics
{
    @Unique
    private ItemStack currentTooltipStack = ItemStack.EMPTY;

    @Shadow public abstract int guiWidth();

    @Shadow public abstract int guiHeight();


    @Inject(method = "renderTooltip", at=@At("HEAD"))
    private void onRenderTooltipInternal(Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, ResourceLocation resourceLocation, CallbackInfo ci)
    {
        EventManager.fire(new RenderTooltipEvent(this.currentTooltipStack, (GuiGraphics)(Object)this, x, y, this.guiWidth(), this.guiHeight(), components, positioner));
    }

    @Override
    public ItemStack getCurrentTooltipStack()
    {
        return this.currentTooltipStack;
    }

    public void setCurrentTooltipStack(ItemStack stack)
    {
        this.currentTooltipStack = stack;
    }
}
