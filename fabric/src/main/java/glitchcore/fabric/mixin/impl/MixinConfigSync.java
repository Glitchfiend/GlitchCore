package glitchcore.fabric.mixin.impl;

import glitchcore.config.Config;
import glitchcore.config.ConfigSync;
import glitchcore.core.GlitchCore;
import glitchcore.util.Environment;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ConfigSync.class, remap = false)
public class MixinConfigSync {
  @Shadow
  @Final
  private static ResourceLocation CONFIG_SYNC_CHANNEL;
  @Shadow
  @Final
  private static Map<String, Config> CONFIGS_BY_PATH;

  @Overwrite
  public static void initSyncs() {
    var earlyPhase = new ResourceLocation("glitchcore", "early");
    ServerPlayConnectionEvents.JOIN.addPhaseOrdering(earlyPhase, Event.DEFAULT_PHASE);

    // the server sends packets to the player
    ServerPlayConnectionEvents.JOIN.register(earlyPhase, (handler, sender, server) -> {
      CONFIGS_BY_PATH.forEach((path, confik) -> {
        var packet = PacketByteBufs.create();
        packet.writeUtf(path);
        packet.writeByteArray(confik.encode().getBytes(StandardCharsets.UTF_8));

        sender.sendPacket(CONFIG_SYNC_CHANNEL, packet);
      });
    });
  }
}
