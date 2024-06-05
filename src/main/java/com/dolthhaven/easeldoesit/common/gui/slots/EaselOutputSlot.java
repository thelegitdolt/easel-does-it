package com.dolthhaven.easeldoesit.common.gui.slots;

import com.dolthhaven.easeldoesit.common.gui.EaselMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EaselOutputSlot extends Slot {
    private final EaselMenu easelMenu;
    public EaselOutputSlot(Container container, EaselMenu easelMenu, int id, int xOffset, int yOffset) {
        super(container, id, xOffset, yOffset);
        this.easelMenu = easelMenu;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        stack.onCraftedBy(player.level(), player, stack.getCount());
        this.easelMenu.getResultContainer().awardUsedRecipes(player, this.getRelevantItems());
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.easelMenu.getInputSlot().getItem());
    }
}
