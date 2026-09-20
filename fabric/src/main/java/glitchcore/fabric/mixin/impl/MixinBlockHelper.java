/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.impl;

import glitchcore.fabric.handlers.CompostableHandler;
import glitchcore.util.BlockHelper;
import net.fabricmc.fabric.api.item.v1.BlockTransformerHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlockHelper.class, remap = false)
public class MixinBlockHelper
{
    @Overwrite
    public static void registerStrippable(Block block, Block strippedBlock)
    {
        BlockTransformerHelper.registerStripping(block, strippedBlock);
    }

    @Overwrite
    public static void registerCompostable(ResourceKey<ContextIntProvider> layers, ItemLike item)
    {
        CompostableHandler.register(item.asItem(), layers);
    }
}
