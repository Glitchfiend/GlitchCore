package glitchcore.forge.mixin.impl;

import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.jodah.typetools.TypeResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.spongepowered.asm.mixin.*;

import java.util.Optional;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler {
    @Shadow
    @Final
    private ResourceLocation channelName;

    @Unique
    private SimpleChannel channel;

    @Overwrite
    public <T extends CustomPacket<T>> void register(ResourceLocation name, CustomPacket<T> packet)
    {
        final Class<T> dataType = (Class<T>) TypeResolver.resolveRawArgument(CustomPacket.class, packet.getClass());

        if ((Class<?>)dataType == TypeResolver.Unknown.class)
        {
            throw new IllegalStateException("Failed to resolve packet data type: " + packet);
        }

        this.channel.messageBuilder(dataType, 1).encoder(CustomPacket::encode).decoder(packet::decode).consumerMainThread((data, forgeContext) ->
        {
            forgeContext.get().enqueueWork(() ->
            {
                packet.handle(data, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return forgeContext.get().getNetworkManager().getReceiving() == PacketFlow.CLIENTBOUND;
                    }

                    @Override
                    public Optional<Player> getPlayer()
                    {
                        return Optional.ofNullable((Player)forgeContext.get().getSender()).or(() -> isClientSide() ? Optional.ofNullable(Minecraft.getInstance().player) : Optional.empty());
                    }
                });
            });
            forgeContext.get().setPacketHandled(true);
        }).add();
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToPlayer(T data, ServerPlayer player)
    {
        channel.send(PacketDistributor.PLAYER.with(() -> player), data);
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToAll(T packet, MinecraftServer server)
    {
        channel.send(PacketDistributor.ALL.noArg(), packet);
    }

    @Overwrite
    public <T extends CustomPacket<T>> void sendToServer(T data)
    {
        channel.send(PacketDistributor.SERVER.noArg(), data);
    }

    @Overwrite
    private void init()
    {
        String protocolVersion = Integer.toString(1);
        this.channel = ChannelBuilder.named(this.channelName)
                .clientAcceptedVersions(protocolVersion::equals)
                .serverAcceptedVersions(protocolVersion::equals)
                .networkProtocolVersion(() -> protocolVersion)
                .simpleChannel();
    }
}
