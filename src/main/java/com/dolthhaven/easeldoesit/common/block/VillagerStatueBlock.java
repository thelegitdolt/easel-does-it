package com.dolthhaven.easeldoesit.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VillagerStatueBlock extends Block /* extends BaseEntityBlock */ {
    public static EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape TOP_SHAPE_NS;
    private static final VoxelShape TOP_SHAPE_WE;
    private static final VoxelShape BOTTOM_SHAPE_NS;
    private static final VoxelShape BOTTOM_SHAPE_WE;

    public VillagerStatueBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF).add(FACING);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return switch (state.getValue(FACING)) {
                case NORTH -> BOTTOM_SHAPE_NS;
                case SOUTH -> BOTTOM_SHAPE_NS;
                default -> BOTTOM_SHAPE_WE;
            };
        }
        else {
            return switch (state.getValue(FACING)) {
                case NORTH -> TOP_SHAPE_NS;
                case SOUTH -> TOP_SHAPE_NS;
                default -> TOP_SHAPE_WE;
            };
        }
    }


    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }

    public @NotNull BlockState updateShape(BlockState state, Direction direction, @NotNull BlockState changedState, @NotNull LevelAccessor level, @NotNull BlockPos changedPos, @NotNull BlockPos p_52899_) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || changedState.is(this) && changedState.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(level, changedPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, changedState, level, changedPos, p_52899_);
        }
        else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, level, pos);
        }
        else {
            BlockState blockstate = level.getBlockState(pos.below());
            if (state.getBlock() != this) return super.canSurvive(state, level, pos);  //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState aboveState = level.getBlockState(blockpos.above());

        if (!level.isOutsideBuildHeight(blockpos) && aboveState.canBeReplaced(context)) {
            BlockState placeState = super.getStateForPlacement(context);
            if (Objects.nonNull(placeState)) {
                return placeState.setValue(FACING, context.getHorizontalDirection().getOpposite());
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity entity, @NotNull ItemStack stack) {
        BlockPos blockpos = pos.above();
        Direction facing = state.getValue(FACING);
        level.setBlock(blockpos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER).setValue(FACING, facing), 3);
    }

    @Override
    public void playerWillDestroy(Level p_52878_, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (!p_52878_.isClientSide) {
            if (player.isCreative()) {
                preventCreativeDropFromBottomPart(p_52878_, pos, state, player);
            } else {
                dropResources(state, p_52878_, pos, null, player, player.getMainHandItem());
            }
        }

        super.playerWillDestroy(p_52878_, pos, state, player);
    }

    public void playerDestroy(@NotNull Level p_52865_, @NotNull Player p_52866_, @NotNull BlockPos p_52867_, @NotNull BlockState p_52868_, @javax.annotation.Nullable BlockEntity p_52869_, @NotNull ItemStack p_52870_) {
        super.playerDestroy(p_52865_, p_52866_, p_52867_, Blocks.AIR.defaultBlockState(), p_52869_, p_52870_);
    }

    protected static void preventCreativeDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState emptyState = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockpos, emptyState, 35);
                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
        }
    }

    static {
        BOTTOM_SHAPE_NS = Shapes.join(
                Block.box(1, 0, 1, 15, 1, 15),
                Block.box(4, 1, 6, 12, 16, 10),
                BooleanOp.OR
        );
        BOTTOM_SHAPE_WE = Shapes.join(
                Block.box(1, 0, 1, 15, 1, 15),
                Block.box(6, 1, 4, 10, 16, 12),
                BooleanOp.OR
        );

        TOP_SHAPE_NS = Shapes.join(
                Block.box(4, 10, 4, 12, 20, 12),
                Block.box(4, 0, 5, 12, 10, 11),
                BooleanOp.OR
        );
        TOP_SHAPE_WE = Shapes.join(
                Block.box(4, 10, 4, 12, 20, 12),
                Block.box(5, 0, 4, 11, 10, 12),
                BooleanOp.OR
        );
    }
}
