package net.unseenhand.taskmanagermod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;

public class C2SSwapEntryOnDraggingPacket implements SimplePacketBase {
    private int i1;
    private int i2;

    public C2SSwapEntryOnDraggingPacket() {
    }

    // TODO: Indices are updated with the list of entries, thus the logic clashes
    public C2SSwapEntryOnDraggingPacket(int i1, int i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    public C2SSwapEntryOnDraggingPacket(FriendlyByteBuf buf) {
        i1 = buf.readInt();
        i2 = buf.readInt();
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
            // TODO: think of how to swap the elements (18.05.24 - 19.05.24)
            playerTasks.swap(i1, i2);
        });

        PacketHandler.sendToServer(new C2SRefreshTSLTasksOnScreenActionPacket());

        // PacketHandler.se(new C2SRefreshTSLTasksOnScreenActionPacket(), player);

        return true;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(i1);
        buf.writeInt(i2);
    }
}
