/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.network;

import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public final class PacketHandler
{
    private final ResourceLocation channelName;

    public PacketHandler(ResourceLocation channelName)
    {
        this.channelName = channelName;
        this.init();
    }

    public void register(ResourceLocation name, CustomPacket<?> packet) { throw new UnsupportedOperationException(); }

    public <T> void sendToPlayer(T data, ServerPlayer player) { throw new UnsupportedOperationException(); }

    public <T> void sendToHandler(T data, ServerConfigurationPacketListenerImpl handler) { throw new UnsupportedOperationException(); }

    public <T> void sendToServer(T data) { throw new UnsupportedOperationException(); }

    private void init() {}
}
