package com.dolthhaven.easeldoesit.data.server;

import com.dolthhaven.easeldoesit.common.recipe.PaintingUnsetRecipe;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.core.registry.other.EaselModRecipeSerializers;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class EaselModRecipes extends BlueprintRecipeProvider {
    public EaselModRecipes(GatherDataEvent event) {
        super(EaselDoesIt.MOD_ID, event.getGenerator().getPackOutput(), event.getLookupProvider());
    }

    public void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EaselModBlocks.EASEL.get())
                .define('0', ItemTags.PLANKS)
                .pattern(" 0")
                .pattern("0 ")
                .pattern("00")
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING)).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EaselModBlocks.EASEL.get())
                .define('0', ItemTags.PLANKS)
                .pattern("0 ")
                .pattern(" 0")
                .pattern("00")
                .unlockedBy(getHasName(Items.PAINTING), has(Items.PAINTING)).save(output, EaselDoesIt.rl("easel_mirrored"));

        SpecialRecipeBuilder.special(PaintingUnsetRecipe::new).save(output, EaselDoesIt.rl("painting_variant_unset"));
    }
}
