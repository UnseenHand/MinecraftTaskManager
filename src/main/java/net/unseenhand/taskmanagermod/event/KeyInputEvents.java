package net.unseenhand.taskmanagermod.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.client.KeyBindings;
import net.unseenhand.taskmanagermod.network.S2COpenTMScreenFromKeyPressedPacket;
import net.unseenhand.taskmanagermod.network.PacketHandler;

@Mod.EventBusSubscriber(modid = TaskManagerMod.MOD_ID, value = Dist.CLIENT)
public class KeyInputEvents {
    @SubscribeEvent
    public static void onButtonClick(InputEvent.Key event) {
        Minecraft mcClient = Minecraft.getInstance();
        if (mcClient.screen != null)
            return;

        if (KeyBindings.openTaskManagerScreenKeyMapping.isDown()) {
            PacketHandler.sendToAllClients(new S2COpenTMScreenFromKeyPressedPacket());
        }
    }
}
