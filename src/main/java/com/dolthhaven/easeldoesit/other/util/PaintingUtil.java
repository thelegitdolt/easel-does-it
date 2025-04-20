package com.dolthhaven.easeldoesit.other.util;

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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class PaintingUtil {
    public static Optional<PaintingVariant> readStack(ItemStack stack) {
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

    public static ItemStack makeStack(Supplier<PaintingVariant> variant) {
        return makeStack(variant.get());
    }

    public static ItemStack makeStack(PaintingVariant variant) {
        ItemStack paintingStack = new ItemStack(Items.PAINTING, 1);

        CompoundTag tag = paintingStack.getOrCreateTagElement("EntityTag");
        Painting.storeVariant(tag, holder(variant));

        return paintingStack;
    }

    public static List<PaintingVariant> withTag(int width, int height) {
        return withTag(width, height, false);
    }

    public static List<PaintingVariant> withTag(int width, int height, boolean includeUnplaceable) {
        return ForgeRegistries.PAINTING_VARIANTS.getValues().stream()
                .filter(painting -> painting.getHeight() == height && painting.getWidth() == width)
                .filter(painting -> includeUnplaceable || holder(painting).is(PaintingVariantTags.PLACEABLE))
                .toList();
    }

    public static Set<ItemStack> withTag(TagKey<PaintingVariant> tag) {
        return ForgeRegistries.PAINTING_VARIANTS.getValues().stream()
                .map(PaintingUtil::holder)
                .filter(h -> h.is(tag))
                .map(Holder::value)
                .map(PaintingUtil::makeStack)
                .collect(Collectors.toSet());
    }

    public static Holder<PaintingVariant> holder(PaintingVariant painting) {
        return ForgeRegistries.PAINTING_VARIANTS.getHolder(painting).orElseThrow();
    }

    public static Optional<Holder<PaintingVariant>> fromLanguageKey(String key) {
        String[] keys = key.split("\\.");
        return ForgeRegistries.PAINTING_VARIANTS.getHolder(
                new ResourceLocation(keys[1], keys[2])
        );
    }

    public static PaintingVariant getPainting(ResourceLocation location) {
        return ForgeRegistries.PAINTING_VARIANTS.getValue(location);
    }
}