/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;

public class ClientFabricPacketWrapper<T extends CustomPacket<T>> extends FabricPacketWrapper<T>
{
    public ClientFabricPacketWrapper(ResourceLocation channel, CustomPacket<T> packet)
    {
        super(channel, packet);

        ClientPlayNetworking.registerGlobalReceiver(this.fabricPacketType, new ClientPlayNetworking.PlayPacketHandler() {

            @Override
            public void receive(FabricPacket packet, LocalPlayer player, PacketSender responseSender) {
                Runnable runnable = () -> {
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
                };

                var executor = Minecraft.getInstance();
                if (!executor.isSameThread()) {
                    executor.submitAsync(runnable);
                } else {
                    runnable.run();
                    CompletableFuture.completedFuture(null);
                }
            }
        });
    }
}
