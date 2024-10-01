package glitchcore.config;

import glitchcore.core.GlitchCore;
import glitchcore.network.PacketHandler;
import glitchcore.network.SyncConfigPacket;
import glitchcore.util.Environment;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class ConfigSync {
  private static ResourceLocation configSyncChannel;
  public static PacketHandler packetHandler;
  public static final Map<String, Config> CONFIGS_BY_PATH = new HashMap<>();
  private static boolean inited = false;
  /**
   * Enables sync between server and client for your config.
   *
   * @param config your config.
   */
  public static void register(Config config) {
    if (!inited) {
      configSyncChannel = new ResourceLocation(GlitchCore.MOD_ID, "config_sync");
      packetHandler = new PacketHandler(configSyncChannel);
      packetHandler.register(new ResourceLocation(GlitchCore.MOD_ID, "config_sync_packet"),
              new SyncConfigPacket());
      initFabric();
      inited = true;
    }
    String relative = Environment.getConfigPath().relativize(config.getPath()).toString();
    CONFIGS_BY_PATH.put(relative, config);
  }
  private static void initFabric() {
  }
  public static void reload(String path, String toml) {
    var config = CONFIGS_BY_PATH.get(path);
    config.parse(toml);
    config.load();
  }
}
