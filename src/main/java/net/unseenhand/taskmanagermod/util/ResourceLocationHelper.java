package net.unseenhand.taskmanagermod.util;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.unseenhand.taskmanagermod.TaskManagerMod;

public class ResourceLocationHelper {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(TaskManagerMod.MOD_ID, path);
    }

    public static ModelResourceLocation modelResourceLocation(String path, String variant) {
        return new ModelResourceLocation(prefix(path), variant);
    }
}
