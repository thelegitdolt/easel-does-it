package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(HangingEntityItem.class)
public class HangingEntityItemMixin {
    @Shadow @Final private EntityType<? extends HangingEntity> type;

    @Inject(method = "useOn", locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/entity/EntityType;updateCustomEntityTag(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/nbt/CompoundTag;)V"))
    private void EaselDoesIt$AssignPaintingDropThemselves(UseOnContext p_41331_, CallbackInfoReturnable<InteractionResult> cir, BlockPos blockpos, Direction direction, BlockPos blockpos1, Player player, ItemStack hangingStack, Level level, HangingEntity hangingentity, CompoundTag compoundtag) {
        if (this.type != EntityType.PAINTING)
            return;

        IDataManager painting = (IDataManager) hangingentity;

        if (compoundtag.contains("EntityTag", 10)) {
            EaselDoesIt.log("it's running and shit so");
            painting.setValue(EaselModTrackedData.PAINTING_SHOULD_DROP_SELF, true);
        }

        EaselDoesIt.log(((IDataManager) (hangingentity)).getValue(EaselModTrackedData.PAINTING_SHOULD_DROP_SELF) + " xd painting sex");
    }
}
