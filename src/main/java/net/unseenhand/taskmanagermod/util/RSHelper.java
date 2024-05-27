package net.unseenhand.taskmanagermod.util;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.unseenhand.taskmanagermod.TaskManagerMod;

public class RSHelper {
    public static ResourceLocation mod(String path) {
        return new ResourceLocation(TaskManagerMod.MOD_ID, path);
    }

    public static ResourceLocation mc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public static ModelResourceLocation modelResourceLocation(String path, String variant) {
        return new ModelResourceLocation(mod(path), variant);
    }
}
