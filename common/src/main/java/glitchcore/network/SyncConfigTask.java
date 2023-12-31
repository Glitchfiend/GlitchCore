/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.network;

import glitchcore.config.ConfigSync;
import glitchcore.core.GlitchCore;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

import java.util.function.Consumer;

public class SyncConfigTask implements ConfigurationTask
{
    private static final Type TYPE = new Type("glitchcore:sync_config");
    private final ServerConfigurationPacketListenerImpl handler;
    private final Consumer<Type> finish;

    public SyncConfigTask(ServerConfigurationPacketListenerImpl handler, Consumer<Type> finish)
    {
        this.handler = handler;
        this.finish = finish;
    }

    @Override
    public void start(Consumer<Packet<?>> send)
    {
        ConfigSync.createPackets().forEach(p -> GlitchCore.PACKET_HANDLER.sendToHandler(p, this.handler));
        this.finish.accept(TYPE);
    }

    @Override
    public Type type()
    {
        return TYPE;
    }
}
