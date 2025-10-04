/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.client;

import glitchcore.event.Event;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.BiConsumer;

public class RegisterParticleSpritesEvent extends Event
{
    private final BiConsumer<ParticleType<?>, ParticleResources.SpriteParticleRegistration<?>> registerSpriteSetFunc;

    public RegisterParticleSpritesEvent(BiConsumer<ParticleType<?>, ParticleResources.SpriteParticleRegistration<?>> registerSpriteSetFunc)
    {
        this.registerSpriteSetFunc = registerSpriteSetFunc;
    }

    public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> type, ParticleResources.SpriteParticleRegistration<T> registration)
    {
        this.registerSpriteSetFunc.accept(type, registration);
    }
}
