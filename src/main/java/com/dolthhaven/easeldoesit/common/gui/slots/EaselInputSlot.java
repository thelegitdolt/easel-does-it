package com.dolthhaven.easeldoesit.common.gui.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class EaselInputSlot extends Slot {
    public EaselInputSlot(Container container, int id, int xOffset, int yOffset) {
        super(container, id, xOffset, yOffset);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(Items.PAINTING);
    }
}
