package com.dolthhaven.easeldoesit.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VillagerStatueBlock extends Block {
    public static EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public VillagerStatueBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return null;
    }

    public @NotNull BlockState updateShape(BlockState state, Direction direction, @NotNull BlockState changedState, @NotNull LevelAccessor level, @NotNull BlockPos changedPos, @NotNull BlockPos p_52899_) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || changedState.is(this) && changedState.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(level, changedPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, changedState, level, changedPos, p_52899_);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, level, pos);
        } else {
            BlockState blockstate = level.getBlockState(pos.below());
            if (state.getBlock() != this) return super.canSurvive(state, level, pos);  //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
    }

    public void setPlacedBy(Level level, BlockPos p_52873_, @NotNull BlockState p_52874_, LivingEntity p_52875_, @NotNull ItemStack p_52876_) {
        BlockPos blockpos = p_52873_.above();
        level.setBlock(blockpos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3);
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
}
