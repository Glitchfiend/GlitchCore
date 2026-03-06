/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.core.GlitchCore;
import glitchcore.util.Remapper;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RemapperEventHandler
{
    @SubscribeEvent
    public static void onMissingMapping(MissingMappingsEvent event)
    {
        Remapper.getRemaps(event.getKey()).forEach((old, replacement) -> {
            ((List<MissingMappingsEvent.Mapping>)event.getMappings((ResourceKey) event.getKey(), old.identifier().getNamespace())).forEach(mapping -> {
                if (mapping.getKey().equals(old.identifier()))
                    mapping.remap(event.getRegistry().getValue(replacement.identifier()));
            });
        });
    }
}
