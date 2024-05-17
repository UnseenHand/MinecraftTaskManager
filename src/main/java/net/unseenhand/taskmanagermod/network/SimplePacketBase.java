package net.unseenhand.taskmanagermod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public interface SimplePacketBase {
    boolean handle(CustomPayloadEvent.Context context);
    void encode(FriendlyByteBuf buf);
}
