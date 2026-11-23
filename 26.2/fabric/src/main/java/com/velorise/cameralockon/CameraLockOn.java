package com.velorise.cameralockon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;

/** Fabric 26.2 client entry point. */
public final class CameraLockOn implements ClientModInitializer {
    public static final String MODID = "camera_lockon";

    @Override
    public void onInitializeClient() {
        CameraLockOnConfig.initialize(
                PlatformBridge.configDirectory().resolve("camera_lockon-client.json")
        );

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
            KeyMappingHelper.registerKeyMapping(mapping);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            TargetCycleController.clientTick();
            LockOnController.clientTick();
            TargetOutlineController.tick();
        });

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(MODID, "hud"),
                (graphics, tickCounter) -> LockOnHudRenderer.render(graphics)
        );

        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (level.isClientSide() && entity instanceof LivingEntity living) {
                LockOnController.onEntityAttacked(living);
            }
            return InteractionResult.PASS;
        });
    }
}
