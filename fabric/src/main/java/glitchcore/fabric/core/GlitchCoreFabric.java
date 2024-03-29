/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.core;

import glitchcore.core.GlitchCore;
import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import glitchcore.event.TagsUpdatedEvent;
import glitchcore.event.TickEvent;
import glitchcore.event.server.RegisterCommandsEvent;
import glitchcore.event.village.VillagerTradesEvent;
import glitchcore.event.village.WandererTradesEvent;
import glitchcore.fabric.GlitchCoreInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;

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

        BuiltInRegistries.VILLAGER_PROFESSION.forEach(profession -> {
            for (int level = VillagerData.MIN_VILLAGER_LEVEL; level <= VillagerData.MAX_VILLAGER_LEVEL; level++)
            {
                final int finalLevel = level;
                TradeOfferHelper.registerVillagerOffers(profession, level, trades -> EventManager.fire(new VillagerTradesEvent(profession, finalLevel, trades)));
            }
        });

        ServerTickEvents.START_WORLD_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.START, level));
        });

        ServerTickEvents.END_WORLD_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.END, level));
        });

        CommandRegistrationCallback.EVENT.register(((dispatcher, context, selection) -> {
            EventManager.fire(new RegisterCommandsEvent(dispatcher, selection, context));
        }));

        CommonLifecycleEvents.TAGS_LOADED.register(((registries, client) -> {
            EventManager.fire(new TagsUpdatedEvent(registries, client ? TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED : TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD));
        }));
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
