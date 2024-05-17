package net.unseenhand.taskmanagermod.network;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;
import net.unseenhand.taskmanagermod.data.TaskManager;

import java.io.IOException;

public class C2SRemoveTaskToPlayerOnButtonClickPacket implements SimplePacketBase {
    private static final String MESSAGE_FAILED_REMOVE_TASK = "Failed to remove task";
    private String id;

    public C2SRemoveTaskToPlayerOnButtonClickPacket(String id) {
        this.id = id;
    }

    public C2SRemoveTaskToPlayerOnButtonClickPacket(FriendlyByteBuf buf) {
        id = buf.readUtf();
    }

    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isClientSide()) {
            return false;
        }

        ServerPlayer player = context.getSender();

        if (player == null) {
            return false;
        }

        player.getCapability(PlayerTasksProvider.PLAYER_TASKS).ifPresent(playerTasks -> {
            playerTasks.removeTaskById(id);
        });

//        try (Level level = player.level()) {
//            TaskManager taskManager = TaskManager.get(level);
//            if (taskManager == null) {
//                return false;
//            }
//
//            if (!taskManager.removeTaskFromPlayer(player, id)) {
//                player.sendSystemMessage(
//                        Component.translatable(MESSAGE_FAILED_REMOVE_TASK).withStyle(ChatFormatting.DARK_RED));
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return true;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(id);
    }
}