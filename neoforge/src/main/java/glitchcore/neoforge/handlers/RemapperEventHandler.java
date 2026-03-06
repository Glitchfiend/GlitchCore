/*******************************************************************************
 * Copyright 2026, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.core.GlitchCore;
import glitchcore.util.Remapper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class RemapperEventHandler
{
    @SubscribeEvent
    public static void onRegister(RegisterEvent event)
    {
        Remapper.getRemaps(event.getRegistryKey()).forEach((old, replacement) -> {
            event.getRegistry().addAlias(old.identifier(), replacement.identifier());
        });
    }
}
