/*******************************************************************************
 * Copyright 2025, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.mixin.client;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import glitchcore.event.EventManager;
import glitchcore.event.client.LevelRenderEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer
{
    @Shadow
    private int ticks;

    @Shadow @Final private Minecraft minecraft;

    @Shadow
    private Frustum capturedFrustum;

    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Unique
    @Nullable
    private Frustum lastFrustum;

    @Inject(method = "prepareCullFrustum", at=@At(value= "RETURN"))
    private void postPrepareCullFrustum(Matrix4f p_254341_, Matrix4f p_332544_, Vec3 p_253766_, CallbackInfoReturnable<Frustum> cir)
    {
        this.lastFrustum = cir.getReturnValue();
    }

    @Inject(method="renderLevel", at=@At(value= "TAIL"))
    public void postRenderLevel(GraphicsResourceAllocator p_367325_, DeltaTracker p_342180_, boolean p_109603_, Camera p_109604_, Matrix4f p_254120_, Matrix4f p_330527_, Matrix4f p_429784_, GpuBufferSlice p_407881_, Vector4f p_410175_, boolean p_407316_, CallbackInfo ci)
    {
        this.clearLastFrustum();
    }

    @Inject(method = "method_62213", at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/state/ParticlesRenderState;reset()V", shift= At.Shift.AFTER))
    public void onAddParticles(GpuBufferSlice p_420874_, ResourceHandle p_420875_, ResourceHandle p_420876_, CallbackInfo ci)
    {
        EventManager.fire(new LevelRenderEvent(LevelRenderEvent.Stage.AFTER_PARTICLES, (LevelRenderer)(Object)this, new PoseStack(), RenderSystem.getModelViewMatrix(), this.ticks, this.minecraft.getDeltaTracker(), this.levelRenderState.cameraRenderState, this.lastFrustum));
    }

    @Unique
    private void clearLastFrustum()
    {
        this.lastFrustum = null;
    }
}
