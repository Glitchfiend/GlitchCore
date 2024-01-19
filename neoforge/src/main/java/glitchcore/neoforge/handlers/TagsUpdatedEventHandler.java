/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TagsUpdatedEventHandler
{
    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event)
    {
        EventManager.fire(new glitchcore.event.TagsUpdatedEvent(event.getRegistryAccess(), switch (event.getUpdateCause()) {
            case SERVER_DATA_LOAD -> glitchcore.event.TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD;
            case CLIENT_PACKET_RECEIVED -> glitchcore.event.TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED;
        }));
    }
}
