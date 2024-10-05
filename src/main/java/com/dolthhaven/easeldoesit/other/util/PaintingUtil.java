package com.dolthhaven.easeldoesit.other.util;

import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class PaintingUtil {
    public static Optional<PaintingVariant> readPresetVariant(ItemStack stack) {
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

    public static ItemStack createPresetVariantPaintingStack(Supplier<PaintingVariant> variant) {
        return createPresetVariantPaintingStack(variant.get());
    }

    public static ItemStack createPresetVariantPaintingStack(PaintingVariant variant) {
        ItemStack paintingStack = new ItemStack(Items.PAINTING, 1);

        CompoundTag tag = paintingStack.getOrCreateTagElement("EntityTag");
        Painting.storeVariant(tag, getHolder(variant));

        return paintingStack;
    }

    public static List<PaintingVariant> getAllPaintingsOfDimensions(int width, int height) {
        return getAllPaintingsOfDimensions(width, height, false);
    }

    public static List<PaintingVariant> getAllPaintingsOfDimensions(int width, int height, boolean includeUnplaceable) {
        return ForgeRegistries.PAINTING_VARIANTS.getValues().stream()
                .filter(painting -> painting.getHeight() == height && painting.getWidth() == width)
                .filter(painting -> includeUnplaceable || getHolder(painting).is(PaintingVariantTags.PLACEABLE))
                .toList();
    }

    public static Set<ItemStack> getAllPaintingsOfTag(TagKey<PaintingVariant> tag) {
        return ForgeRegistries.PAINTING_VARIANTS.getValues().stream()
                .map(PaintingUtil::getHolder)
                .filter(h -> h.is(tag))
                .map(Holder::value)
                .map(PaintingUtil::createPresetVariantPaintingStack)
                .collect(Collectors.toSet());
    }

    public static Holder<PaintingVariant> getHolder(PaintingVariant painting) {
        return ForgeRegistries.PAINTING_VARIANTS.getHolder(painting).orElseThrow();
    }

    public static Optional<Holder<PaintingVariant>> fromLanguageKey(String key) {
        String[] keys = key.split("\\.");
        return ForgeRegistries.PAINTING_VARIANTS.getHolder(
                new ResourceLocation(keys[1], keys[2])
        );
    }
}