package glitchcore.neoforge.mixin.impl;

import com.google.common.base.Preconditions;
import glitchcore.neoforge.network.GCPayloadFactory;
import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.jodah.typetools.TypeResolver;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.spongepowered.asm.mixin.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler
{
    @Shadow
    @Final
    private Identifier channelName;

    @Unique
    private Map<Class<?>, CustomPacketPayload.Type<?>> ids = new HashMap<>();

    @Overwrite
    public <T extends CustomPacket<T>> void register(Identifier name, CustomPacket<T> packet)
    {
        // Store data type -> id mappings
        var type = new CustomPacketPayload.Type<>(Identifier.parse(name.toString()));
        ids.put(getPacketDataType(packet), type);

        // Register an event handler for NeoForge's payload event
        String modid = name.getNamespace();
        ModContainer container = ModList.get().getModContainerById(modid).orElseThrow(() -> new IllegalArgumentException("Channel namespace does not belong to a mod"));
        var factory = new GCPayloadFactory<>(type, packet);

        container.getEventBus().addListener((RegisterPayloadHandlersEvent event) -> {
            PayloadRegistrar registrar = event.registrar(modid);
            registrar.versioned(modid);

            switch (packet.getPhase())
            {
                case PLAY -> registrar.playBidirectional(factory.type(), factory.getCodec(), factory.getPayloadHandler());
                case CONFIGURATION -> registrar.configurationBidirectional(factory.type(), factory.getCodec(), factory.getPayloadHandler());
                default -> throw new UnsupportedOperationException("Attempted to register packet with unsupported phase " + packet.getPhase());
            }
        });

        container.getEventBus().addListener((RegisterClientPayloadHandlersEvent event) -> {
            event.register(factory.type(), factory.getPayloadHandler());
        });
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToPlayer(T data, ServerPlayer player)
    {
        Objects.requireNonNull(player);
        PacketDistributor.sendToPlayer(player, createPayload(data));
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToAll(T packet, MinecraftServer server)
    {
        PacketDistributor.sendToAllPlayers(createPayload(packet));
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToHandler(T packet, ServerConfigurationPacketListenerImpl handler)
    {
        var payload = createPayload(packet);
        switch (handler.getConnection().getSending()) {
            case CLIENTBOUND -> handler.getConnection().send(new ClientboundCustomPayloadPacket(payload));
            case SERVERBOUND -> handler.getConnection().send(new ServerboundCustomPayloadPacket(payload));
        };
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToServer(T data)
    {
        ClientPacketDistributor.sendToServer(createPayload(data));
    }

    @Overwrite
    private void init() {}

    private GCPayloadFactory<?>.PacketPayload createPayload(CustomPacket<?> packet)
    {
        var dataType = getPacketDataType(packet);
        Preconditions.checkState(ids.containsKey(dataType), "Unregistered packet data type " + dataType);
        return new GCPayloadFactory<>(ids.get(dataType), packet).createPayload();
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
