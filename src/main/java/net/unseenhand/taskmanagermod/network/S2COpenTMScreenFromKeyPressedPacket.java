package net.unseenhand.taskmanagermod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.gui.TaskManagerGuiWrapper;

import java.io.IOException;

public class S2COpenTMScreenFromKeyPressedPacket implements SimplePacketBase {
    public S2COpenTMScreenFromKeyPressedPacket() {
    }

    public S2COpenTMScreenFromKeyPressedPacket(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {
    }

    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        TaskManagerMod.LOGGER.info("Packet start handling");
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer clientPlayer = mc.player;

        if (clientPlayer == null)
            return false;

        try (Level level = clientPlayer.level()){
            if (!level.isClientSide()) {
                return false;
            }

            TaskManagerGuiWrapper.openGUI(mc, level, clientPlayer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TaskManagerMod.LOGGER.info("Packet handled");
        return true;
    }
}
