/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.mixin.client;

import glitchcore.event.EventManager;
import glitchcore.event.client.RenderTooltipEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics
{
    @Shadow
    private ItemStack tooltipStack;

    @Shadow public abstract int guiWidth();

    @Shadow public abstract int guiHeight();

    @Inject(method = "renderTooltipInternal", at=@At("HEAD"))
    private void onRenderTooltipInternal(Font fallbackFont, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci)
    {
        components = new ArrayList<>(components);
        EventManager.fire(new RenderTooltipEvent(this.tooltipStack, (GuiGraphics)(Object)this, x, y, this.guiWidth(), this.guiHeight(), components, fallbackFont, positioner));
    }
}
