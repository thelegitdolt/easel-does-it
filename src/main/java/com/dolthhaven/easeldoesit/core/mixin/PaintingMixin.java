package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Painting.class)
public abstract class PaintingMixin extends HangingEntity {
    protected PaintingMixin(EntityType<? extends HangingEntity> p_31703_, Level p_31704_) {
        super(p_31703_, p_31704_);
    }

    @Shadow public abstract Holder<PaintingVariant> getVariant();

    @WrapOperation(method = "dropItem",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/entity/decoration/Painting;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity EaselDoesIt$PaintingsDropTheirOwnVariants(Painting instance, ItemLike itemLike, Operation<ItemEntity> original) {
        IDataManager iDataManager = (IDataManager) this;

        if (!iDataManager.getValue(EaselModTrackedData.PAINTING_SHOULD_DROP_SELF)) {
            return original.call(instance, itemLike);
        }

        ItemStack paintingStack = PaintingUtil.makeStack(this.getVariant().value());
        return instance.spawnAtLocation(paintingStack);
    }
}
