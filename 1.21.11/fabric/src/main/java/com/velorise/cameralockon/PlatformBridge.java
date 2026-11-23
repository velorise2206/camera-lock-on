package com.velorise.cameralockon;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;
import java.util.Map;

public final class PlatformBridge {
    private PlatformBridge() {}

    public static Path configDirectory() {
        return FabricLoader.getInstance().getConfigDir().resolve(CameraLockOn.MODID);
    }

    public static boolean canSwordSweep(ItemStack stack) {
        return stack.is(ItemTags.SWORDS);
    }

    public static void collectMultipartSubEntities(LivingEntity target, Map<Entity, String> parts) {
        // Fabric vanilla multipart entities are handled directly via EnderDragonPart in MultipartDamageBoxResolver.
    }

    public static boolean isLoaderPartEntity(Entity part) {
        return part.getClass().getSimpleName().contains("PartEntity");
    }
}
