/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.network;

import glitchcore.network.CustomPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.Optional;

public class GCPayloadFactory<T extends CustomPacket<T>>
{
    private final CustomPacketPayload.Type<? extends CustomPacketPayload> type;
    private final CustomPacket<T> packet;
    private final PayloadHandler payloadHandler;

    public GCPayloadFactory(CustomPacketPayload.Type<? extends CustomPacketPayload> type, CustomPacket<T> packet)
    {
        this.type = type;
        this.packet = packet;
        this.payloadHandler = new PayloadHandler();
    }

    public CustomPacketPayload.Type<PacketPayload> type()
    {
        return (CustomPacketPayload.Type<PacketPayload>)this.type;
    }

    public StreamCodec<? super FriendlyByteBuf, PacketPayload> getCodec()
    {
        return CustomPacketPayload.codec(PacketPayload::write, PacketPayload::new);
    }
    public PayloadHandler getPayloadHandler()
    {
        return this.payloadHandler;
    }

    public PacketPayload createPayload()
    {
        return new PacketPayload((T) this.packet);
    }

    public class PayloadHandler implements IPayloadHandler<PacketPayload>
    {
        @Override
        public void handle(PacketPayload payload, IPayloadContext context)
        {
            context.enqueueWork(() -> {
                payload.data.handle(payload.data, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return context.flow() == PacketFlow.CLIENTBOUND;
                    }

                    @Override
                    public Optional<Player> getPlayer()
                    {
                        var player = context.player();
                        if (player != null) return Optional.of(player);
                        if (isClientSide()) return Optional.ofNullable(Minecraft.getInstance().player);
                        return Optional.empty();
                    }
                });
            });
        }
    }

    public class PacketPayload implements CustomPacketPayload
    {
        protected final T data;

        private PacketPayload(T data)
        {
            this.data = data;
        }

        PacketPayload(FriendlyByteBuf buf)
        {
            this.data = GCPayloadFactory.this.packet.decode(buf);
        }

        public void write(FriendlyByteBuf buf)
        {
            this.data.encode(buf);
        }

        @Override
        public Type<? extends CustomPacketPayload> type()
        {
            return GCPayloadFactory.this.type;
        }
    }
}
