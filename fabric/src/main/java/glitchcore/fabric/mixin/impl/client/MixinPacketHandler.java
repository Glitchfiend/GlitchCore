/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.impl.client;

import glitchcore.fabric.network.GCPayloadFactoryClient;
import glitchcore.fabric.network.GCPayloadFactory;
import glitchcore.fabric.network.ICustomPayloadPacketHandler;
import glitchcore.network.CustomPacket;
import glitchcore.network.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PacketHandler.class, remap = false)
public abstract class MixinPacketHandler implements ICustomPayloadPacketHandler
{
    @Overwrite
    public <T extends CustomPacket<T>> void sendToServer(T packet)
    {
        CustomPacketPayload payload = createCustomPacketPayload((CustomPacket)packet);
        switch (packet.getPhase())
        {
            case PLAY -> ClientPlayNetworking.send(payload);
            default -> throw new UnsupportedOperationException("Attempted to send packet with unsupported phase " + packet.getPhase());
        }
    }

    @Override
    public GCPayloadFactory<?> createPayloadFactory(Identifier channel, CustomPacket<?> packet)
    {
        return new GCPayloadFactoryClient<>(channel, packet);
    }
}
