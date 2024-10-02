package glitchcore.fabric.mixin.impl;

import glitchcore.config.Config;
import glitchcore.config.ConfigSync;
import glitchcore.network.PacketHandler;
import glitchcore.network.SyncConfigPacket;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ConfigSync.class, remap = false)
public class MixinConfigSync {
    @Shadow
    private static PacketHandler packetHandler;
    @Shadow
    @Final
    private static Map<String, Config> CONFIGS_BY_PATH;

    @Overwrite
    public static void initFabric() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            CONFIGS_BY_PATH.forEach((path, config) -> {
                packetHandler.sendToPlayer(new SyncConfigPacket(path,
                        config.encode().getBytes(StandardCharsets.UTF_8)), handler.getPlayer());
            });
        });
    }
}
