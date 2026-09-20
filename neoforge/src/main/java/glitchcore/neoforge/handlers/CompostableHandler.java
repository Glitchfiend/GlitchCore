/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class CompostableHandler
{
    private static final Map<Item, ResourceKey<ContextIntProvider>> COMPOSTABLES = new HashMap<>();

    public static void register(Item item, ResourceKey<ContextIntProvider> layers)
    {
        COMPOSTABLES.put(item, layers);
    }

    @SubscribeEvent
    private static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event)
    {
        COMPOSTABLES.forEach((item, layers) -> {
            event.modify(item, (components, context, it) -> components.set(DataComponents.COMPOSTABLE, new Compostable(layers)));
        });
    }
}
