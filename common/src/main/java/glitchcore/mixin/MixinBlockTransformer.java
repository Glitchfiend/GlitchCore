/*******************************************************************************
 * Copyright 2026, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import glitchcore.util.StrippableRegistry;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(BlockTransformer.class)
public class MixinBlockTransformer
{
    @ModifyExpressionValue(method = "transformBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/BlockTransformer;transforms:Ljava/util/List;"), remap = false)
    private List<BlockTransformer.BlockTransformData> appendStrippables(List<BlockTransformer.BlockTransformData> original, UseOnContext context)
    {
        var transformer = context.getItemInHand().get(DataComponents.BLOCK_TRANSFORMER);
        return transformer != null && transformer.is(BlockTransformers.AXE) ? StrippableRegistry.appendTo(original) : original;
    }
}
