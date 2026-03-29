/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import glitchcore.event.EventManager;
import glitchcore.event.client.RenderTooltipEvent;
import glitchcore.fabric.gui.IExtendedGuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphicsExtractor.class)
public abstract class MixinGuiGraphicsExtractor implements IExtendedGuiGraphics
{
    @Unique
    private ItemStack currentTooltipStack = ItemStack.EMPTY;

    @Shadow public abstract int guiWidth();

    @Shadow public abstract int guiHeight();

    @Inject(method = "tooltip", at=@At("HEAD"))
    private void modifyRenderTooltipComponents(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo ci)
    {
        EventManager.fire(new RenderTooltipEvent(this.currentTooltipStack, (GuiGraphicsExtractor) (Object)this, xo, yo, this.guiWidth(), this.guiHeight(), lines, positioner));
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
