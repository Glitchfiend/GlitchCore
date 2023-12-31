/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.impl.client;

import glitchcore.fabric.network.ClientFabricPacketWrapper;
import glitchcore.fabric.network.FabricPacketWrapper;
import glitchcore.fabric.network.IFabricPacketHandler;
import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PacketHandler.class, remap = false, priority = 0)
public abstract class MixinPacketHandler implements IFabricPacketHandler
{
    @Overwrite
    public <T> void sendToServer(T packet)
    {
        ClientPlayNetworking.send(createFabricPacket((CustomPacket)packet));
    }

    @Override
    public FabricPacketWrapper<?> createPacketWrapper(ResourceLocation channel, CustomPacket<?> packet)
    {
        return new ClientFabricPacketWrapper(channel, packet);
    }
}
