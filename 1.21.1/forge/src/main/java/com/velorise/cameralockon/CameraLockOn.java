package com.velorise.cameralockon;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(CameraLockOn.MODID)
public class CameraLockOn {
    public static final String MODID = "camera_lockon";

    public CameraLockOn(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CameraLockOnConfig.initialize(
                    PlatformBridge.configDirectory().resolve("camera_lockon-client.json")
            );
            context.registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (mc, parent) -> new LockOnConfigScreen(parent)
                    )
            );
        }
    }
}
