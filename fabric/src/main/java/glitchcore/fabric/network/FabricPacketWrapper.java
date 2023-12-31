/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricPacketWrapper<T extends CustomPacket<T>>
{
    protected final ResourceLocation channel;
    protected final CustomPacket<T> packet;
    protected final PacketType<?> fabricPacketType;

    public FabricPacketWrapper(ResourceLocation channel, CustomPacket<T> packet)
    {
        this.channel = channel;
        this.packet = packet;
        this.fabricPacketType = PacketType.create(this.channel, Impl::new);

        ServerPlayNetworking.registerGlobalReceiver(this.fabricPacketType, new ServerPlayNetworking.PlayPacketHandler()
        {
            @Override
            public void receive(FabricPacket packet, ServerPlayer player, PacketSender responseSender)
            {
                FabricPacketWrapper.this.packet.handle(((Impl)packet).data, new CustomPacket.Context()
                {
                    @Override
                    public boolean isClientSide()
                    {
                        return false;
                    }

                    @Override
                    public ServerPlayer getSender()
                    {
                        return player;
                    }
                });
            }
        });
    }

    public FabricPacket createPacket(T data)
    {
        return new Impl(data);
    }

    class Impl implements FabricPacket
    {
        protected final T data;

        private Impl(T data)
        {
            this.data = data;
        }

        private Impl(FriendlyByteBuf buf)
        {
            this.data = FabricPacketWrapper.this.packet.decode(buf);
        }

        @Override
        public void write(FriendlyByteBuf buf)
        {
            this.data.encode(buf);
        }

        @Override
        public PacketType<?> getType()
        {
            return FabricPacketWrapper.this.fabricPacketType;
        }
    }
}
