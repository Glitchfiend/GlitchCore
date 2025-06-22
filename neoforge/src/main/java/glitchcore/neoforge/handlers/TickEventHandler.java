/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import glitchcore.event.TickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber
public class TickEventHandler
{
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Pre event)
    {
        EventManager.fire(new glitchcore.event.TickEvent.Level(TickEvent.Phase.START, event.getLevel()));
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event)
    {
        EventManager.fire(new glitchcore.event.TickEvent.Level(TickEvent.Phase.END, event.getLevel()));
    }
}
