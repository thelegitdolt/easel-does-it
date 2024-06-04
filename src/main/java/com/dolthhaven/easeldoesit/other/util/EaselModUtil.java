package com.dolthhaven.easeldoesit.other.util;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
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

    public static ItemStack getStackFromPainting(PaintingVariant variant) {
        ItemStack paintingStack = new ItemStack(Items.PAINTING);
        CompoundTag tag = paintingStack.getOrCreateTagElement("EntityTag");
        Painting.storeVariant(tag, Holder.direct(variant));

        return paintingStack;
    }

    public static InteractionHand getOtherHand(InteractionHand hand) {
        return switch (hand) {
            case MAIN_HAND -> InteractionHand.OFF_HAND;
            case OFF_HAND -> InteractionHand.MAIN_HAND;
        };
    }

    public static List<PaintingVariant> getAllPaintingsOfDimensions(int width, int height) {
        return ForgeRegistries.PAINTING_VARIANTS.getValues().stream()
                .filter(painting -> painting.getHeight() == height && painting.getWidth() == width).toList();
    }
}
