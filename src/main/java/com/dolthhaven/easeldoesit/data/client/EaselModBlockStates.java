package com.dolthhaven.easeldoesit.data.client;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.common.block.VillagerStatueBlock;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Objects;
import java.util.function.Supplier;

import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.EASEL;

@SuppressWarnings("unused")
public class EaselModBlockStates extends BlueprintBlockStateProvider {
    private static final String ITEM_GENERATED = "item/generated";

    public EaselModBlockStates(GatherDataEvent e) {
        super(e.getGenerator().getPackOutput(), EaselDoesIt.MOD_ID, e.getExistingFileHelper());
    }

    @Override
    protected void registerStatesAndModels() {
        this.easel(EASEL);
//        this.doubleBlock(STATUE);
    }

    private void doubleBlock(DeferredBlock<? extends Block> doubleBlock) {
        ModelFile topModel = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(doubleBlock) + "_top"), this.models().existingFileHelper);
        ModelFile bottomModel = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(doubleBlock) + "_bottom"), this.models().existingFileHelper);

        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(doubleBlock.get());

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int rotation = (int) (direction.toYRot() + 180) % 360;

            builder.part().modelFile(topModel).rotationY(rotation).addModel().condition(VillagerStatueBlock.HALF, DoubleBlockHalf.UPPER)
                    .condition(VillagerStatueBlock.FACING, direction);
            builder.part().modelFile(bottomModel).rotationY(rotation).addModel().condition(VillagerStatueBlock.HALF, DoubleBlockHalf.LOWER)
                    .condition(VillagerStatueBlock.FACING, direction);
        }
    }

    private void easel(DeferredBlock<? extends Block> easel) {
        ModelFile model = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(easel)), this.models().existingFileHelper);
        ModelFile paintingModel = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(easel) + "_painting"), this.models().existingFileHelper);

        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(easel.get());

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int rotation = (int) (direction.toYRot() + 180) % 360;
            builder.part().modelFile(model).rotationY(rotation).addModel()
                    .condition(HorizontalDirectionalBlock.FACING, direction).condition(EaselBlock.HAS_PAINTING, false);

            builder.part().modelFile(paintingModel).rotationY(rotation).addModel()
                    .condition(HorizontalDirectionalBlock.FACING, direction).condition(EaselBlock.HAS_PAINTING, true);
        }

        this.blockItem(easel.get());

    }

    private ConfiguredModel existingModel(String nameSpace) {
        return new ConfiguredModel(new ModelFile.ExistingModelFile(EaselDoesIt.rl(nameSpace), this.models().existingFileHelper));
    }

    private void variantHorizontalDirectionalModel(DeferredBlock<? extends Block> mgBlock, ModelFile model) {
        this.getVariantBuilder(mgBlock.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build());
    }

    private String getName(Supplier<? extends ItemLike> object) {
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(object.get().asItem())).getPath();
    }
}
