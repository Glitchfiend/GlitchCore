/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.event.EventManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class TooltipEventHandler
{
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event)
    {
        EventManager.fire(new glitchcore.event.client.ItemTooltipEvent(event.getEntity(), event.getItemStack(), event.getToolTip()));
    }
}
