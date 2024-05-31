package com.dolthhaven.easeldoesit.other.util;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class EaselModUtil {
    public static Optional<PaintingVariant> getPaintingFromStack(ItemStack stack) {
        if (!stack.is(Items.PAINTING)) return Optional.empty();

        CompoundTag tag = stack.getTag();
        if (tag == null) return Optional.empty();

        Optional<Holder<PaintingVariant>> painting = Painting.loadVariant(tag.getCompound("EntityTag"));

        if (painting.isPresent()) {
            return Optional.of(painting.orElseThrow().get());
        }
        else {
            return Optional.empty();
        }
    }
}
