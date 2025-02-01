package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.core.EaselModConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.teamabnormals.gallery.core.other.GalleryEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GalleryEvents.class)
public class GalleryModEventsMixin {
    @WrapWithCondition(method = "rightClickWithPainting", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"))
    private static boolean EaselDoesIt$CancelPaintingStuff(Player instance, MenuProvider p_36150_) {
        return !EaselModConfig.COMMON.galleryCompat.get();
    }
}
