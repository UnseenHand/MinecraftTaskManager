package net.unseenhand.taskmanagermod.codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.unseenhand.taskmanagermod.TaskManagerMod;
import net.unseenhand.taskmanagermod.data.PlayerTasks;

import java.util.function.Consumer;

public class Codecs {
//    public static final DeferredRegister<DataComponentType<?>> DR = DeferredRegister
//            .create(Registries.DATA_COMPONENT_TYPE, TaskManagerMod.MOD_ID);
//
//    public static final DataComponentType<PlayerTasks> PLAYER_TASKS = register(
//            "player_tasks",
//            builder -> builder.persistent(PlayerTasks.CODEC)
//                    .networkSynchronized(PlayerTasks.STREAM_CODEC));
//
//    private static <T> DataComponentType<T> register(String name, Consumer<DataComponentType.Builder<T>> customizer) {
//        var builder = DataComponentType.<T>builder();
//        customizer.accept(builder);
//        var componentType = builder.build();
//        DR.register(name, () -> componentType);
//        return componentType;
//    }
}
