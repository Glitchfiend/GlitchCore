/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge;

import glitchcore.core.GlitchCore;
import glitchcore.event.Event;
import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import glitchcore.neoforge.handlers.RegistryEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = GlitchCore.MOD_ID)
public class GlitchCoreNeoForge
{
    public GlitchCoreNeoForge()
    {
        GlitchCore.init();
    }

    public static void prepareModEventHandlers(IEventBus modEventBus)
    {
        for (Class<? extends Event> eventClass : EventManager.getRequiredEvents())
        {
            if (eventClass.equals(RegistryEvent.class))
                RegistryEventHandler.setup(modEventBus);
        }
    }
}
