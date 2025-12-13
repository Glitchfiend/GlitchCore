package glitchcore.event;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public final class RegistryEvent extends Event
{
    @NotNull
    protected final ResourceKey<? extends Registry<?>> registryKey;
    @NotNull
    private final BiConsumer<Identifier, ?> doRegister;

    public RegistryEvent(ResourceKey<? extends Registry<?>> registryKey, BiConsumer<Identifier, ?> doRegister)
    {
        this.registryKey = registryKey;
        this.doRegister = doRegister;
    }

    public <T> T register(Identifier location, T value)
    {
        ((BiConsumer<Identifier, T>)this.doRegister).accept(location, value);
        return value;
    }

    public ResourceKey<? extends Registry<?>> getRegistryKey()
    {
        return this.registryKey;
    }
}
