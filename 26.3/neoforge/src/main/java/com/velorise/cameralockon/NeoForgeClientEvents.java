package com.velorise.cameralockon;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = CameraLockOn.MODID, value = Dist.CLIENT)
public final class NeoForgeClientEvents {
    private NeoForgeClientEvents() {}

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.registerCategory(LockOnController.CATEGORY);
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

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
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
    public static void renderHud(RenderGuiEvent.Post event) {
        LockOnHudRenderer.render(event.getGuiGraphics());
    }
}
