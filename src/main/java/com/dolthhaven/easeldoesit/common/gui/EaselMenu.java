package com.dolthhaven.easeldoesit.common.gui;

import com.dolthhaven.easeldoesit.common.blocks.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.core.registry.EaselModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EaselMenu extends AbstractContainerMenu {
    public final EaselBlockEntity easelEntity;
    private final Level level;
    private final List<PaintingVariant> possiblePaintings = Lists.newArrayList();
    private int paintingHeight = 0;
    private int paintingWidth = 0;
    private ItemStack input;
    final Slot inputSlot;
    final Slot resultSlot;
    public final Container container = new SimpleContainer(1) {
        public void setChanged() {
            super.setChanged();
            EaselMenu.this.slotsChanged(this);
            EaselMenu.this.slotUpdateListener.run();
        }
    };
    final ResultContainer resultContainer;
    Runnable slotUpdateListener = () -> {};


    public EaselMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory.player.level().getBlockEntity(extraData.readBlockPos()), ContainerLevelAccess.NULL);
    }

    public EaselMenu(int containerId, Inventory inventory, BlockEntity blockEntity, final ContainerLevelAccess container) {
        super(EaselModMenuTypes.EASEL.get(), containerId);
        this.easelEntity = (EaselBlockEntity) blockEntity;
        this.level = inventory.player.level();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int id) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    public List<PaintingVariant> getPossiblePaintings() {
        return possiblePaintings;
    }

    public int getNumRecipes() {
        return possiblePaintings.size();
    }

    public void registerUpdateListener(Runnable updateListener) {
        this.slotUpdateListener = updateListener;
    }
}
