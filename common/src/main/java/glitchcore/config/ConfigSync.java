package glitchcore.config;

import glitchcore.core.GlitchCore;
import glitchcore.util.Environment;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class ConfigSync {
  private static final ResourceLocation CONFIG_SYNC_CHANNEL = new ResourceLocation("glitchcorefabric", "config_sync");
  private static final Map<String, Config> CONFIGS_BY_PATH = new HashMap<>();
  private static boolean inited = false;
  /**
   * Enables sync between server and client for your config.
   * NOTE: This function works only on fabric, you need to implement it yourself
   * if you want to use it on forge
   *
   * @param config your config.
   */
  public static void register(Config config) {
    if (!inited) {
      initSyncs();
      inited = true;
    }
    String relative = Environment.getConfigPath().relativize(config.getPath()).toString();
    CONFIGS_BY_PATH.put(relative, config);
  }
  private static void initSyncs() {
    throw new UnsupportedOperationException();
  }
  private static void reload(String path, String toml) {
    var config = CONFIGS_BY_PATH.get(path);
    config.parse(toml);
    config.load();
  }
}
