/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import glitchcore.core.GlitchCore;
import glitchcore.event.EventManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
