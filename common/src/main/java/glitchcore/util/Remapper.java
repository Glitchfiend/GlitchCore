/*******************************************************************************
 * Copyright 2026, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Remapper
{
    private static final Map<ResourceKey<?>, ResourceKey<?>> remaps = Maps.newHashMap();

    public static <T> void remap(ResourceKey<T> prev, ResourceKey<T> replacement)
    {
        remaps.put(prev, replacement);
    }

    public static Map<ResourceKey<?>, ResourceKey<?>> getRemaps()
    {
        return ImmutableMap.copyOf(remaps);
    }

    public static Map<ResourceKey<?>, ResourceKey<?>> getRemaps(ResourceKey<? extends Registry<?>> registryKey)
    {
        return remaps.entrySet()
                .stream()
                .filter(e -> e.getKey().registryKey().equals(registryKey))
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static boolean hasRemap(Identifier old)
    {
        return remaps.keySet().stream().anyMatch(key -> key.identifier().equals(old));
    }

    public static Identifier getRemap(Identifier old)
    {
        return remaps.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().identifier(),
                entry -> entry.getValue().identifier()
        )).get(old);
    }

    public static ImmutableSet<ResourceKey<? extends Registry<?>>> getRegistryKeys()
    {
        var builder = ImmutableSet.<ResourceKey<? extends Registry<?>>>builder();

        for (var entry : remaps.entrySet())
        {
            builder.add(entry.getKey().registryKey());
        }

        return builder.build();
    }

    public static Set<String> getNamespaces()
    {
        var builder = ImmutableSet.<String>builder();

        for (var entry : remaps.entrySet())
        {
            builder.add(entry.getKey().identifier().getNamespace());
        }

        return builder.build();
    }
}
