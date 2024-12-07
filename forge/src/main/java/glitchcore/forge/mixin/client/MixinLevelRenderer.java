/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.mixin.client;

import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import glitchcore.event.EventManager;
import glitchcore.event.client.LevelRenderEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer 
{
    @Shadow
    private int ticks;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "lambda$addParticlesPass$2", at=@At(value="INVOKE", target="Lnet/minecraft/client/particle/ParticleEngine;render(Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/culling/Frustum;)V", shift= At.Shift.AFTER))
    public void onAddParticles(FogParameters p_363695_, ResourceHandle resourcehandle1, ResourceHandle resourcehandle, Camera camera, float p_365755_, Frustum frustum, CallbackInfo ci)
    {
        EventManager.fire(new LevelRenderEvent(LevelRenderEvent.Stage.AFTER_PARTICLES, (LevelRenderer)(Object)this, new PoseStack(), RenderSystem.getProjectionMatrix(), this.ticks, this.minecraft.getDeltaTracker(), camera, frustum));
    }
}
