package net.unseenhand.taskmanagermod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.unseenhand.taskmanagermod.TaskManagerMod;

public class PacketHandler {
    public static final String MAIN_CHANNEL = "main_channel";
    public static final int VERSION = 1;
    private static final SimpleChannel INSTANCE = ChannelBuilder.named(
                    new ResourceLocation(TaskManagerMod.MOD_ID, MAIN_CHANNEL))
            .serverAcceptedVersions((status, version) -> true)
            .clientAcceptedVersions(((status, version) -> true))
            .networkProtocolVersion(VERSION)
            .simpleChannel();

    public static void registerPackets() {
        INSTANCE.messageBuilder(S2COpenTMScreenFromKeyPressedPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2COpenTMScreenFromKeyPressedPacket::encode)
                .decoder(S2COpenTMScreenFromKeyPressedPacket::new)
                .consumerMainThread(S2COpenTMScreenFromKeyPressedPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SAddTaskToPlayerOnButtonClickPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SAddTaskToPlayerOnButtonClickPacket::encode)
                .decoder(C2SAddTaskToPlayerOnButtonClickPacket::new)
                .consumerMainThread(C2SAddTaskToPlayerOnButtonClickPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SRefreshTSLTasksOnScreenActionPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SRefreshTSLTasksOnScreenActionPacket::encode)
                .decoder(C2SRefreshTSLTasksOnScreenActionPacket::new)
                .consumerMainThread(C2SRefreshTSLTasksOnScreenActionPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SRemoveTaskToPlayerOnButtonClickPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SRemoveTaskToPlayerOnButtonClickPacket::encode)
                .decoder(C2SRemoveTaskToPlayerOnButtonClickPacket::new)
                .consumerMainThread(C2SRemoveTaskToPlayerOnButtonClickPacket::handle)
                .add();

        INSTANCE.messageBuilder(S2CRefreshTSLOnTickPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CRefreshTSLOnTickPacket::encode)
                .decoder(S2CRefreshTSLOnTickPacket::new)
                .consumerMainThread(S2CRefreshTSLOnTickPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SSwapEntryOnDraggingPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SSwapEntryOnDraggingPacket::encode)
                .decoder(C2SSwapEntryOnDraggingPacket::new)
                .consumerMainThread(C2SSwapEntryOnDraggingPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SSetFilterOnBtnClickPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SSetFilterOnBtnClickPacket::encode)
                .decoder(C2SSetFilterOnBtnClickPacket::new)
                .consumerMainThread(C2SSetFilterOnBtnClickPacket::handle)
                .add();

        INSTANCE.messageBuilder(S2CAddIgnoredTasksPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CAddIgnoredTasksPacket::encode)
                .decoder(S2CAddIgnoredTasksPacket::new)
                .consumerMainThread(S2CAddIgnoredTasksPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(msg, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToAllClients(Object msg) {
        INSTANCE.send(msg, PacketDistributor.ALL.noArg());
    }
}
