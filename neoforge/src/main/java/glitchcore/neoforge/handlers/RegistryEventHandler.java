/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

public class RegistryEventHandler
{
    public static void setup(IEventBus modEventBus)
    {
        modEventBus.addListener(RegistryEventHandler::onRegister);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void onRegister(RegisterEvent forgeEvent)
    {
        var registryKey = forgeEvent.getRegistryKey();
        EventManager.fire(new RegistryEvent(registryKey, (location, value) -> forgeEvent.register((ResourceKey<? extends Registry<Object>>)registryKey, location, () -> value)));
    }
}
