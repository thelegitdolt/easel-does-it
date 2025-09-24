package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(HangingEntityItem.class)
public abstract class HangingEntityItemMixin {
    @Unique
    private static ThreadLocal<HolderLookup.Provider> access = new ThreadLocal<>();

    @Inject(method = "appendHoverText", at = @At(value = "HEAD"))
    private void EaselDoesIt$RegistryAccessAccessor(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        access.set(context.registries());
    }

    @ModifyArg(method = "lambda$appendHoverText$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    private static ChatFormatting EaselDoesIt$TreasurePaintingsHaveBlueText(ChatFormatting original, @Local(argsOnly=true) ResourceKey<PaintingVariant> key) {
        return PaintingUtil.isTagged(key, EaselModTags.Paintings.TREASURE, access.get()) ? ChatFormatting.AQUA : original;
    }


    @WrapOperation(method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/Painting;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Ljava/util/Optional;"))
    private Optional<Painting> EaselDoesIt$AssignPaintingDropThemselves(Level level, BlockPos pos, Direction dir, Operation<Optional<Painting>> original, @Local ItemStack playerStack) {
        Optional<Painting> painting = original.call(level, pos, dir);
        if (PaintingUtil.readStack(playerStack, level.registryAccess()).isPresent()) {
            painting.ifPresent(paint -> ((IDataManager) paint).setValue(EaselModTrackedData.PAINTING_SHOULD_DROP_SELF, true));
        }
        access.remove();
        return painting;
    }

//    @Inject(method = "useOn", locals = LocalCapture.CAPTURE_FAILSOFT,
//            at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/entity/EntityType;updateCustomEntityTag(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/nbt/CompoundTag;)V"))
//    private void EaselDoesIt$SyncClientStuff(UseOnContext p_41331_, CallbackInfoReturnable<InteractionResult> cir, BlockPos blockpos, Direction direction, BlockPos blockpos1, Player player, ItemStack hangingStack, Level level, HangingEntity hangingentity, CompoundTag compoundtag) {
//        if (this.type != EntityType.PAINTING)
//            return;
//
//        if (level.isClientSide) {
//            Optional<PaintingVariant> variant = readStack(hangingStack);
//            variant.ifPresent(paintingVariant -> ((Painting) hangingentity).setVariant(holder(paintingVariant)));
//        }
//    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void EaselDoesIt$UsePaintingOnEasel(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
//        Player player = context.getPlayer();
//        if (player == null) return;
//        if (this.type != EntityType.PAINTING || !player.isShiftKeyDown()) return;
//
//        Level level = context.getLevel();
//        BlockPos pos = context.getClickedPos();
//        BlockState state = level.getBlockState(pos);
//        ItemStack stack = context.getItemInHand();
//        if (state.is(EaselModBlocks.EASEL.get())) {
//            if (EaselBlock.tryPlacePainting(player, level, pos, state, stack)) {
//                cir.setReturnValue(InteractionResult.SUCCESS);
//            }
//        }
    }
}
