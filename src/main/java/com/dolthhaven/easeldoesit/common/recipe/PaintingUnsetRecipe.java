package com.dolthhaven.easeldoesit.common.recipe;

import com.dolthhaven.easeldoesit.core.registry.other.EaselModRecipeSerializers;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PaintingUnsetRecipe extends CustomRecipe {
    public PaintingUnsetRecipe(ResourceLocation loc, CraftingBookCategory category) {
        super(loc, category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
        byte paintingCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (PaintingUtil.readPresetVariant(stack).isEmpty()) {
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
    public @NotNull ItemStack assemble(@NotNull CraftingContainer craftingContainer, @NotNull RegistryAccess registryAccess) {
        return new ItemStack(Items.PAINTING);
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return EaselModRecipeSerializers.PAINTING_UNSET.get();
    }
}
