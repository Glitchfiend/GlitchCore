/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.util;

import java.nio.file.Path;

public final class Environment
{
    public static boolean isClient()
    {
        throw new UnsupportedOperationException();
    }

    public static Path getConfigPath()
    {
        throw new UnsupportedOperationException();
    }

    public static boolean isModLoaded(String id)
    {
        throw new UnsupportedOperationException();
    }
}
