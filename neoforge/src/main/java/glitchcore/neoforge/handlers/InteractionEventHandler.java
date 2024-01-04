/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InteractionEventHandler
{
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        var gcEvent = new glitchcore.event.player.PlayerInteractEvent.UseBlock(event.getEntity(), event.getHand(), event.getHitVec());
        EventManager.fire(gcEvent);

        if (gcEvent.isCancelled())
        {
            event.setCancellationResult(gcEvent.getCancelResult().getResult());
            event.setCanceled(true);
        }
    }
}
