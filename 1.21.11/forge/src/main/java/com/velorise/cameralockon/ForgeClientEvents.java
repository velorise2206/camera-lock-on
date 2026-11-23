package com.velorise.cameralockon;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ForgeClientEvents {
    private ForgeClientEvents() {}

    public static void initClient(FMLJavaModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, parent) -> new LockOnConfigScreen(parent)
                )
        );
        RegisterKeyMappingsEvent.BUS.addListener(ForgeClientEvents::registerKeyMappings);
        TickEvent.ClientTickEvent.Post.BUS.addListener(ForgeClientEvents::clientTick);
        InputEvent.InteractionKeyMappingTriggered.BUS.addListener(ForgeClientEvents::attackInput);
        ViewportEvent.ComputeCameraAngles.BUS.addListener(ForgeClientEvents::computeCameraAngles);
        CustomizeGuiOverlayEvent.Chat.BUS.addListener(ForgeClientEvents::renderHud);
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
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

    private static void clientTick(TickEvent.ClientTickEvent.Post event) {
        TargetCycleController.clientTick();
        LockOnController.clientTick();
        TargetOutlineController.tick();
    }

    private static boolean attackInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isAttack()) return false;
        Minecraft mc = Minecraft.getInstance();
        if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof LivingEntity living) {
            LockOnController.onEntityAttacked(living);
        }
        return false;
    }

    private static void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!ThirdPersonCameraController.shouldOverrideCameraRotation()) return;
        event.setYaw(ThirdPersonCameraController.getVisualYaw());
        event.setPitch(ThirdPersonCameraController.getVisualPitch());
    }

    private static void renderHud(CustomizeGuiOverlayEvent.Chat event) {
        LockOnHudRenderer.render(event.getGuiGraphics());
    }
}
