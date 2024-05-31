package com.dolthhaven.easeldoesit.common.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EaselMenu extends AbstractContainerMenu {

    protected EaselMenu(@Nullable MenuType<?> menu, int id) {
        super(menu, id);
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
