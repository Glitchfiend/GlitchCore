/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.handlers;

// TODO: TAN
//@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class LevelRenderEventHandler
//{
//    @SubscribeEvent
//    public static void onRender(RenderLevelStageEvent event)
//    {
//        if (event.getStage().equals(AFTER_PARTICLES))
//        {
//            fireStage(LevelRenderEvent.Stage.AFTER_PARTICLES, event);
//        }
//    }
//
//    private static void fireStage(LevelRenderEvent.Stage stage, RenderLevelStageEvent event)
//    {
//        PoseStack poseStack = ((IExtendedDebugRenderer)Minecraft.getInstance().debugRenderer).getLastPoseStack();
//
//        var deltaTracker = new DeltaTracker() {
//            public float getGameTimeDeltaTicks() {
//                return event.getPartialTick();
//            }
//
//            public float getGameTimeDeltaPartialTick(boolean p_344036_) {
//                return event.getPartialTick();
//            }
//
//            public float getRealtimeDeltaTicks() {
//                return event.getPartialTick();
//            }
//        };
//
//        EventManager.fire(new LevelRenderEvent(stage, event.getLevelRenderer(), poseStack, event.getProjectionMatrix(), event.getRenderTick(), deltaTracker, event.getCamera(), event.getFrustum()));
//    }
//}
