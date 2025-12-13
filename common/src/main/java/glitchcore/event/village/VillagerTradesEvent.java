/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.village;

import glitchcore.event.Event;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;

import java.util.List;
import java.util.function.Function;

public class VillagerTradesEvent extends Event
{
    private final ResourceKey<VillagerProfession> profession;
    private final int level;
    private final List<VillagerTrades.ItemListing> trades;

    public VillagerTradesEvent(ResourceKey<VillagerProfession> profession, int level, List<VillagerTrades.ItemListing> trades)
    {
        this.profession = profession;
        this.level = level;
        this.trades = trades;
    }

    public ResourceKey<VillagerProfession> getProfession()
    {
        return this.profession;
    }

    public int getLevel()
    {
        return this.level;
    }

    public List<VillagerTrades.ItemListing> getTrades()
    {
        return this.trades;
    }
}
