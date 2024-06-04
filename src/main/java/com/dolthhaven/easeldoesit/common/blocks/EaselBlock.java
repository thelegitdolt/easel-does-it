package com.dolthhaven.easeldoesit.common.blocks;

import com.dolthhaven.easeldoesit.common.blocks.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.common.gui.EaselMenu;
import com.dolthhaven.easeldoesit.other.util.EaselModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

import static com.dolthhaven.easeldoesit.common.blocks.entity.EaselBlockEntity.CONTAINER_TITLE;

@SuppressWarnings("deprecation")
public class EaselBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("container.easel_does_it.easel");


    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty PAINTING = BooleanProperty.create("painting");
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);

    public EaselBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(PAINTING, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EaselBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER).setValue(PAINTING, false);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDef) {
        stateDef.add(WATERLOGGED, FACING, PAINTING);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult result) {
        if (level.getBlockEntity(pos) instanceof EaselBlockEntity easelEntity) {
            ItemStack stack = player.getItemInHand(hand);

            boolean toggledPainting = attemptTogglePainting(player, level, pos, hand, stack, easelEntity);
            if (toggledPainting) {
                return InteractionResult.SUCCESS;
            }

            if (!level.isClientSide()) {
                player.openMenu(state.getMenuProvider(level, pos));
                return InteractionResult.CONSUME;
            }

        }
        return InteractionResult.SUCCESS;
    }

    private static boolean attemptTogglePainting(Player player, Level level, BlockPos pos, InteractionHand hand, ItemStack heldStack, EaselBlockEntity easelEntity) {
        if (!player.isCrouching() && player.getItemInHand(EaselModUtil.getOtherHand(hand)).isEmpty()) {
            return false;
        }

        if (heldStack.is(Items.PAINTING)) {
            if (Objects.nonNull(easelEntity.getSavedPainting())) {
                return false;
            }
            level.playSound(player, pos, SoundEvents.PAINTING_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            heldStack.shrink(1);
            easelEntity.setSavedPainting(heldStack);
            return true;
        }
        else if (heldStack.isEmpty()) {
            if (Objects.isNull(easelEntity.getSavedPainting())) {
                return false;
            }
            level.playSound(player, pos, SoundEvents.PAINTING_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (!player.getInventory().add(easelEntity.getSavedPainting())) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), easelEntity.getSavedPainting());
            }
            easelEntity.setSavedPainting((ItemStack) null);
            return true;
        }

        return false;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((p_57074_, inventory, player) ->
                new EaselMenu(), CONTAINER_TITLE);
    }

}
