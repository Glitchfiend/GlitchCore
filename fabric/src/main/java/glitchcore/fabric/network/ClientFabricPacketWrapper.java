/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ClientFabricPacketWrapper<T extends CustomPacket<T>> extends FabricPacketWrapper<T>
{
    public ClientFabricPacketWrapper(ResourceLocation channel, CustomPacket<T> packet)
    {
        super(channel, packet);

        ClientPlayNetworking.registerGlobalReceiver(this.fabricPacketType, new ClientPlayNetworking.PlayPacketHandler() {
            @Override
            public void receive(FabricPacket packet, LocalPlayer player, PacketSender responseSender) {
                ClientFabricPacketWrapper.this.packet.handle(((Impl) packet).data, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return true;
                    }

                    @Override
                    public ServerPlayer getSender() {
                        return null;
                    }
                });
            }
        });
    }
}
