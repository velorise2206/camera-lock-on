package com.velorise.cameralockon;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(CameraLockOn.MODID)
public class CameraLockOn {
    public static final String MODID = "camera_lockon";

    public CameraLockOn(ModContainer modContainer) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CameraLockOnConfig.initialize(
                    PlatformBridge.configDirectory().resolve("camera_lockon-client.json")
            );
            modContainer.registerExtensionPoint(
                    IConfigScreenFactory.class,
                    (mc, parent) -> new LockOnConfigScreen(parent)
            );
        }
    }
}
