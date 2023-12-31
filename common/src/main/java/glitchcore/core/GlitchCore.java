/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.core;

import glitchcore.config.Config;
import glitchcore.config.ConfigSync;
import glitchcore.network.PacketHandler;
import glitchcore.network.SyncConfigPacket;
import glitchcore.util.Environment;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GlitchCore
{
    public static final String MOD_ID = "glitchcore";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static final ResourceLocation CHANNEL = new ResourceLocation(MOD_ID, "main");
    public static final PacketHandler PACKET_HANDLER = new PacketHandler(CHANNEL);

    public static void init()
    {
        registerPackets();
    }

    private static void registerPackets()
    {
        PACKET_HANDLER.register(new ResourceLocation(MOD_ID, "sync_config"), new SyncConfigPacket());
    }
}
