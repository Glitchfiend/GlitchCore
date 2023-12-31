/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@Mod.EventBusSubscriber
public class TooltipEventHandler
{
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event)
    {
        EventManager.fire(new glitchcore.event.client.ItemTooltipEvent(event.getItemStack(), event.getToolTip()));
    }
}
