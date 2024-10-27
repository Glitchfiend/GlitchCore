/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;

public class TagsUpdatedEvent extends Event
{
    private final HolderLookup.Provider lookupProvider;
    private final UpdateCause updateCause;

    public TagsUpdatedEvent(HolderLookup.Provider lookupProvider, UpdateCause cause)
    {
        this.lookupProvider = lookupProvider;
        this.updateCause = cause;
    }

    public HolderGetter.Provider getLookupProvider()
    {
        return this.lookupProvider;
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