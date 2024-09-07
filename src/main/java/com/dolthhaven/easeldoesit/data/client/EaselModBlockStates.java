package com.dolthhaven.easeldoesit.data.client;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

import static com.dolthhaven.easeldoesit.core.registry.EaselModBlocks.EASEL;

public class EaselModBlockStates extends BlueprintBlockStateProvider {
    private static final String ITEM_GENERATED = "item/generated";

    public EaselModBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EaselDoesIt.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.easel(EASEL, true);
    }

    private void easel(RegistryObject<? extends Block> easel, boolean doItem) {
        ModelFile model = new ModelFile.ExistingModelFile(modLoc("block/" + getName(easel)), this.models().existingFileHelper);

        if (doItem)
                this.blockItem(easel.get());

        this.variantHorizontalDirectionalModel(easel, model);
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
