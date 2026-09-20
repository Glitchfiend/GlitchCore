/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

import java.util.HashSet;
import java.util.Set;

public class BlockHelper
{
    public static void registerCompostable(ResourceKey<ContextIntProvider> layers, ItemLike item)
    {
        throw new UnsupportedOperationException();
    }

    public static void registerStrippable(Block block, Block strippedBlock)
    {
        StrippableRegistry.register(block, strippedBlock);
    }

    public static void registerFlammable(Block block, int encouragement, int flammability)
    {
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        fireblock.setFlammable(block, encouragement, flammability);
    }

    public static <T extends BlockEntity> void addBlockEntityBlocks(BlockEntityType<T> type, Block... blocks)
    {
        type.validBlocks = new HashSet<Block>(type.validBlocks);
        type.validBlocks.addAll(Set.of(blocks));
    }
}
