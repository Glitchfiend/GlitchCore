/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.network;

import glitchcore.network.CustomPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.Optional;

public class NeoForgePacketWrapper<T extends CustomPacket<T>> implements CustomPacketPayload
{
    private final ResourceLocation id;
    private final CustomPacket<T> packet;
    private final Reader reader;
    private final Handler handler;

    public NeoForgePacketWrapper(ResourceLocation id, CustomPacket<T> packet)
    {
        this.id = id;
        this.packet = packet;
        this.reader = new Reader();
        this.handler = new Handler();
    }

    @Override
    public void write(FriendlyByteBuf buf)
    {
        this.packet.encode(buf);
    }

    @Override
    public ResourceLocation id()
    {
        return this.id;
    }

    public Reader getReader()
    {
        return this.reader;
    }

    public Handler getHandler()
    {
        return this.handler;
    }

    public class Reader implements FriendlyByteBuf.Reader<NeoForgePacketWrapper<T>>
    {
        @Override
        public NeoForgePacketWrapper<T> apply(FriendlyByteBuf buf)
        {
            return new NeoForgePacketWrapper<>(NeoForgePacketWrapper.this.id, NeoForgePacketWrapper.this.packet.decode(buf));
        }
    }

    public class Handler implements IPayloadHandler<NeoForgePacketWrapper<T>>
    {
        @Override
        public void handle(NeoForgePacketWrapper<T> payload, IPayloadContext context)
        {
            payload.packet.handle((T) payload.packet, new CustomPacket.Context() {
                @Override
                public boolean isClientSide() {
                    return context.flow() == PacketFlow.CLIENTBOUND;
                }

                @Override
                public Optional<Player> getPlayer() {
                    return context.player();
                }
            });
        }
    }
}
