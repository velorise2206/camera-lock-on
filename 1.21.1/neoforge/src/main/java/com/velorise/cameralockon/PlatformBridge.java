package com.velorise.cameralockon;

import net.neoforged.neoforge.common.ItemAbilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.entity.PartEntity;

import java.nio.file.Path;
import java.util.Map;

public final class PlatformBridge {
    private PlatformBridge() {}

    public static Path configDirectory() {
        return FMLPaths.CONFIGDIR.get().resolve(CameraLockOn.MODID);
    }

    public static boolean canSwordSweep(ItemStack stack) {
        return stack.canPerformAction(ItemAbilities.SWORD_SWEEP);
    }

    public static void collectMultipartSubEntities(LivingEntity target, Map<Entity, String> parts) {
        if (target.isMultipartEntity()) {
            PartEntity<?>[] loaderParts = target.getParts();
            if (loaderParts != null) {
                for (int i = 0; i < loaderParts.length; i++) {
                    parts.putIfAbsent(loaderParts[i], "multipart_part_" + i);
                }
            }
        }
    }

    public static boolean isLoaderPartEntity(Entity part) {
        return part instanceof PartEntity<?>;
    }
}
