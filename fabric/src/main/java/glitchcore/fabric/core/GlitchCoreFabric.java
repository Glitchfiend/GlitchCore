/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.core;

import glitchcore.core.GlitchCore;
import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import glitchcore.event.TickEvent;
import glitchcore.event.village.WandererTradesEvent;
import glitchcore.fabric.GlitchCoreInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class GlitchCoreFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        // GlitchCore initialization
        GlitchCore.init();

        // Perform initialization for dependants
        FabricLoader.getInstance().getEntrypointContainers("glitchcore", GlitchCoreInitializer.class).forEach(entrypoint -> {
            GlitchCoreInitializer initializer = entrypoint.getEntrypoint();
            initializer.onInitialize();
        });

        // Fire events which must occur during initialization
        postRegisterEvents();

        var wandererTradesEvent = new WandererTradesEvent();
        EventManager.fire(wandererTradesEvent);

        TradeOfferHelper.registerWanderingTraderOffers(1, (list) -> {
            list.addAll(wandererTradesEvent.getGenericTrades());
        });

        TradeOfferHelper.registerWanderingTraderOffers(2, (list) -> {
            list.addAll(wandererTradesEvent.getRareTrades());
        });

        ServerTickEvents.START_WORLD_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.START, level));
        });

        ServerTickEvents.END_WORLD_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.END, level));
        });
    }

    private static void postRegisterEvents()
    {
        // We use LOADERS to ensure objects are registered at the correct time relative to each other
        for (ResourceLocation registryName : BuiltInRegistries.LOADERS.keySet())
        {
            ResourceKey<? extends Registry<?>> registryKey = ResourceKey.createRegistryKey(registryName);
            Registry<?> registry = BuiltInRegistries.REGISTRY.get(registryName);
            EventManager.fire(new RegistryEvent(registryKey, (location, value) -> Registry.register((Registry<? super Object>)registry, location, value)));
        }
    }
}
