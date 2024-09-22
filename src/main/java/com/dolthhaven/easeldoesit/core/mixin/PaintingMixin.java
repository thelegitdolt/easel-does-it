package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.core.EaselDoesIt;
import com.dolthhaven.easeldoesit.core.other.EaselModTrackedData;
import com.dolthhaven.easeldoesit.data.server.tags.EaselModTags;
import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Painting.class)
public abstract class PaintingMixin extends HangingEntity {
    protected PaintingMixin(EntityType<? extends HangingEntity> p_31703_, Level p_31704_) {
        super(p_31703_, p_31704_);
    }

    @Shadow public abstract Holder<PaintingVariant> getVariant();

    @Inject(method = "dropItem", cancellable = true,
            at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
                    target = "Lnet/minecraft/world/entity/decoration/Painting;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private void EaselDoesIt$PaintingsDropTheirOwnVariants(Entity p_31925_, CallbackInfo ci) {
        IDataManager iDataManager = (IDataManager) this;

        if (!this.getVariant().is(EaselModTags.Paintings.ALWAYS_DROP_ITSELF) &&
            !iDataManager.getValue(EaselModTrackedData.PAINTING_SHOULD_DROP_SELF)) {
            return;
        }

        ItemStack paintingStack = PaintingUtil.getStackFromPainting(this.getVariant().get());
        this.spawnAtLocation(paintingStack);
        ci.cancel();
    }
}
