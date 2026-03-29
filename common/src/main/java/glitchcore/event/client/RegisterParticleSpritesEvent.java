/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.client;

import glitchcore.event.Event;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.BiConsumer;

public class RegisterParticleSpritesEvent<T extends ParticleOptions> extends Event
{
    private final BiConsumer<ParticleType<T>, ParticleProvider<T>> registerSpriteSetFunc;

    public RegisterParticleSpritesEvent(BiConsumer<ParticleType<T>, ParticleProvider<T>> registerSpriteSetFunc)
    {
        this.registerSpriteSetFunc = registerSpriteSetFunc;
    }

    public void registerSpriteSet(ParticleType<T> type, ParticleProvider<T> registration)
    {
        this.registerSpriteSetFunc.accept(type, registration);
    }
}
