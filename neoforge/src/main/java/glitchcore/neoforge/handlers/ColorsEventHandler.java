/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import glitchcore.event.client.RegisterColorsEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorsEventHandler
{
    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event)
    {
        EventManager.fire(new RegisterColorsEvent.Block(event::register));
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event)
    {
        EventManager.fire(new RegisterColorsEvent.Item(event::register));
    }
}
