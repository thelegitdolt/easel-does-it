package com.dolthhaven.easeldoesit.common.gui;

import com.dolthhaven.easeldoesit.common.blocks.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.common.gui.slots.EaselInputSlot;
import com.dolthhaven.easeldoesit.common.gui.slots.EaselOutputSlot;
import com.dolthhaven.easeldoesit.core.registry.EaselModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EaselMenu extends AbstractContainerMenu {
    public final EaselBlockEntity easelEntity;
    private final Level level;
    private @Nullable PaintingVariant currentPainting;
    private final List<PaintingVariant> possiblePaintings;
    private final DataSlot selectedPaintingIndex;
    private int paintingHeight;
    private int paintingWidth;
    private ItemStack input;
    final Slot inputSlot;
    final Slot resultSlot;
    public final Container container;
    final ResultContainer resultContainer;
    Runnable slotUpdateListener = () -> {};


    public EaselMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), ContainerLevelAccess.NULL);
    }

    public EaselMenu(int containerId, Inventory inventory, BlockEntity blockEntity, final ContainerLevelAccess container) {
        super(EaselModMenuTypes.EASEL.get(), containerId);
        this.easelEntity = (EaselBlockEntity) blockEntity;
        this.level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        this.possiblePaintings = Lists.newArrayList();
        this.selectedPaintingIndex = DataSlot.standalone();

        this.resultContainer = new ResultContainer();
        this.currentPainting = null;
        this.paintingWidth = 0;
        this.paintingHeight = 0;

        this.container = new SimpleContainer(1) {
            public void setChanged() {
                super.setChanged();
                EaselMenu.this.slotsChanged(this);
                EaselMenu.this.slotUpdateListener.run();
            }
        };

        this.input = ItemStack.EMPTY;
        this.inputSlot = this.addSlot(new EaselInputSlot(this.container, 0, 15, 35));
        this.resultSlot = this.addSlot(new EaselOutputSlot(this.container, this, 1, 143, 35));

        this.addDataSlot(this.selectedPaintingIndex);
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

    public int getNumPaintings() {
        return possiblePaintings.size();
    }

    public void registerUpdateListener(Runnable updateListener) {
        this.slotUpdateListener = updateListener;
    }

    // Thanks Kaupenjoe
    private void addPlayerInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private boolean validRecipeIndex(int index) {
        return index >= 0 && index < getNumPaintings();
    }

    // Yay
    private void addPlayerHotbar(Inventory inventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    public int getSelectedPaintingIndex() {
        return this.selectedPaintingIndex.get();
    }

    // Boilerplate
    public int getPaintingHeight(int height) {
        return this.paintingHeight;
    }

    public int getPaintingWidth(int width) {
        return this.paintingWidth;
    }

    public void setPaintingHeight(int height) {
        if (height > 4 || height < 0) {
            throw new IllegalArgumentException("Current painting cannot be smaller than 0 or larger than 4");
        }
        this.paintingHeight = height;
    }

    public void setPaintingWidth(int width) {
        if (width > 4 || width < 0) {
            throw new IllegalArgumentException("Current painting cannot be smaller than 0 or larger than 4");
        }
        this.paintingWidth = width;
    }

    public Level getLevel() {
        return level;
    }

    public @Nullable PaintingVariant getCurrentPainting() {
        return this.currentPainting;
    }

    public void setCurrentPainting(@Nullable PaintingVariant painting) {
        this.currentPainting = painting;
    }

    public ResultContainer getResultContainer() {
        return this.resultContainer;
    }

    public Slot getInputSlot() {
        return this.inputSlot;
    }
}
