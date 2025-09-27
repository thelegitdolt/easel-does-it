package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    @ModifyArg(method = "lambda$bootstrap$31",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTabs;generatePresetPaintings(Lnet/minecraft/world/item/CreativeModeTab$Output;Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/core/HolderLookup$RegistryLookup;Ljava/util/function/Predicate;Lnet/minecraft/world/item/CreativeModeTab$TabVisibility;)V"))
    private static Predicate<Holder<PaintingVariant>> noMoreTreasure(Predicate<Holder<PaintingVariant>> original, @Local(argsOnly = true) CreativeModeTab.ItemDisplayParameters parameters) {
        return original.and(holder -> !holder.is(EaselModTags.Paintings.TREASURE));
    }

    @ModifyArg(method = "lambda$bootstrap$8",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTabs;generatePresetPaintings(Lnet/minecraft/world/item/CreativeModeTab$Output;Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/core/HolderLookup$RegistryLookup;Ljava/util/function/Predicate;Lnet/minecraft/world/item/CreativeModeTab$TabVisibility;)V"))
    private static Predicate<Holder<PaintingVariant>> moreTreasure(Predicate<Holder<PaintingVariant>> original, @Local(argsOnly = true) CreativeModeTab.ItemDisplayParameters parameters) {
        return original.or(holder -> holder.is(EaselModTags.Paintings.TREASURE));
    }
}
