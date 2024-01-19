package glitchcore.neoforge.mixin.impl;

import com.google.common.base.Preconditions;
import glitchcore.neoforge.network.NeoForgePacketWrapper;
import glitchcore.neoforge.network.NetworkUtils;
import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.jodah.typetools.TypeResolver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.spongepowered.asm.mixin.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler
{
    @Shadow
    @Final
    private ResourceLocation channelName;

    @Unique
    private Map<Class<?>, ResourceLocation> ids = new HashMap<>();

    @Overwrite
    public <T extends CustomPacket<T>> void register(ResourceLocation name, CustomPacket<T> packet)
    {
        // Store data type -> id mappings
        ids.put(getPacketDataType(packet), name);

        // Register an event handler for NeoForge's payload event
        String modid = name.getNamespace();
        ModContainer container = ModList.get().getModContainerById(modid).orElseThrow(() -> new IllegalArgumentException("Channel namespace does not belong to a mod"));
        var wrappedPacket = new NeoForgePacketWrapper<>(name, packet);

        container.getEventBus().addListener((RegisterPayloadHandlerEvent event) -> {
            IPayloadRegistrar registrar = event.registrar(modid);
            registrar.versioned(modid);

            switch (packet.getPhase())
            {
                case PLAY -> registrar.play(name, wrappedPacket.getReader(), wrappedPacket.getPlayPayloadHandler());
                case CONFIGURATION -> registrar.configuration(name, wrappedPacket.getReader(), wrappedPacket.getConfigurationPayloadHandler());
                default -> throw new UnsupportedOperationException("Attempted to register packet with unsupported phase " + packet.getPhase());
            }
        });
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToPlayer(T data, ServerPlayer player)
    {
        Objects.requireNonNull(player);
        PacketDistributor.PLAYER.with(player).send(wrapPacket(data));
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToAll(T packet, MinecraftServer server)
    {
        PacketDistributor.ALL.noArg().send(wrapPacket(packet));
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToHandler(T packet, ServerConfigurationPacketListenerImpl handler)
    {
        var wrappedPacket = wrapPacket(packet);
        switch (handler.getConnection().getSending()) {
            case CLIENTBOUND -> NetworkUtils.CLIENTBOUND_CONFIG_LISTENER.with(handler).send(wrappedPacket);
            case SERVERBOUND -> NetworkUtils.SERVERBOUND_CONFIG_LISTENER.with(handler).send(wrappedPacket);
        };
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToServer(T data)
    {
        PacketDistributor.SERVER.noArg().send(wrapPacket(data));
    }

    @Overwrite
    private void init() {}

    private NeoForgePacketWrapper<?> wrapPacket(CustomPacket<?> packet)
    {
        var dataType = getPacketDataType(packet);
        Preconditions.checkState(ids.containsKey(dataType), "Unregistered packet data type " + dataType);
        return new NeoForgePacketWrapper<>(ids.get(dataType), packet);
    }

    private static <T extends CustomPacket<T>> Class<?> getPacketDataType(CustomPacket<T> packet)
    {
        final Class<T> dataType = (Class<T>) TypeResolver.resolveRawArgument(CustomPacket.class, packet.getClass());

        if ((Class<?>)dataType == TypeResolver.Unknown.class)
        {
            throw new IllegalStateException("Failed to resolve packet data type: " + packet);
        }

        return dataType;
    }
}
