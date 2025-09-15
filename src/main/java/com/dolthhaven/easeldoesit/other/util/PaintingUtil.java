package com.dolthhaven.easeldoesit.other.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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