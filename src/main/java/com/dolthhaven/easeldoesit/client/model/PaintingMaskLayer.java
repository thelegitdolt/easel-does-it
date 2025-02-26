package com.dolthhaven.easeldoesit.client.model;

import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;

public class PaintingMaskLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public PaintingMaskLayer(EntityRendererProvider.Context context, RenderLayerParent<T, M> parent) {
        super(parent);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, T living,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = living.getItemBySlot(EquipmentSlot.HEAD);
        PaintingVariant variant = PaintingUtil.readStack(stack).orElse(null);
        if (variant == null || variant.getHeight() != 16 || variant.getWidth() != 16) {
            return;
        }

        Painting painting = new Painting(living.level(), living.blockPosition(), Direction.NORTH, PaintingUtil.holder(variant));
        this.dispatcher.render(painting, 0, 0, 0, 0, partialTicks, pose, buffer, packedLight);

    }
}
