/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import glitchcore.fabric.gui.IExtendedGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen
{
    @Shadow @Nullable protected Slot hoveredSlot;

    @Inject(method="renderTooltip", at=@At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.renderTooltip (Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V"))
    public void onPreRenderTooltip(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci)
    {
        ((IExtendedGuiGraphics)guiGraphics).setCurrentTooltipStack(this.hoveredSlot.getItem());
    }

    @Inject(method="renderTooltip", at=@At(value = "TAIL"))
    public void onPostRenderTooltip(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci)
    {
        ((IExtendedGuiGraphics)guiGraphics).setCurrentTooltipStack(ItemStack.EMPTY);
    }
}
