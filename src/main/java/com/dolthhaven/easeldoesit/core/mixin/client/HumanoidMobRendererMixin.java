package com.dolthhaven.easeldoesit.core.mixin.client;

import com.dolthhaven.easeldoesit.client.model.PaintingMaskLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin<T extends Mob, M extends HumanoidModel<T>> extends MobRenderer<T, M>  {
    public HumanoidMobRendererMixin(EntityRendererProvider.Context pContext, M pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(method = "Lnet/minecraft/client/renderer/entity/HumanoidMobRenderer;<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/HumanoidModel;FFFF)V", at = @At("TAIL"))
    private void EaselDoesIt$NewLayerDropped(EntityRendererProvider.Context context, HumanoidModel pModel, float pShadowRadius, float pScaleX, float pScaleY, float pScaleZ, CallbackInfo ci) {
        this.layers.add(0, new PaintingMaskLayer<>(context, this));
    }

}
