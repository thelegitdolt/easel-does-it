package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EaselModRecipes extends RecipeProvider {
    public EaselModRecipes(GatherDataEvent event) {
        super(event.getGenerator());
    }

    @Override
    public void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(EaselModBlocks.EASEL.get())
                .define('_', ItemTags.WOODEN_SLABS)
                .define('/', Items.STICK)
                .define('0', ItemTags.PLANKS)
                .pattern(" 0")
                .pattern("0/")
                .pattern("__")
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING)).save(consumer);

        ShapedRecipeBuilder.shaped(EaselModBlocks.EASEL.get())
                .define('_', ItemTags.WOODEN_SLABS)
                .define('/', Items.STICK)
                .define('0', ItemTags.PLANKS)
                .pattern("0 ")
                .pattern("/0")
                .pattern("__")
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING)).save(consumer, EaselDoesIt.rl("easel_mirrored"));
    }
}
