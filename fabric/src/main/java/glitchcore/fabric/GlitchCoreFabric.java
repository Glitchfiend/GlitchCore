/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric;

import glitchcore.core.GlitchCore;
import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import glitchcore.event.client.ItemTooltipEvent;
import glitchcore.event.client.LevelRenderEvent;
import glitchcore.event.client.RegisterColorsEvent;
import glitchcore.event.player.PlayerInteractEvent;
import glitchcore.event.village.WandererTradesEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;

import java.util.Comparator;

public class GlitchCoreFabric implements ModInitializer, ClientModInitializer
{
    @Override
    public void onInitialize()
    {
        GlitchCore.init();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            var wandererTradesEvent = new WandererTradesEvent();
            EventManager.fire(wandererTradesEvent);

            TradeOfferHelper.registerWanderingTraderOffers(1, (list) -> {
                list.addAll(wandererTradesEvent.getGenericTrades());
            });

            TradeOfferHelper.registerWanderingTraderOffers(2, (list) -> {
                list.addAll(wandererTradesEvent.getRareTrades());
            });
        });
    }

    @Override
    public void onInitializeClient()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            // Fire color registration events
            EventManager.fire(new RegisterColorsEvent.Block(ColorProviderRegistry.BLOCK::register));
            EventManager.fire(new RegisterColorsEvent.Item(ColorProviderRegistry.ITEM::register));
        });

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            EventManager.fire(new ItemTooltipEvent(stack, lines));
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            var event = new PlayerInteractEvent.UseBlock(player, hand, hitResult);
            EventManager.fire(event);

            if (event.isCancelled())
                return event.getCancelResult().getResult();

            return InteractionResult.PASS;
        });

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            EventManager.fire(new LevelRenderEvent(LevelRenderEvent.Stage.AFTER_PARTICLES, context.worldRenderer(), context.matrixStack(), context.projectionMatrix(), context.worldRenderer().ticks, context.tickDelta(), context.camera(), context.frustum()));
        });
    }

    public static void prepareEvents()
    {
        postRegisterEvents();
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
