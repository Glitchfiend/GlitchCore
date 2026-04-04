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
import glitchcore.fabric.GlitchCoreInitializer;
import glitchcore.util.Remapper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

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

        ServerTickEvents.START_LEVEL_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.START, level));
        });

        ServerTickEvents.END_LEVEL_TICK.register(level -> {
            EventManager.fire(new TickEvent.Level(TickEvent.Phase.END, level));
        });

        CommandRegistrationCallback.EVENT.register(((dispatcher, context, selection) -> {
            EventManager.fire(new RegisterCommandsEvent(dispatcher, selection, context));
        }));

        CommonLifecycleEvents.TAGS_LOADED.register(((registries, client) -> {
            EventManager.fire(new TagsUpdatedEvent(registries, client ? TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED : TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD));
        }));

        // Remap the BuiltInRegistries
        for (Field field : BuiltInRegistries.class.getFields())
        {
            if (FabricRegistry.class.isAssignableFrom(field.getType()))
            {
                try {
                    var fabricRegistry = (FabricRegistry)field.get(null);
                    ResourceKey<? extends Registry<?>> registryKey = ((Registry)fabricRegistry).key();
                    Remapper.getRemaps(registryKey).forEach((key, value) -> fabricRegistry.addAlias(key.identifier(), value.identifier()));

                } catch (IllegalAccessException ignored) {}
            }
        }
    }

    private static void postRegisterEvents()
    {
        Set<Identifier> registries = new LinkedHashSet<>();
        registries.addAll(VanillaRegistries.createLookup().listRegistryKeys().map(ResourceKey::identifier).toList());
        registries.addAll(BuiltInRegistries.LOADERS.keySet());

        // We use LOADERS to ensure objects are registered at the correct time relative to each other
        for (Identifier registryName : registries)
        {
            ResourceKey<? extends Registry<?>> registryKey = ResourceKey.createRegistryKey(registryName);
            BuiltInRegistries.REGISTRY.get(registryName).ifPresent(registry -> {
                EventManager.fire(new RegistryEvent(registryKey, (location, value) -> Registry.register((Registry<? super Object>)registry.value(), location, value)));
            });
        }
    }
}
