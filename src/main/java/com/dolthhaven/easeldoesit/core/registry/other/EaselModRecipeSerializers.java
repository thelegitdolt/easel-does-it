package com.dolthhaven.easeldoesit.core.registry.other;

import com.dolthhaven.easeldoesit.common.recipe.PaintingUnsetRecipe;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EaselModRecipeSerializers {
    public static net.neoforged.neoforge.registries.DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, EaselDoesIt.MOD_ID);

    public static Supplier<RecipeSerializer<PaintingUnsetRecipe>> PAINTING_UNSET = RECIPE_SERIALIZERS
            .register("painting_unset", () -> new SimpleCraftingRecipeSerializer<>(PaintingUnsetRecipe::new));
}
