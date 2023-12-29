package glitchcore.neoforge.mixin.impl;

import glitchcore.util.Environment;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.file.Path;

@Mixin(value = Environment.class, remap = false)
public abstract class MixinEnvironment
{
    @Overwrite
    public static boolean isClient()
    {
        return FMLEnvironment.dist.isClient();
    }

    @Overwrite
    public static Path getConfigPath()
    {
        return FMLPaths.CONFIGDIR.get();
    }
}
