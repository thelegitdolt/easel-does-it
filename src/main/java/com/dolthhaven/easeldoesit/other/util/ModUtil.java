package com.dolthhaven.easeldoesit.other.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModUtil {
    public static List<Item> getAllMembersOfTag(TagKey<Item> tag) {
        return ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.builtInRegistryHolder().is(tag)).toList();
    }
}
