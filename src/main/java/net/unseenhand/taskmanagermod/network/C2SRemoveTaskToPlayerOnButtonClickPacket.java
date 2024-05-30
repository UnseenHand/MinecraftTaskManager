package net.unseenhand.taskmanagermod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;

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

        return true;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(id);
    }
}
