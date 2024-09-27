package glitchcore.fabric.mixin.impl;

import glitchcore.config.Config;
import glitchcore.config.ConfigSync;
import glitchcore.util.Environment;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ConfigSync.class)
public class MixinConfigSync {
  @Unique
  private static final ResourceLocation CONFIG_SYNC_CHANNEL = new ResourceLocation("glitchcorefabric", "config_sync");
  @Unique
  private static Map<String, Config> configsByPath = new HashMap<>();

  @Overwrite
  public static void register(Config config) {
    String relative = Environment.getConfigPath().relativize(config.getPath()).toString();
    configsByPath.put(relative, config);
  }

  @Unique
  private static void reload(String path, String toml) {
    var config = configsByPath.get(path);
    config.parse(toml);
    config.load();
  }

  static {
    var earlyPhase = new ResourceLocation("glitchcore", "early");
    ServerPlayConnectionEvents.JOIN.addPhaseOrdering(earlyPhase, Event.DEFAULT_PHASE);

    // the server sends packets to the player
    ServerPlayConnectionEvents.JOIN.register(earlyPhase, (handler, sender, server) -> {
      configsByPath.forEach((path, config) -> {
        var packet = PacketByteBufs.create();
        packet.writeUtf(path);
        packet.writeByteArray(config.encode().getBytes(StandardCharsets.UTF_8));

        sender.sendPacket(CONFIG_SYNC_CHANNEL, packet);
      });
    });

    // the player accepts the packets
    if (Environment.isClient()) {
      ClientPlayNetworking.registerGlobalReceiver(CONFIG_SYNC_CHANNEL, new PlayChannelHandler() {
        @Override
        public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
            PacketSender responseSender) {
          reload(buf.readUtf(), new String(buf.readByteArray(), StandardCharsets.UTF_8));
        }
      });
    }
  }
}
