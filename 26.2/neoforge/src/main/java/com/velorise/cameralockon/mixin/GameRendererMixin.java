package com.velorise.cameralockon.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.velorise.cameralockon.LockOnController;
import com.velorise.cameralockon.ProjectileTrajectoryRenderer;
import com.velorise.cameralockon.ThirdPersonAimRayRenderer;
import com.velorise.cameralockon.ThirdPersonCameraController;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "update", at = @At("HEAD"))
    private void cameraLockOn$updateCamera(DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        ThirdPersonCameraController.captureMouseLook();
        LockOnController.updateCameraAngles();
    }

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;render(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/renderer/state/level/CameraRenderState;Lorg/joml/Matrix4fc;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
                    shift = At.Shift.AFTER
            )
    )
    private void cameraLockOn$renderWorldOverlays(DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        PoseStack poseStack = new PoseStack();
        Camera camera = mc.gameRenderer.mainCamera();
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
        Matrix4fStack mvStack = RenderSystem.getModelViewStack();
        mvStack.pushMatrix();
        mvStack.mul(camera.getViewRotationMatrix(new Matrix4f()));
        try {
            LockOnController.renderReticle(poseStack, camera, partialTick);
            ThirdPersonAimRayRenderer.render(poseStack, camera, partialTick);
            ProjectileTrajectoryRenderer.render(poseStack, camera, partialTick);
        } finally {
            mvStack.popMatrix();
        }
    }
}
