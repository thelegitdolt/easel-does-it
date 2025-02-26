package com.dolthhaven.easeldoesit.core.mixin;

import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void DoltModHow$SpawnPainting(RandomSource random, DifficultyInstance diff, CallbackInfo ci) {
        PaintingVariant variant = Util.getRandom(PaintingUtil.withTag(16, 16), random);
        ItemStack stack = PaintingUtil.makeStack(variant);
        this.setItemSlot(EquipmentSlot.HEAD, stack);
        this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 1f;
    }
}
