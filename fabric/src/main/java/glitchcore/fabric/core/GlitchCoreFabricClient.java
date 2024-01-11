/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.core;

import glitchcore.event.EventManager;
import glitchcore.event.client.ItemTooltipEvent;
import glitchcore.event.client.LevelRenderEvent;
import glitchcore.event.client.RegisterColorsEvent;
import glitchcore.event.client.RegisterParticleSpritesEvent;
import glitchcore.event.player.PlayerInteractEvent;
import glitchcore.fabric.GlitchCoreInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.InteractionResult;

import java.util.function.BiConsumer;

public class GlitchCoreFabricClient implements ClientModInitializer
{
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
}
