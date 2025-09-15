package com.dolthhaven.easeldoesit.other.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
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

    public static Set<PaintingVariant> tagged(TagKey<PaintingVariant> tag, Level level, Predicate<PaintingVariant> predicate) {
        return predicate(painting -> holder(painting, level).is(tag) && predicate.test(painting), level);
    }

    public static Set<PaintingVariant> predicate(Predicate<PaintingVariant> variantPredicate, Level level) {
        Optional<Registry<PaintingVariant>> paintings = level.registryAccess().registry(Registries.PAINTING_VARIANT);
        return paintings
                .map(list -> list
                    .stream().filter(variantPredicate)
                    .collect(Collectors.toSet()))
                .orElseGet(Set::of);
    }

    public static Holder<PaintingVariant> holder(PaintingVariant painting, Level level) {
        Optional<Registry<PaintingVariant>> paintings = level.registryAccess().registry(Registries.PAINTING_VARIANT);
        return paintings.map(registry -> registry.getHolder(registry.getKey(painting))).orElseThrow().orElseThrow();
    }

    public static Optional<Holder<PaintingVariant>> fromLanguageKey(String key) {
        String[] keys = key.split("\\.");
        return ForgeRegistries.PAINTING_VARIANTS.getHolder(
                new ResourceLocation(keys[1], keys[2])
        );
    }

    public static ResourceLocation getPaintingLocation(PaintingVariant variant) {
        ResourceLocation loc = Objects.requireNonNull(ForgeRegistries.PAINTING_VARIANTS.getKey(variant));
        return new ResourceLocation(loc.getNamespace(), "textures/painting/" + loc.getPath() + ".png");
    }
}