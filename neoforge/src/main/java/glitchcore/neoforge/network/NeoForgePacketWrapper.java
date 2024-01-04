/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.network;

import glitchcore.core.GlitchCore;
import glitchcore.network.CustomPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.ConfigurationPayloadContext;
import net.neoforged.neoforge.network.handling.IConfigurationPayloadHandler;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class NeoForgePacketWrapper<T extends CustomPacket<T>> implements CustomPacketPayload
{
    private final ResourceLocation id;
    private final CustomPacket<T> packet;
    private final Reader reader;
    private final ConfigurationPayloadHandler configurationPayloadHandler;
    private final PlayPayloadHandler playPayloadHandler;

    public NeoForgePacketWrapper(ResourceLocation id, CustomPacket<T> packet)
    {
        this.id = id;
        this.packet = packet;
        this.reader = new Reader();
        this.configurationPayloadHandler = new ConfigurationPayloadHandler();
        this.playPayloadHandler = new PlayPayloadHandler();
    }

    @Override
    public void write(FriendlyByteBuf buf)
    {
        this.packet.encode(buf);
    }

    @Override
    public ResourceLocation id()
    {
        return this.id;
    }

    public Reader getReader()
    {
        return this.reader;
    }

    public ConfigurationPayloadHandler getConfigurationPayloadHandler()
    {
        return this.configurationPayloadHandler;
    }

    public PlayPayloadHandler getPlayPayloadHandler()
    {
        return this.playPayloadHandler;
    }

    public class Reader implements FriendlyByteBuf.Reader<NeoForgePacketWrapper<T>>
    {
        @Override
        public NeoForgePacketWrapper<T> apply(FriendlyByteBuf buf)
        {
            return new NeoForgePacketWrapper<>(NeoForgePacketWrapper.this.id, NeoForgePacketWrapper.this.packet.decode(buf));
        }
    }

    public class ConfigurationPayloadHandler implements IConfigurationPayloadHandler<NeoForgePacketWrapper<T>>
    {
        @Override
        public void handle(NeoForgePacketWrapper<T> payload, ConfigurationPayloadContext context)
        {
            context.workHandler().execute(() -> {
                payload.packet.handle((T) payload.packet, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return context.flow() == PacketFlow.CLIENTBOUND;
                    }

                    @Override
                    public Optional<Player> getPlayer() {
                        return context.player().or(() -> isClientSide() ? Optional.ofNullable(Minecraft.getInstance().player) : Optional.empty());
                    }
                });
            });
        }
    }

    public class PlayPayloadHandler implements IPlayPayloadHandler<NeoForgePacketWrapper<T>>
    {
        @Override
        public void handle(NeoForgePacketWrapper<T> payload, PlayPayloadContext context)
        {
            context.workHandler().execute(() -> {
                payload.packet.handle((T) payload.packet, new CustomPacket.Context() {
                    @Override
                    public boolean isClientSide() {
                        return context.flow() == PacketFlow.CLIENTBOUND;
                    }

                    @Override
                    public Optional<Player> getPlayer() {
                        return context.player().or(() -> isClientSide() ? Optional.ofNullable(Minecraft.getInstance().player) : Optional.empty());
                    }
                });
            });
        }
    }
}
