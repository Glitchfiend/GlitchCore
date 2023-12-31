package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;

public interface IFabricPacketHandler
{
    <T extends CustomPacket<T>> FabricPacket createFabricPacket(T packet);
}
