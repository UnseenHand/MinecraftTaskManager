package net.unseenhand.taskmanagermod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;
import net.unseenhand.taskmanagermod.data.TaskListFilter;
import net.unseenhand.taskmanagermod.model.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class C2SSetFilterOnBtnClickPacket implements SimplePacketBase {
    private String searchTerm = "";
    private boolean byName;
    private boolean byStatus;

    public C2SSetFilterOnBtnClickPacket() {
    }

    public C2SSetFilterOnBtnClickPacket(TaskListFilter filter) {
        this.searchTerm = filter.getSearchTerm();
        this.byName = filter.isByName();
        this.byStatus = filter.isByStatus();
    }

    public C2SSetFilterOnBtnClickPacket(FriendlyByteBuf buf) {
        searchTerm = buf.readUtf();
        byName = buf.readBoolean();
        byStatus = buf.readBoolean();
    }

    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isClientSide())
            return false;

        ServerPlayer player = context.getSender();

        if (player == null)
            return false;

        player.getCapability(PlayerTasksProvider.PLAYER_TASKS).ifPresent(playerTasks -> {
            // Updating the screen values (GOOD IMPL)
            List<String> filtered = new ArrayList<>();

            if (!(searchTerm == null || searchTerm.isEmpty())) {
                filtered = playerTasks.filter((t) -> !t.name().toLowerCase().contains(searchTerm.toLowerCase()))
                        .stream()
                        .map(Task::id)
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            PacketHandler.sendToAllClients(new S2CAddIgnoredTasksPacket(filtered));

            playerTasks.sortBy(byName ? Comparator.comparing(Task::name) : Comparator.comparingInt(Task::status));
        });

        return false;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(searchTerm);
        buf.writeBoolean(byName);
        buf.writeBoolean(byStatus);
    }
}
