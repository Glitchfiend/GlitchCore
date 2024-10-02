package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.minecraft.resources.ResourceLocation;

public interface IFabricPacketHandler
{
    default FabricPacketWrapper<?> createPacketWrapper(ResourceLocation channel, CustomPacket<?> packet)
    {
        return new FabricPacketWrapper<>(channel, packet);
    }

    <T extends CustomPacket<T>> FabricPacket createFabricPacket(T packet);
}
