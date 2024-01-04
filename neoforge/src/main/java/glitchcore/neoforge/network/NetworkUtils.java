/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Consumer;

public class NetworkUtils
{
    public static final PacketDistributor<ServerConfigurationPacketListenerImpl> CLIENTBOUND_CONFIG_LISTENER = new PacketDistributor<>(NetworkUtils::configListenerConsumer, PacketFlow.CLIENTBOUND);
    public static final PacketDistributor<ServerConfigurationPacketListenerImpl> SERVERBOUND_CONFIG_LISTENER = new PacketDistributor<>(NetworkUtils::configListenerConsumer, PacketFlow.SERVERBOUND);

    private static Consumer<Packet<?>> configListenerConsumer(PacketDistributor<?> distributor, final ServerConfigurationPacketListenerImpl handler)
    {
        return p -> handler.getConnection().send(p);
    }
}
