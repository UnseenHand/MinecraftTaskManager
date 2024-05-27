package net.unseenhand.taskmanagermod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.data.PlayerTasks;
import net.unseenhand.taskmanagermod.data.PlayerTasksProvider;
import net.unseenhand.taskmanagermod.gui.screens.TaskManagerScreen;

@Mod.EventBusSubscriber(modid = TaskManagerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTasksEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(PlayerTasksProvider.PLAYER_TASKS).isPresent()) {
                event.addCapability(new ResourceLocation(TaskManagerMod.MOD_ID, "properties"),
                        new PlayerTasksProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            event.getOriginal().getCapability(PlayerTasksProvider.PLAYER_TASKS).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerTasksProvider.PLAYER_TASKS).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerTasks.class);
    }

    @SubscribeEvent
    public static void onPlayerTickServer(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient() || event.phase == TickEvent.Phase.START)
            return;

//        Player player = event.player;
//        Level level = player.level();
//
//        TaskManager manager = TaskManager.get(level);
//
//        if (manager == null)
//            return;
//
//        manager.tick(level);
    }

    @SubscribeEvent
    public static void onPlayerTickClient(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() || event.phase == TickEvent.Phase.START)
            return;


        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof TaskManagerScreen screen) {
            screen.tick();
        }
    }
}
