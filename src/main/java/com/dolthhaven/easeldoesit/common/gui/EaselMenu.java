package com.dolthhaven.easeldoesit.common.gui;

import com.dolthhaven.easeldoesit.common.blocks.entity.EaselBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EaselMenu extends AbstractContainerMenu {
    public final EaselBlockEntity easelEntity;
    private final Level level;
    private final ContainerData data;

    public EaselMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData, @Nullable MenuType<?> menu, int id) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public EaselMenu(int containerId, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(, containerId);
        checkContainerSize(inventory, 2);
        this.easelEntity = (EaselBlockEntity) entity;
        this.level = inventory.player.level();
        this.data = data;
    }
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int id) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }
}
