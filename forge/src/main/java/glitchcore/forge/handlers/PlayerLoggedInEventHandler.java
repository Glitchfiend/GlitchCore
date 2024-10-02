package glitchcore.forge.handlers;

import glitchcore.config.ConfigSync;
import glitchcore.network.SyncConfigPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.charset.StandardCharsets;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerLoggedInEventHandler {

	@SubscribeEvent
	public static void onJoin(PlayerEvent.PlayerLoggedInEvent e) {
		if (ConfigSync.CONFIGS_BY_PATH.isEmpty()) return;
		ConfigSync.CONFIGS_BY_PATH.forEach((path, config) -> {
			ConfigSync.packetHandler.sendToPlayer(new SyncConfigPacket(path,
					config.encode().getBytes(StandardCharsets.UTF_8)), (ServerPlayer) e.getEntity());
		});
	}
}
