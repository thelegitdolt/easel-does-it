package com.dolthhaven.easeldoesit.other.util;

import com.dolthhaven.easeldoesit.core.other.EaselModConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModUtil {
    public static List<Item> getAllMembersOfTag(TagKey<Item> tag) {
        return BuiltInRegistries.ITEM.entrySet().stream().map(Map.Entry::getValue).filter(item ->
                item.builtInRegistryHolder().is(tag)).toList();
    }

    public static List<Item> getAllDyedItems(Function<String, ResourceLocation> thing) {
        if (!ModList.get().isLoaded(EaselModConstants.DYE_DEPOT)) {
            return Arrays.stream(DyeColor.values())
                    .map(dye -> BuiltInRegistries.ITEM.get(thing.apply(dye.getName()))).toList();
        }
        else {
            return Arrays.stream(DyeColor.values()).map(dye -> thing.apply(dye.getName()))
                    .map(ModUtil::tryModIds)
                    .filter(item -> item != Items.AIR).toList();
        }
    }

    private static Item tryModIds(ResourceLocation location) {
        for (String i : new String[]{"minecraft", EaselModConstants.DYE_DEPOT, "dye_the_world"}) {
            location = ResourceLocation.fromNamespaceAndPath(i, location.getPath());
            Item item = BuiltInRegistries.ITEM.get(location);
            if (item != Items.AIR) {
                return item;
            }
        }

        return Items.AIR;
    }
}
