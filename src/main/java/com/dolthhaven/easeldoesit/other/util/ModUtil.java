package com.dolthhaven.easeldoesit.other.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ModUtil {
    public static List<Item> getAllMembersOfTag(TagKey<Item> tag) {
        return ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.builtInRegistryHolder().is(tag)).toList();
    }

    public static List<Item> getAllDyedItems(Function<String, ResourceLocation> thing) {
        return Arrays.stream(DyeColor.values()).map(DyeColor::getName)
                .map(name -> ForgeRegistries.ITEMS.getValue(thing.apply(name))).toList();
    }
}
