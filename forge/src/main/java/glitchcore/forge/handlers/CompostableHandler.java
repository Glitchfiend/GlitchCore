/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraftforge.event.GatherComponentsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class CompostableHandler
{
    private static final Map<Item, ResourceKey<ContextIntProvider>> COMPOSTABLES = new HashMap<>();

    public static void register(Item item, ResourceKey<ContextIntProvider> layers)
    {
        COMPOSTABLES.put(item, layers);
    }

    @SubscribeEvent
    public static void onGatherComponents(GatherComponentsEvent.Item event)
    {
        ResourceKey<ContextIntProvider> layers = COMPOSTABLES.get(event.getOwner());

        if (layers != null)
            event.register(DataComponents.COMPOSTABLE, new Compostable(layers));
    }
}
