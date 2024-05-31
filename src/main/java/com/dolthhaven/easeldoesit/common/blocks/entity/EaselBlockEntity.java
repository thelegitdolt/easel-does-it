package com.dolthhaven.easeldoesit.common.blocks.entity;

import com.dolthhaven.easeldoesit.core.registry.EaselModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EaselBlockEntity extends BlockEntity implements MenuProvider {
    public static final String SAVED_PAINTING_TAG_KEY = "easelPainting";
    private static final Component CONTAINER_TITLE = Component.translatable("container.easel_does_it.easel");
    private @Nullable PaintingVariant savedPainting;
    protected final ContainerData data;
    private int width;
    private int height;

    public EaselBlockEntity(BlockPos pos, BlockState state) {
        super(EaselModBlockEntities.EASEL.get(), pos, state);
        this.width = 0;
        this.height = 0;
        this.data = new ContainerData() {
            @Override
            public int get(int id) {
                return switch (id) {
                    case 0 -> EaselBlockEntity.this.width;
                    case 1 -> EaselBlockEntity.this.height;
                    default -> -1;
                };
            }

            @Override
            public void set(int id, int after) {
                switch (id) {
                    case 0 -> EaselBlockEntity.this.width = after;
                    case 1 -> EaselBlockEntity.this.height = after;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        this.savedPainting = null;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return CONTAINER_TITLE;
    }

    public void setSavedPainting(@Nullable PaintingVariant newPainting) {
        this.savedPainting = newPainting;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        ResourceLocation painting = ForgeRegistries.PAINTING_VARIANTS.getKey(this.savedPainting);
        if (Objects.nonNull(painting)) {
            tag.putString(SAVED_PAINTING_TAG_KEY, painting.toString());
        }
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        ResourceLocation savedPainting = new ResourceLocation(tag.getString(SAVED_PAINTING_TAG_KEY));
        this.setSavedPainting(ForgeRegistries.PAINTING_VARIANTS.getValue(savedPainting));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return null;
    }
}
