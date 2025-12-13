/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class GCPayloadFactory<T extends CustomPacket<T>>
{
    protected final CustomPacketPayload.Type<Impl> type;
    protected final CustomPacket<T> packet;
    private final StreamCodec<? super FriendlyByteBuf, Impl> codec;

    public GCPayloadFactory(Identifier name, CustomPacket<T> packet)
    {
        this.type = new CustomPacketPayload.Type(Identifier.parse(name.toString()));
        this.packet = packet;
        this.codec = CustomPacketPayload.codec(Impl::write, Impl::new);

        if (packet.getPhase() == CustomPacket.Phase.PLAY)
        {
            PayloadTypeRegistry.playC2S().register(this.type, this.codec);
            PayloadTypeRegistry.playS2C().register(this.type, this.codec);
            ServerPlayNetworking.registerGlobalReceiver(this.type, new ServerPlayNetworking.PlayPayloadHandler<Impl>() {
                @Override
                public void receive(Impl payload, ServerPlayNetworking.Context context)
                {
                    GCPayloadFactory.this.packet.handle(payload.data, new CustomPacket.Context() {
                        @Override
                        public boolean isClientSide() {
                            return false;
                        }

                        @Override
                        public Optional<Player> getPlayer() {
                            return Optional.of(context.player());
                        }
                    });
                }
            });
        }
        else if (packet.getPhase() == CustomPacket.Phase.CONFIGURATION)
        {
            PayloadTypeRegistry.configurationC2S().register(this.type, this.codec);
            PayloadTypeRegistry.configurationS2C().register(this.type, this.codec);
            ServerConfigurationNetworking.registerGlobalReceiver(this.type, new ServerConfigurationNetworking.ConfigurationPacketHandler<Impl>()
            {
                @Override
                public void receive(Impl payload, ServerConfigurationNetworking.Context context)
                {
                    GCPayloadFactory.this.packet.handle(payload.data, new CustomPacket.Context()
                    {
                        @Override
                        public boolean isClientSide()
                        {
                            return false;
                        }

                        @Override
                        public Optional<Player> getPlayer()
                        {
                            return Optional.empty();
                        }
                    });
                }
            });
        }
    }

    public CustomPacketPayload createPayload(T data)
    {
        return new Impl(data);
    }

    public class Impl implements CustomPacketPayload
    {
        protected final T data;

        private Impl(T data)
        {
            this.data = data;
        }

        Impl(FriendlyByteBuf buf)
        {
            this.data = GCPayloadFactory.this.packet.decode(buf);
        }

        public void write(FriendlyByteBuf buf)
        {
            this.data.encode(buf);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return GCPayloadFactory.this.type;
        }
    }
}
