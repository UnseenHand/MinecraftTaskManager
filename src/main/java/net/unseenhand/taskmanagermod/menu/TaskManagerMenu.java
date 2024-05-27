package net.unseenhand.taskmanagermod.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.unseenhand.taskmanagermod.block.ModBlocks;
import net.unseenhand.taskmanagermod.blockentity.TaskManagerMenuBlockEntity;
import org.jetbrains.annotations.NotNull;

public class TaskManagerMenu extends AbstractContainerMenu {
    private TaskManagerMenuBlockEntity blockEntity = null;
    private ContainerLevelAccess levelAccess = ContainerLevelAccess.NULL;

    // Client Constructor
    public TaskManagerMenu(int containerId, Inventory inventory, FriendlyByteBuf additionalData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(additionalData.readBlockPos()));
    }

    // Server Constructor
    public TaskManagerMenu(int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenus.TASK_MANAGER_MENU.get(), containerId);
        if (blockEntity instanceof TaskManagerMenuBlockEntity be) {
            this.blockEntity = be;
        } else {
            throw new IllegalStateException("Incorrect blockEntity class (%s) passed into TaskManagerMenu!"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        Level level = blockEntity.getLevel();

        if (level != null) {
            this.levelAccess = ContainerLevelAccess.create(level, blockEntity.getBlockPos());
        }

        createPlayerHotbar(inventory);
        createPlayerInventory(inventory);
        createBlockEntityInventory(be);
    }

    public TaskManagerMenu(Player player) {
        super(ModMenus.TASK_MANAGER_MENU.get(), 0);
    }

    private void createBlockEntityInventory(TaskManagerMenuBlockEntity be) {

    }

    private void createPlayerInventory(Inventory inventory) {

    }

    private void createPlayerHotbar(Inventory inventory) {

    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.levelAccess, pPlayer, ModBlocks.TASK_MANAGER_BLOCK.get());
    }
}
