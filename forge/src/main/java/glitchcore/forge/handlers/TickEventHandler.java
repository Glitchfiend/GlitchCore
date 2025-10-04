/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.event.EventManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickEventHandler
{
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent.Pre event)
    {
        EventManager.fire(new glitchcore.event.TickEvent.Level(glitchcore.event.TickEvent.Phase.START, event.level()));
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent.Post event)
    {
        EventManager.fire(new glitchcore.event.TickEvent.Level(glitchcore.event.TickEvent.Phase.END, event.level()));
    }
}
