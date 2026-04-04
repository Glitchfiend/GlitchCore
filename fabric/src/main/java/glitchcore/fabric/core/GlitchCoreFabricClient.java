/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.core;

import glitchcore.event.EventManager;
import glitchcore.event.client.*;
import glitchcore.event.player.PlayerInteractEvent;
import glitchcore.fabric.GlitchCoreInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.InteractionResult;

import java.util.function.BiConsumer;

public class GlitchCoreFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        // GlitchCore initialization
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            var player = Minecraft.getInstance().player;
            EventManager.fire(new ItemTooltipEvent(player, stack, lines));
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            var event = new PlayerInteractEvent.UseBlock(player, hand, hitResult);
            EventManager.fire(event);

            if (event.isCancelled())
                return event.getCancelResult();

            return InteractionResult.PASS;
        });

        // Perform initialization for dependants
        FabricLoader.getInstance().getEntrypointContainers("glitchcore", GlitchCoreInitializer.class).forEach(entrypoint -> {
            GlitchCoreInitializer initializer = entrypoint.getEntrypoint();
            initializer.onInitializeClient();
        });

        EventManager.fire(new RegisterLayerDefinitionsEvent());
        EventManager.fire(new RegisterRenderersEvent());
        EventManager.fire(new RegisterColorsEvent.Block(BlockColorRegistry::register));

        BiConsumer<ParticleType, RegisterParticleSpritesEvent.SpriteParticleRegistration> particleSpriteRegisterFunc = (type, registration) -> {
            ParticleProviderRegistry.getInstance().register(type, registration::create);
        };
        EventManager.fire(new RegisterParticleSpritesEvent(particleSpriteRegisterFunc));
    }
}
