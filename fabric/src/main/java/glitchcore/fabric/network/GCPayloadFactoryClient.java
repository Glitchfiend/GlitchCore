/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class GCPayloadFactoryClient<T extends CustomPacket<T>> extends GCPayloadFactory<T>
{
    public GCPayloadFactoryClient(Identifier channel, CustomPacket<T> packet)
    {
        super(channel, packet);

        if (packet.getPhase() == CustomPacket.Phase.PLAY)
        {
            ClientPlayNetworking.registerGlobalReceiver(this.type, new ClientPlayNetworking.PlayPayloadHandler<Impl>() {
                @Override
                public void receive(Impl payload, ClientPlayNetworking.Context context) {
                    GCPayloadFactoryClient.this.packet.handle(payload.data, new CustomPacket.Context() {
                        @Override
                        public boolean isClientSide() {
                            return true;
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
            ClientConfigurationNetworking.registerGlobalReceiver(this.type, new ClientConfigurationNetworking.ConfigurationPayloadHandler<Impl>() {
                @Override
                public void receive(Impl payload, ClientConfigurationNetworking.Context context) {
                    GCPayloadFactoryClient.this.packet.handle(payload.data, new CustomPacket.Context() {
                        @Override
                        public boolean isClientSide() {
                            return true;
                        }

                        @Override
                        public Optional<Player> getPlayer() {
                            return Optional.empty();
                        }
                    });
                }
            });
        }
    }
}
