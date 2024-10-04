package com.dolthhaven.easeldoesit.common.block;

import com.dolthhaven.easeldoesit.common.block.entity.EaselBlockEntity;
import com.dolthhaven.easeldoesit.common.inventory.EaselMenu;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.other.util.MathUtil;
import com.dolthhaven.easeldoesit.other.util.ModUtil;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class EaselBlock extends BaseEntityBlock {
    // https://www.youtube.com/watch?v=O2DdUAP-7yk
    // https://www.youtube.com/watch?v=uV81DhuZ96w
    public static final Component CONTAINER_TITLE = Component.translatable("container."  + EaselDoesIt.MOD_ID + ".easel");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_PAINTING = BooleanProperty.create("painting");

    private static final VoxelShape SHAPE_NORTH;
    private static final VoxelShape SHAPE_SOUTH;
    private static final VoxelShape SHAPE_EAST;
    private static final VoxelShape SHAPE_WEST;

    public EaselBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(HAS_PAINTING, false));
    }

    @Nullable
    public MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) ->
                new EaselMenu(id, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
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
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState superState = super.getStateForPlacement(context);
        if (Objects.isNull(superState))
            return null;

        return superState.setValue(HAS_PAINTING, false).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING).add(HAS_PAINTING);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (tryTakePainting(player, hand, level, pos)) {
           return InteractionResult.SUCCESS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        else {
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
            return InteractionResult.CONSUME;
        }
    }

    public static boolean tryTakePainting(Player player, InteractionHand hand, Level level, BlockPos pos) {
        if (player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof EaselBlockEntity easel && !easel.isEmpty()) {
                ItemStack paintingStack = easel.clearContent();

                if (!player.getInventory().add(paintingStack))
                    player.drop(paintingStack, false);

                level.playSound(null, pos, SoundEvents.PAINTING_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                return true;
            }
        }
        return false;
    }

    public static boolean tryPlacePainting(@Nullable Entity entity, Level level, BlockPos pos, BlockState state, ItemStack stack) {
        if (!state.getValue(HAS_PAINTING)) {
            if (!level.isClientSide) {
                placePainting(entity, level, pos, state, stack);
            }

            return true;
        }
        return false;
    }

    private static void placePainting(@Nullable Entity entity, Level level, BlockPos pos, BlockState state, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EaselBlockEntity easel) {
            easel.setPainting(stack.split(1));
            level.playSound(null, pos, SoundEvents.PAINTING_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            togglePainting(entity, pos, level, state, true);
        }
    }

    public static void togglePainting(Entity entity, BlockPos pos, Level level, BlockState state, boolean painting) {
        BlockState newState = state.setValue(HAS_PAINTING, painting);
        level.setBlock(pos, newState, 1);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, state));
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (!level.isClientSide && !player.isCreative())
            popPainting(state, level, pos);
    }

    private void popPainting(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof EaselBlockEntity easelEntity) {
            Direction direction = state.getValue(FACING);
            ItemStack itemstack = easelEntity.getPainting().copy();
            float f = 0.25F * (float) direction.getStepX();
            float f1 = 0.25F * (float) direction.getStepZ();
            ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + 0.5D + (double)f, pos.getY() + 1, (double)pos.getZ() + 0.5D + (double)f1, itemstack);
            itementity.setDefaultPickUpDelay();
            level.addFreshEntity(itementity);
            easelEntity.clearContent();
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof EaselBlockEntity easel) {
            ItemStack stack = easel.getPainting();
            if (stack.isEmpty()) return 0;
            Optional<PaintingVariant> maybeVariant = PaintingUtil.getPaintingFromStack(stack);

            if (maybeVariant.isEmpty()) return 15;
            else {
                PaintingVariant variant = maybeVariant.get();
                return Math.min(15, MathUtil.base4ExceptTheNumbersAre1234InsteadOf0123(
                    variant.getWidth() / 16,
                    variant.getHeight() / 16
                ));
            }
        }
        else {
            return 0;
        }
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EaselBlockEntity(pos, state);
    }

    static {
        SHAPE_NORTH = Shapes.joinUnoptimized(
                Block.box(0, 0, 0, 16, 2, 16),
                Block.box(1, 2, 2, 15, 15, 16),
                BooleanOp.OR
        );
        SHAPE_SOUTH = Shapes.joinUnoptimized(
                Block.box(0, 0, 0, 16, 2, 16),
                Block.box(1, 2, 0, 15, 15, 14),
                BooleanOp.OR
        );
        SHAPE_EAST = Shapes.joinUnoptimized(
                Block.box(0, 0, 0, 16, 2, 16),
                Block.box(0, 2, 1, 14, 15, 15),
                BooleanOp.OR
        );
        SHAPE_WEST = Shapes.joinUnoptimized(
                Block.box(0, 0, 0, 16, 2, 16),
                Block.box(2, 2, 1, 16, 15, 15),
                BooleanOp.OR
        );
    }
}
