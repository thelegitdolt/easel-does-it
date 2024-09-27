package com.dolthhaven.easeldoesit.other.util;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.other.EaselModConstants;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
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
                        if (ForgeRegistries.ITEMS.getValue(rl) == null) {
                            return EaselModConstants.dyeDepot(rl.getPath());
                        }
                        else return rl;
                    })
                    .map(ForgeRegistries.ITEMS::getValue).filter(Objects::nonNull).toList();
        }
    }
}
