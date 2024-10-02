/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.event.EventManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TickEventHandler
{
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event)
    {
        glitchcore.event.TickEvent.Phase phase = switch (event.phase) {
            case START -> glitchcore.event.TickEvent.Phase.START;
            case END -> glitchcore.event.TickEvent.Phase.END;
        };

        EventManager.fire(new glitchcore.event.TickEvent.Level(phase, event.level));
    }
}
