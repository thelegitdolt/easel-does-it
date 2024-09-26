package com.dolthhaven.easeldoesit.data.client;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.common.block.VillagerStatueBlock;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModItems;
import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.EASEL;
import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.STATUE;

@SuppressWarnings("unused")
public class EaselModBlockStates extends BlueprintBlockStateProvider {
    private static final String ITEM_GENERATED = "item/generated";

    public EaselModBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EaselDoesIt.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.easel(EASEL);
        this.doubleBlock(STATUE);
    }

    private void doubleBlock(RegistryObject<? extends Block> doubleBlock) {
        ModelFile topModel = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(doubleBlock) + "_top"), this.models().existingFileHelper);
        ModelFile bottomModel = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(doubleBlock) + "_bottom"), this.models().existingFileHelper);

        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(doubleBlock.get());

        builder.part().modelFile(topModel).addModel().condition(VillagerStatueBlock.HALF, DoubleBlockHalf.UPPER);
        builder.part().modelFile(bottomModel).addModel().condition(VillagerStatueBlock.HALF, DoubleBlockHalf.LOWER);

        this.itemModels().basicItem(EaselModItems.STATUE.get());
    }

    private void easel(RegistryObject<? extends Block> easel) {
        ModelFile model = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(easel)), this.models().existingFileHelper);
        ModelFile paintingModel = new ModelFile.ExistingModelFile(EaselDoesIt.rl("block/" + getName(easel) + "_painting"), this.models().existingFileHelper);

        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(easel.get());
//
//        VariantBlockStateBuilder builder = this.getVariantBuilder(easel.get());
//
//        for (Direction direction : Direction.Plane.HORIZONTAL) {
//            int rotation = (int) (direction.toYRot() + 180.0F) % 360;
//            builder.partialState().with(EaselBlock.FACING, direction).with(EaselBlock.HAS_PAINTING, true)
//                    .setModels(new ConfiguredModel(paintingModel)).
//        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int rotation = (int) (direction.toYRot() + 180.0F) % 360;
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

    private void variantHorizontalDirectionalModel(RegistryObject<? extends Block> mgBlock, ModelFile model) {
        this.getVariantBuilder(mgBlock.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build());
    }

    private String getName(Supplier<? extends ItemLike> object) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(object.get().asItem())).getPath();
    }
}
