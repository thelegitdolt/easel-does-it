package com.dolthhaven.easeldoesit.common.blocks.entity;

import com.dolthhaven.easeldoesit.core.registry.EaselModBlockEntities;
import com.dolthhaven.easeldoesit.other.util.EaselModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EaselBlockEntity extends BlockEntity {
    private final ItemStackHandler ITEM_HANDLER = new ItemStackHandler(1);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    public static final String INVENTORY_COMPOUND_KEY = "inventory";

    public EaselBlockEntity(BlockPos pos, BlockState state) {
        super(EaselModBlockEntities.EASEL.get(), pos, state);
    }

    public @NotNull ItemStack getSavedPainting() {
        return ITEM_HANDLER.getStackInSlot(0);
    }

    public void setSavedPainting(ItemStack paintingStack) {
        if (paintingStack.is(Items.PAINTING)) {
            ITEM_HANDLER.setStackInSlot(0, paintingStack);
        }
    }

    public void setSavedPainting(PaintingVariant paintingVariant) {
        this.setSavedPainting(EaselModUtil.getStackFromPainting(paintingVariant));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> ITEM_HANDLER);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put(INVENTORY_COMPOUND_KEY, ITEM_HANDLER.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        ITEM_HANDLER.deserializeNBT(tag.getCompound(INVENTORY_COMPOUND_KEY));
    }
}
