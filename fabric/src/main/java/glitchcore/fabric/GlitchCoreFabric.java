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
import glitchcore.event.client.RegisterParticleSpritesEvent;
import glitchcore.event.player.PlayerInteractEvent;
import glitchcore.event.village.WandererTradesEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;

import java.util.Comparator;
import java.util.function.BiConsumer;

public class GlitchCoreFabric implements ModInitializer, ClientModInitializer
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
    }

    @Override
    public void onInitializeClient()
    {
        // GlitchCore initialization
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

        // Perform initialization for dependants
        FabricLoader.getInstance().getEntrypointContainers("glitchcore", GlitchCoreInitializer.class).forEach(entrypoint -> {
            GlitchCoreInitializer initializer = entrypoint.getEntrypoint();
            initializer.onInitializeClient();
        });

        EventManager.fire(new RegisterColorsEvent.Block(ColorProviderRegistry.BLOCK::register));
        EventManager.fire(new RegisterColorsEvent.Item(ColorProviderRegistry.ITEM::register));

        BiConsumer<ParticleType<?>, ParticleEngine.SpriteParticleRegistration<?>> particleSpriteRegisterFunc = (type, registration) -> {
            ParticleFactoryRegistry.getInstance().register(type, provider -> (ParticleProvider)registration.create(provider));
        };
        EventManager.fire(new RegisterParticleSpritesEvent(particleSpriteRegisterFunc));
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
