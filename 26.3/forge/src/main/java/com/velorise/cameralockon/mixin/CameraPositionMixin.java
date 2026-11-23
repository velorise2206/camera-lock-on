package com.velorise.cameralockon.mixin;

import com.velorise.cameralockon.ThirdPersonAimResolver;
import com.velorise.cameralockon.ThirdPersonCameraController;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraPositionMixin {
    @Shadow protected abstract void setPosition(double x, double y, double z);
    @Shadow protected abstract void setRotation(float yaw, float pitch);
    @Shadow public abstract Vec3 position();
    @Shadow public abstract Entity entity();
    @Shadow public abstract boolean isDetached();

    @Inject(method = "alignWithEntity", at = @At("RETURN"))
    private void cameraLockOn$applyThirdPersonOffset(float partialTick, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!isDetached()
                || minecraft.options.getCameraType().isMirrored()
                || !(entity() instanceof LocalPlayer player)) {
            return;
        }

        Vec3 adjusted = ThirdPersonCameraController.apply(
                player, position(), partialTick);
        setPosition(adjusted.x, adjusted.y, adjusted.z);

        if (ThirdPersonCameraController.shouldOverrideCameraRotation()) {
            setRotation(
                    ThirdPersonCameraController.getVisualYaw(),
                    ThirdPersonCameraController.getVisualPitch()
            );
        }

        /*
         * Camera direction remains controlled by the mouse. Only the player's
         * real eye/projectile rotation converges on the centered camera ray.
         */
        ThirdPersonAimResolver.alignFreeAim((Camera) (Object) this, player);
    }
}
