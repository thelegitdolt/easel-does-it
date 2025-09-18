package com.dolthhaven.easeldoesit.other.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class PaintingUtil {
    public static Optional<PaintingVariant> readStack(ItemStack stack, RegistryAccess access) {
        if (!stack.is(Items.PAINTING)) return Optional.empty();

        return Optional.ofNullable(access.registry(Registries.PAINTING_VARIANT).orElseThrow().get(PaintingVariants.ALBAN));
//        CustomData data = stack.get(DataComponents.ENTITY_DATA);
//        data.read
    }

    public static ItemStack makeStack(ResourceLocation location, RegistryAccess access) {
        return makeStack(access.registry(Registries.PAINTING_VARIANT).orElseThrow().get(location), access);
    }

    public static ItemStack makeStack(PaintingVariant variant, RegistryAccess access) {
        ItemStack paintingStack = new ItemStack(Items.PAINTING, 1);
        CustomData data = CustomData.EMPTY.update(RegistryOps.create(NbtOps.INSTANCE, access), Painting.VARIANT_MAP_CODEC, holder(variant, access))
                .getOrThrow()
                .update(tag -> tag.putString("id", "minecraft:painting"));
        paintingStack.set(DataComponents.ENTITY_DATA, data);

        return paintingStack;
    }


    public static Set<PaintingVariant> tagged(TagKey<PaintingVariant> tag, RegistryAccess access, Predicate<PaintingVariant> predicate) {
        Set<PaintingVariant> variants = new HashSet<>();
        access.lookup(Registries.PAINTING_VARIANT).orElseThrow().get(PaintingVariantTags.PLACEABLE)
                .ifPresent(paintings -> paintings.forEach(painting -> {
                    PaintingVariant variant = painting.value();
                    if (predicate.test(variant)) {
                        variants.add(painting.value());
                    }
                }));
        return variants;
    }

    public static Holder<PaintingVariant> holder(PaintingVariant painting, Level level) {
        return holder(painting, level.registryAccess());
    }

    public static Holder<PaintingVariant> holder(PaintingVariant painting, RegistryAccess access) {
        Optional<Registry<PaintingVariant>> paintings = access.registry(Registries.PAINTING_VARIANT);
        return paintings.map(registry -> registry.getHolder(registry.getKey(painting))).orElseThrow().orElseThrow();
    }

//    public static Optional<Holder<PaintingVariant>> fromLanguageKey(String key) {
//        String[] keys = key.split("\\.");
//        return ForgeRegistries.PAINTING_VARIANTS.getHolder(
//                new ResourceLocation(keys[1], keys[2])
//        );
//    }
}