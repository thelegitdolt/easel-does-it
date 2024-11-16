package com.dolthhaven.easeldoesit.core.registry.other;

import com.dolthhaven.easeldoesit.common.recipe.PaintingUnsetRecipe;
import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EaselModRecipeSerializers {
    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EaselDoesIt.MOD_ID);

    public static RegistryObject<RecipeSerializer<PaintingUnsetRecipe>> PAINTING_UNSET = RECIPE_SERIALIZERS
            .register("painting_unset", () -> new SimpleCraftingRecipeSerializer<>(PaintingUnsetRecipe::new));
}
