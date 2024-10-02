package glitchcore.forge.handlers;

import glitchcore.config.Config;
import glitchcore.config.ConfigSync;
import glitchcore.util.Environment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerQuitClientEventHandler
{
    @SubscribeEvent
    public static void onQuit(ClientPlayerNetworkEvent.LoggingOut e)
    {
        if (!ConfigSync.CONFIGS_BY_PATH.isEmpty())
        {
            ConfigSync.CONFIGS_BY_PATH.forEach((path, config) -> {
                config.parse(Config.readToml(Environment.getConfigPath().resolve(path)));
                config.load();
            });
        }
    }
}
