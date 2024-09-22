package com.dolthhaven.easeldoesit.common.block.entity;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EaselBlockEntity extends BlockEntity {
    private ItemStack painting = ItemStack.EMPTY;

    public ItemStack clearContent() {
        ItemStack paintingContent = this.painting.copy();
        if (!paintingContent.isEmpty()){
            onPaintingRemove();
        }

        this.setPainting(ItemStack.EMPTY);
        return paintingContent;
    }

    private boolean hasPainting() {
        return !this.painting.isEmpty();
    }

    public void setPainting(ItemStack stack) {
        this.painting = stack;
    }

    private void onPaintingRemove() {
        EaselBlock.togglePainting(null, this.worldPosition, this.getLevel(),  this.getBlockState(),false);
    }

    public EaselBlockEntity(BlockPos pos, BlockState state) {
        super(EaselModBlockEntities.EASEL_ENTITY.get(), pos, state);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!isEmpty()) {
            tag.put("Painting", this.getPainting().save(new CompoundTag()));
        }
    }
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Painting", 10)) {
            this.painting = ItemStack.of(tag.getCompound("Painting"));
        }
    }

    public ItemStack getPainting() {
        return this.painting;
    }

    public boolean isEmpty() {
        return this.painting.isEmpty();
    }
}
