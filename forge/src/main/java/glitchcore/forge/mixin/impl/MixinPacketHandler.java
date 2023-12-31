package glitchcore.forge.mixin.impl;

import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.jodah.typetools.TypeResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.spongepowered.asm.mixin.*;

import java.util.function.Consumer;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler
{
    private static final PacketDistributor<ServerGamePacketListenerImpl> HANDLER_DISTRIBUTOR = new PacketDistributor<>((distributor, handler) -> handler::send);

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

        this.channel.messageBuilder(dataType).encoder(CustomPacket::encode).decoder(packet::decode).consumerMainThread((data, forgeContext) ->
        {
            forgeContext.enqueueWork(() ->
            {
                packet.handle(data, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return forgeContext.isClientSide();
                    }

                    @Override
                    public ServerPlayer getSender()
                    {
                        return forgeContext.getSender();
                    }
                });
            });
            forgeContext.setPacketHandled(true);
        }).add();
    }

    @Overwrite
    public <T> void sendToPlayer(T data, ServerPlayer player)
    {
        channel.send(data, PacketDistributor.PLAYER.with(player));
    }

    @Overwrite
    public <T> void sendToHandler(T packet, ServerConfigurationPacketListenerImpl handler)
    {
        var connection = handler.getConnection();
        channel.send(packet, connection);
    }

    @Overwrite
    public <T> void sendToServer(T data)
    {
        channel.send(data, PacketDistributor.SERVER.noArg());
    }

    @Overwrite
    private void init()
    {
        this.channel = ChannelBuilder.named(this.channelName).simpleChannel();
    }
}
