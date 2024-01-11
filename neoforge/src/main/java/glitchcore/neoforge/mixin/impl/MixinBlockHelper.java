/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.mixin.impl;

import com.mojang.datafixers.util.Pair;
import glitchcore.neoforge.handlers.ToolModificationEventHandler;
import glitchcore.util.BlockHelper;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Predicate;

@Mixin(value = BlockHelper.class, remap = false)
public class MixinBlockHelper
{
    @Overwrite
    public static void registerTillable(Block input, Predicate<UseOnContext> usePredicate, BlockState tilled)
    {
        ToolModificationEventHandler.tillables.put(input, Pair.of(usePredicate, tilled));
    }
}
