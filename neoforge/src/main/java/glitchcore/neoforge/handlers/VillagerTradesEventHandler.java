/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import glitchcore.event.village.WandererTradesEvent;
import net.minecraft.world.entity.npc.VillagerData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesEventHandler
{
    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event)
    {
        var gcEvent = new glitchcore.event.village.WandererTradesEvent();
        EventManager.fire(gcEvent);

        event.getGenericTrades().addAll(gcEvent.getGenericTrades());
        event.getRareTrades().addAll(gcEvent.getRareTrades());
    }

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event)
    {
        for (int level = VillagerData.MIN_VILLAGER_LEVEL; level <= VillagerData.MAX_VILLAGER_LEVEL; level++)
        {
            EventManager.fire(new glitchcore.event.village.VillagerTradesEvent(event.getType(), level, event.getTrades().get(level)));
        }
    }
}
