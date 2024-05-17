package net.unseenhand.taskmanagermod.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    // ScreenEvent - means that screen has to be opened!!!
    @SubscribeEvent
    public static void onButtonClick(InputEvent.Key event) {
        Minecraft mcClient = Minecraft.getInstance();
        if (mcClient.screen != null)
            return;

        Level level = mcClient.level;
        Player player = mcClient.player;

        if (event.getKey() == InputConstants.KEY_H) {
            // mcClient.setScreen(new TaskManagerScreen(player));
        }

        if (KeyBindings.openTaskManagerScreenKeyMapping.isDown()) {
            // PacketHandler.sendToServer(new C2SOpenTMScreenFromKeyPressedPacket());
            PacketHandler.sendToAllClients(new S2COpenTMScreenFromKeyPressedPacket());
            // TaskManagerGuiWrapper.openGUI(level, player);
        }
    }
}
