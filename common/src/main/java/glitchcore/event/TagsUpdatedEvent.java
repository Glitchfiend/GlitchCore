/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import net.minecraft.core.RegistryAccess;

public class TagsUpdatedEvent extends Event
{
    private final RegistryAccess registryAccess;
    private final UpdateCause updateCause;

    public TagsUpdatedEvent(RegistryAccess registryAccess, UpdateCause cause)
    {
        this.registryAccess = registryAccess;
        this.updateCause = cause;
    }

    public RegistryAccess getRegistryAccess()
    {
        return registryAccess;
    }

    public UpdateCause getUpdateCause()
    {
        return updateCause;
    }

    public enum UpdateCause
    {
        SERVER_DATA_LOAD,
        CLIENT_PACKET_RECEIVED
    }
}