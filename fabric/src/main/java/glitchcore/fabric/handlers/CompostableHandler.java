/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.handlers;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

import java.util.HashMap;
import java.util.Map;

public class CompostableHandler
{
    private static final Map<Item, ResourceKey<ContextIntProvider>> COMPOSTABLES = new HashMap<>();

    public static void register(Item item, ResourceKey<ContextIntProvider> layers)
    {
        COMPOSTABLES.put(item, layers);
    }

    public static void init()
    {
        DefaultItemComponentEvents.MODIFY.register(context -> COMPOSTABLES.forEach((item, layers) -> {
            context.modify(item, builder -> builder.set(DataComponents.COMPOSTABLE, new Compostable(layers)));
        }));
    }
}
