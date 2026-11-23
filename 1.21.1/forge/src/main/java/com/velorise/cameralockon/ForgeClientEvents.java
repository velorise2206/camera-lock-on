package com.velorise.cameralockon;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public final class ForgeClientEvents {
    private ForgeClientEvents() {}

    @Mod.EventBusSubscriber(modid = CameraLockOn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBus {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            KeyMapping[] mappings = new KeyMapping[] {
                LockOnController.LOCK_ON_KEY,
                LockOnController.SWITCH_TARGET_KEY,
                LockOnController.SWITCH_TARGET_PREVIOUS_KEY,
                LockOnController.PIN_TARGET_TYPE_KEY,
                LockOnController.OPEN_CONFIG_KEY,
                LockOnController.TOGGLE_AUTO_LOCK_KEY,
                LockOnController.TOGGLE_SMART_LOCK_KEY,
                LockOnController.TOGGLE_HOSTILE_ONLY_KEY,
                LockOnController.TOGGLE_HUD_KEY,
                LockOnController.CYCLE_TARGET_PRIORITY_KEY,
                LockOnController.CYCLE_SWITCH_MODE_KEY,
                LockOnController.CLEAR_PIN_KEY,
                LockOnController.CYCLE_CAMERA_POSITION_KEY,
                LockOnController.TOGGLE_AUTO_RELEASE_BOW_KEY,
                LockOnController.TOGGLE_AUTO_RECHARGE_BOW_KEY,
                LockOnController.CYCLE_PROJECTILE_ASSIST_KEY,
                LockOnController.CAMERA_X_DECREASE_KEY,
                LockOnController.CAMERA_X_INCREASE_KEY,
                LockOnController.CAMERA_Y_DECREASE_KEY,
                LockOnController.CAMERA_Y_INCREASE_KEY,
                LockOnController.CAMERA_Z_DECREASE_KEY,
                LockOnController.CAMERA_Z_INCREASE_KEY
        };
            for (KeyMapping mapping : mappings) {
                event.register(mapping);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = CameraLockOn.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static final class GameBus {
        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent.Post event) {
            TargetCycleController.clientTick();
            LockOnController.clientTick();
            TargetOutlineController.tick();
        }

        @SubscribeEvent
        public static void attackInput(InputEvent.InteractionKeyMappingTriggered event) {
            if (!event.isAttack()) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof LivingEntity living) {
                LockOnController.onEntityAttacked(living);
            }
        }

        @SubscribeEvent
        public static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            if (!ThirdPersonCameraController.shouldOverrideCameraRotation()) return;
            event.setYaw(ThirdPersonCameraController.getVisualYaw());
            event.setPitch(ThirdPersonCameraController.getVisualPitch());
        }

        @SubscribeEvent
        public static void renderLevel(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;
            PoseStack poseStack = new PoseStack();
            float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
            LockOnController.renderReticle(poseStack, event.getCamera(), partialTick);
            ThirdPersonAimRayRenderer.render(poseStack, event.getCamera(), partialTick);
            ProjectileTrajectoryRenderer.render(poseStack, event.getCamera(), partialTick);
        }

        @SubscribeEvent
        public static void renderHud(CustomizeGuiOverlayEvent.Chat event) {
            LockOnHudRenderer.render(event.getGuiGraphics());
        }
    }
}
