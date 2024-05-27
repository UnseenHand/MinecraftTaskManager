package net.unseenhand.taskmanagermod.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTasksProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerTasks> PLAYER_TASKS = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerTasks playerTasks;
    private final LazyOptional<PlayerTasks> opt = LazyOptional.of(this::createPlayerTasks);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == PLAYER_TASKS) {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerTasks().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerTasks().loadNBTData(nbt);
    }

    private PlayerTasks createPlayerTasks() {
        if (playerTasks == null) {
            playerTasks = new PlayerTasks();
        }
        return playerTasks;
    }


}
