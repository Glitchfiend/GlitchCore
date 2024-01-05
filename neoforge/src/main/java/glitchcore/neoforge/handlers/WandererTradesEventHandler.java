/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.core.GlitchCore;
import glitchcore.event.EventManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

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
