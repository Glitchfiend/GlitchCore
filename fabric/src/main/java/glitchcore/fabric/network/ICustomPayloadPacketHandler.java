package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public interface ICustomPayloadPacketHandler
{
    default GCPayloadFactory<?> createPayloadFactory(Identifier channel, CustomPacket<?> packet)
    {
        return new GCPayloadFactory<>(channel, packet);
    }

    <T extends CustomPacket<T>> CustomPacketPayload createCustomPacketPayload(T packet);
}
