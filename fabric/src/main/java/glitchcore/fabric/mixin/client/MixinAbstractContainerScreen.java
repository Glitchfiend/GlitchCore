/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import glitchcore.fabric.gui.IExtendedGuiGraphics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen
{
    @Shadow @Nullable
    protected Slot hoveredSlot;

    @Inject(method="extractTooltip", at=@At(value = "HEAD"))
    public void onPreExtractTooltip(GuiGraphicsExtractor guiGraphics, int i, int j, CallbackInfo ci)
    {
        ((IExtendedGuiGraphics)guiGraphics).setCurrentTooltipStack(Optional.ofNullable(this.hoveredSlot).map(Slot::getItem).orElse(ItemStack.EMPTY));
    }

    @Inject(method="extractTooltip", at=@At(value = "TAIL"))
    public void onPostExtractTooltip(GuiGraphicsExtractor guiGraphics, int i, int j, CallbackInfo ci)
    {
        ((IExtendedGuiGraphics)guiGraphics).setCurrentTooltipStack(ItemStack.EMPTY);
    }
}
