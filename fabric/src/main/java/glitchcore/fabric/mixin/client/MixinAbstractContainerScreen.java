/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import glitchcore.fabric.gui.IExtendedGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen
{
    @Shadow @Nullable protected Slot hoveredSlot;

    @Inject(method="renderTooltip", at=@At(value = "HEAD"))
    public void onPreRenderTooltip(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci)
    {
        ((IExtendedGuiGraphics)guiGraphics).setCurrentTooltipStack(Optional.of(this.hoveredSlot).map(Slot::getItem).orElse(ItemStack.EMPTY));
    }

    @Inject(method="renderTooltip", at=@At(value = "TAIL"))
    public void onPostRenderTooltip(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci)
    {
        ((IExtendedGuiGraphics)guiGraphics).setCurrentTooltipStack(ItemStack.EMPTY);
    }
}
