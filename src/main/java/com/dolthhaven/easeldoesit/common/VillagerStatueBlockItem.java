//package com.dolthhaven.easeldoesit.common;
//
//import com.dolthhaven.easeldoesit.common.block.VillagerStatueBlock;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import org.jetbrains.annotations.NotNull;
//
//public class VillagerStatueBlockItem extends BlockItem {
//    public VillagerStatueBlockItem(Block block, Properties props) {
//        super(block, props);
//    }
//
//    @Override
//    protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
//        Level level = context.getLevel();
//        Direction dir = context.getHorizontalDirection();
//        BlockPos abovePos = context.getClickedPos().above();
//
//        if (state.hasProperty(VillagerStatueBlock.FACING)) {
//
//        }
//
//        return super.placeBlock(context, state);
//    }
//}
