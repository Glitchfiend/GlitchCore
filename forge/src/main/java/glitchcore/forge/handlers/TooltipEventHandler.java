/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.event.EventManager;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TooltipEventHandler
{
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event)
    {
        EventManager.fire(new glitchcore.event.client.ItemTooltipEvent(event.getItemStack(), event.getToolTip()));
    }
}
