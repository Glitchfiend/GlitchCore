/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import glitchcore.event.client.LevelRenderEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import static glitchcore.event.client.LevelRenderEvent.Stage.AFTER_PARTICLES;

@EventBusSubscriber(value = Dist.CLIENT)
public class LevelRenderEventHandler
{
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent.AfterParticles event)
    {
        fireStage(AFTER_PARTICLES, event);
    }

    private static void fireStage(LevelRenderEvent.Stage stage, RenderLevelStageEvent event)
    {
        EventManager.fire(new LevelRenderEvent(stage, event.getLevelRenderer(), event.getPoseStack(), event.getModelViewMatrix(), event.getLevelRenderer().getTicks(), Minecraft.getInstance().getDeltaTracker(), event.getLevelRenderState().cameraRenderState, event.getLevelRenderer().getCapturedFrustum()));
    }
}
