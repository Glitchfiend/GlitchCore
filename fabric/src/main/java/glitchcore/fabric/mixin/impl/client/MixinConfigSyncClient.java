package glitchcore.fabric.mixin.impl.client;


import glitchcore.config.Config;
import glitchcore.config.ConfigSync;
import glitchcore.util.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Mixin(value = ConfigSync.class, remap = false)
public class MixinConfigSyncClient {

	@Shadow
	private static ResourceLocation CONFIG_SYNC_CHANNEL;
	@Shadow
	private static Map<String, Config> CONFIGS_BY_PATH;

	@Shadow
	private static void reload(String path, String toml) {/*dummy body*/}

	@SuppressWarnings("all")
	@Inject(method = "initSyncs", at = @At(value = "TAIL"))
	private static void onInitSyncs(CallbackInfo ci) {
		ClientPlayNetworking.registerGlobalReceiver(CONFIG_SYNC_CHANNEL, new PlayChannelHandler() {
			@Override
			public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf,
								PacketSender responseSender) {
				reload(buf.readUtf(), new String(buf.readByteArray(), StandardCharsets.UTF_8));
			}
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			CONFIGS_BY_PATH.forEach((path, confik) -> {
				confik.parse(Config.readToml(Environment.getConfigPath().resolve(path)));
				confik.load();
			});
		});
	}
}
