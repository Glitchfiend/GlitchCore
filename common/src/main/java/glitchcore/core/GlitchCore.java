/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.core;

import glitchcore.network.PacketHandler;
import glitchcore.network.SyncConfigPacket;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GlitchCore
{
    public static final String MOD_ID = "glitchcore";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static final Identifier CHANNEL = Identifier.fromNamespaceAndPath(MOD_ID, "main");
    public static final PacketHandler PACKET_HANDLER = new PacketHandler(CHANNEL);

    public static void init()
    {
        registerPackets();
    }

    private static void registerPackets()
    {
        PACKET_HANDLER.register(Identifier.fromNamespaceAndPath(MOD_ID, "sync_config"), new SyncConfigPacket());
    }
}
