package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.common.block.EaselBlock;
import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.core.registry.EaselModBlocks;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(HangingEntityItem.class)
public abstract class HangingEntityItemMixin {
    @Shadow @Final private EntityType<? extends HangingEntity> type;

    @WrapOperation(method = "lambda$appendHoverText$0(Ljava/util/List;Lnet/minecraft/resources/ResourceKey;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;"))
    private static MutableComponent EaselDoesIt$TreasurePaintingVariantsHaveBlueText(MutableComponent instance, ChatFormatting formatting, Operation<MutableComponent> original) {
        if (instance.getContents() instanceof TranslatableContents contents && contents.getKey().contains("title")) {
            Optional<Holder<PaintingVariant>> variantMaybe = PaintingUtil.fromLanguageKey(contents.getKey());

            if (variantMaybe.isEmpty() || !variantMaybe.get().is(EaselModTags.Paintings.TREASURE)) {
                return original.call(instance, formatting);
            }

            else {
                return instance.withStyle(ChatFormatting.AQUA);
            }
        }

        return original.call(instance, formatting);
    }


    @Inject(method = "useOn", locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/entity/EntityType;updateCustomEntityTag(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/nbt/CompoundTag;)V"))
    private void EaselDoesIt$AssignPaintingDropThemselves(UseOnContext p_41331_, CallbackInfoReturnable<InteractionResult> cir, BlockPos blockpos, Direction direction, BlockPos blockpos1, Player player, ItemStack hangingStack, Level level, HangingEntity hangingentity, CompoundTag compoundtag) {
        if (this.type != EntityType.PAINTING)
            return;

        IDataManager painting = (IDataManager) hangingentity;

        if (compoundtag.contains("EntityTag", 10)) {
            painting.setValue(EaselModTrackedData.PAINTING_SHOULD_DROP_SELF, true);
        }
    }

    @Inject(method = "useOn", locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/entity/EntityType;updateCustomEntityTag(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/nbt/CompoundTag;)V"))
    private void EaselDoesIt$SyncClientStuff(UseOnContext p_41331_, CallbackInfoReturnable<InteractionResult> cir, BlockPos blockpos, Direction direction, BlockPos blockpos1, Player player, ItemStack hangingStack, Level level, HangingEntity hangingentity, CompoundTag compoundtag) {
        if (this.type != EntityType.PAINTING)
            return;

        if (level.isClientSide) {
            ((Painting) hangingentity).setVariant(PaintingUtil.getHolder(PaintingUtil.readPresetVariant(hangingStack).orElseThrow()));
        }
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void EaselDoesIt$UsePaintingOnEasel(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        if (player == null) return;
        if (this.type != EntityType.PAINTING || !player.isShiftKeyDown()) return;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        if (state.is(EaselModBlocks.EASEL.get())) {
            if (EaselBlock.tryPlacePainting(player, level, pos, state, stack)) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
