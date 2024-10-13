package com.dolthhaven.easeldoesit.other.util;

import com.dolthhaven.easeldoesit.core.other.EaselModConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ModUtil {
    public static List<Item> getAllMembersOfTag(TagKey<Item> tag) {
        return ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.builtInRegistryHolder().is(tag)).toList();
    }

    public static List<Item> getAllDyedItems(Function<String, ResourceLocation> thing) {
        if (!ModList.get().isLoaded(EaselModConstants.DYE_DEPOT)) {
            return Arrays.stream(DyeColor.values())
                    .map(dye -> ForgeRegistries.ITEMS.getValue(thing.apply(dye.getName()))).toList();
        }
        else {
            return Arrays.stream(DyeColor.values()).map(dye -> thing.apply(dye.getName()))
                    .map(rl -> {
                        if (ForgeRegistries.ITEMS.getValue(rl) == Items.AIR) {
                            return EaselModConstants.dyeDepot(rl.getPath());
                        }
                        else return rl;
                    })
                    .map(ForgeRegistries.ITEMS::getValue).filter(item -> item != Items.AIR).toList();
        }
    }
}
