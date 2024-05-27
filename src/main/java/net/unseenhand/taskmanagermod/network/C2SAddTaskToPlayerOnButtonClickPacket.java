package net.unseenhand.taskmanagermod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;
import net.unseenhand.taskmanagermod.model.Task;

public class C2SAddTaskToPlayerOnButtonClickPacket implements SimplePacketBase {

    public static final String MESSAGE_FAILED_ADD_TASK = "Failed to add task to player";
    private String id;
    private String name;
    private String description;
    private byte status;

    public C2SAddTaskToPlayerOnButtonClickPacket() {
    }

    public C2SAddTaskToPlayerOnButtonClickPacket(String id, String name, String description, byte status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public C2SAddTaskToPlayerOnButtonClickPacket(Task task) {
        this(task.id(), task.name(), task.description(), task.status());
    }

    public C2SAddTaskToPlayerOnButtonClickPacket(FriendlyByteBuf buf) {
        id = buf.readUtf();
        name = buf.readUtf();
        description = buf.readUtf();
        status = buf.readByte();
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

        Task task = new Task(id, name, description, status);

        player.getCapability(PlayerTasksProvider.PLAYER_TASKS).ifPresent(playerTasks -> {
            playerTasks.addTask(task);
        });

        return true;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(name);
        buf.writeUtf(description);
        buf.writeByte(status);
    }
}
