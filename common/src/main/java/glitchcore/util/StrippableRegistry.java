/*******************************************************************************
 * Copyright 2026, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.util;

import net.minecraft.core.component.BlockTransformer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class StrippableRegistry
{
    private static final Map<Block, Block> STRIPPABLES = new LinkedHashMap<>();
    private static volatile BlockTransformer.BlockTransformData transformData;

    private StrippableRegistry() {}

    static synchronized void register(Block block, Block strippedBlock)
    {
        STRIPPABLES.put(Objects.requireNonNull(block), Objects.requireNonNull(strippedBlock));
        var rules = RuleBasedStateProvider.builder();
        STRIPPABLES.forEach((source, target) -> rules.ifTrueThenProvide(BlockPredicate.matchesBlocks(source), new CopyPropertiesProvider(target)));
        transformData = BlockTransformer.BlockTransformData.builder(rules.build()).sound(SoundEvents.AXE_STRIP).build();
    }

    public static List<BlockTransformer.BlockTransformData> appendTo(List<BlockTransformer.BlockTransformData> original)
    {
        if (transformData == null)
            return original;

        var result = new ArrayList<>(original);
        result.add(transformData);
        return result;
    }
}
