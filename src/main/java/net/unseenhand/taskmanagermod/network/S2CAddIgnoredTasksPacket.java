package net.unseenhand.taskmanagermod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.unseenhand.taskmanagermod.gui.TaskSelectionList;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;

import java.util.List;

public class S2CAddIgnoredTasksPacket implements SimplePacketBase {
    private final List<String> filtered;

    public S2CAddIgnoredTasksPacket(List<String> filtered) {
        this.filtered = filtered;
    }

    public S2CAddIgnoredTasksPacket(FriendlyByteBuf buf) {
        filtered = buf.readList(FriendlyByteBuf::readUtf);
    }

    @Override
    public boolean handle(CustomPayloadEvent.Context context) {
        if (context.isServerSide())
            return false;

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof TaskManagerScreen screen) {
            TaskSelectionList selectionList = screen.getTaskSelectionList();
            selectionList.setIgnoredAllFrom(filtered);
        }

        return false;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(filtered, FriendlyByteBuf::writeUtf);
    }
}
