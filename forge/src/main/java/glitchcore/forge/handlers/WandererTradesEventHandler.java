/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.core.GlitchCore;
import glitchcore.event.EventManager;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WandererTradesEventHandler
{
    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event)
    {
        var gcEvent = new glitchcore.event.village.WandererTradesEvent();
        EventManager.fire(gcEvent);

        event.getGenericTrades().addAll(gcEvent.getGenericTrades());
        event.getRareTrades().addAll(gcEvent.getRareTrades());
    }
}
