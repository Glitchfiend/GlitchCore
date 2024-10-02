/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.gui;

import net.minecraft.world.item.ItemStack;

public interface IExtendedGuiGraphics
{
    ItemStack getCurrentTooltipStack();
    void setCurrentTooltipStack(ItemStack stack);
}
