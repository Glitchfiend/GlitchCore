/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.neoforge.handlers;

import glitchcore.event.EventManager;
import glitchcore.event.client.LevelRenderEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import static net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage.AFTER_PARTICLES;

@EventBusSubscriber(value = Dist.CLIENT)
public class LevelRenderEventHandler
{
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event)
    {
        if (event.getStage().equals(AFTER_PARTICLES))
        {
            fireStage(LevelRenderEvent.Stage.AFTER_PARTICLES, event);
        }
    }

    private static void fireStage(LevelRenderEvent.Stage stage, RenderLevelStageEvent event)
    {
        EventManager.fire(new LevelRenderEvent(stage, event.getLevelRenderer(), event.getPoseStack(), event.getModelViewMatrix(), event.getRenderTick(), event.getPartialTick(), event.getCamera(), event.getFrustum()));
    }
}
