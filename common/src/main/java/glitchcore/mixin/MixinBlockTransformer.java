/*******************************************************************************
 * Copyright 2026, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import glitchcore.core.GlitchCore;
import glitchcore.util.BlockTransformerExtensions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.context.UseOnContext;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(BlockTransformer.class)
public class MixinBlockTransformer
{
    @ModifyExpressionValue(method = {
            "transformBlock(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;",
            "transformBlock(Lnet/minecraft/world/item/context/UseOnContext;Lnet/minecraft/core/Holder;)Lnet/minecraft/world/InteractionResult;",
    }, at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/BlockTransformer;transforms:Ljava/util/List;"), allow = 1, remap = false)
    private List<BlockTransformer.BlockTransformData> appendExtensions(List<BlockTransformer.BlockTransformData> original, UseOnContext context)
    {
        // The transformer's registry key is only available from the holder on the item
        var transformer = context.getItemInHand().get(DataComponents.BLOCK_TRANSFORMER);
        if (transformer == null || transformer.value() != (Object)this)
            return original;

        return BlockTransformerExtensions.appendTo(transformer, original, context.getLevel().registryAccess());
    }
}
