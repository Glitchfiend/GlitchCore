/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge;

import glitchcore.core.GlitchCore;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(value = GlitchCore.MOD_ID)
public class GlitchCoreForge
{
    public GlitchCoreForge()
    {
        GlitchCore.init();
    }

    @Deprecated
    public static void prepareModEventHandlers(IEventBus modEventBus)
    {

    }
}
