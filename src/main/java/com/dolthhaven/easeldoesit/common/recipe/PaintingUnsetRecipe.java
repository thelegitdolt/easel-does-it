package com.dolthhaven.easeldoesit.common.recipe;

import com.dolthhaven.easeldoesit.core.registry.other.EaselModRecipeSerializers;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PaintingUnsetRecipe extends CustomRecipe {
    public PaintingUnsetRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        byte paintingCount = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (PaintingUtil.readStack(stack, level.registryAccess()).isEmpty()) {
                continue;
            }

            paintingCount++;
            if (paintingCount > 1) {
                return false;
            }
        }
        return paintingCount == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        return new ItemStack(Items.PAINTING);
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EaselModRecipeSerializers.PAINTING_UNSET.get();
    }
}
