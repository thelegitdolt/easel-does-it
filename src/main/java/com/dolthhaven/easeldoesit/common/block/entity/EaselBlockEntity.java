package com.dolthhaven.easeldoesit.common.block.entity;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!isEmpty()) {
            tag.put("painting", this.getPainting().save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("painting", 10)) {
            setPainting(ItemStack.parse(registries, tag.get("painting")).orElse(ItemStack.EMPTY));
        }
    }

    public ItemStack getPainting() {
        return this.painting;
    }

    public boolean isEmpty() {
        return this.painting.isEmpty();
    }
}
