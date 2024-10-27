/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class TagsUpdatedEventHandler
{
    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event)
    {
        EventManager.fire(new glitchcore.event.TagsUpdatedEvent(event.getLookupProvider(), switch (event.getUpdateCause()) {
            case SERVER_DATA_LOAD -> glitchcore.event.TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD;
            case CLIENT_PACKET_RECEIVED -> glitchcore.event.TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED;
        }));
    }
}
