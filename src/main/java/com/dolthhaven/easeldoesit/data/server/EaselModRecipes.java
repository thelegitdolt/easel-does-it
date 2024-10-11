package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.function.Consumer;

public class EaselModRecipes extends BlueprintRecipeProvider {
    public EaselModRecipes(GatherDataEvent event) {
        super(EaselDoesIt.MOD_ID, event.getGenerator().getPackOutput());
    }

    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EaselModBlocks.EASEL.get())
                .define('_', ItemTags.WOODEN_SLABS)
                .define('/', Items.STICK)
                .define('0', ItemTags.PLANKS)
                .pattern(" 0")
                .pattern("0/")
                .pattern("__")
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EaselModBlocks.EASEL.get())
                .define('_', ItemTags.WOODEN_SLABS)
                .define('/', Items.STICK)
                .define('0', ItemTags.PLANKS)
                .pattern("0 ")
                .pattern("/0")
                .pattern("__")
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING)).save(consumer, EaselDoesIt.rl("easel_mirrored"));
    }
}
