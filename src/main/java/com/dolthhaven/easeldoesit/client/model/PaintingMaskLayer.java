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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;
import org.joml.Quaternionf;

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
        if (Mth.abs(headPitch) > 0.1) {
            System.out.println(netHeadYaw + " and " + headPitch);
        }

        pose.pushPose();
        pose.mulPose(new Quaternionf(-1, 0, 0, 0));
        pose.mulPose(followHeadRotations(netHeadYaw, headPitch));

        pose.translate(0, 0.23f, 0.3f);
        pose.scale(0.55f, 0.55f, 0.55f);
        Painting painting = new Painting(living.level(), living.blockPosition(), Direction.NORTH, PaintingUtil.holder(variant));
        this.dispatcher.render(painting, 0, 0, 0, 0, partialTicks, pose, buffer, packedLight);

        pose.popPose();
    }

    private static Quaternionf followHeadRotations(float headYaw, float headPitch) {
        // i found the formula

        float headYawInRad = headYaw * Mth.DEG_TO_RAD;
        float headPitchInRad = headPitch * Mth.DEG_TO_RAD;

        float angle = Math.acos(
                Mth.cos(headYawInRad) /
                Mth.sqrt(1 + (1 / Mth.square(Mth.cos(headPitch)))));

        float sinHalfOmega = Mth.sin(angle / 2);
        float cosHalfOmega = Mth.cos(angle / 2);

        float sinTheta = Mth.sin(headYawInRad);
        float cosTheta = Mth.cos(headYawInRad);
        float sinPhi = Mth.sin(headPitchInRad);
        float cosPhi = Mth.sin(headPitchInRad);

        // SPHERICAL COORDINATES
        Vec3 axis = new Vec3(cosTheta * sinPhi, sinTheta * sinPhi, cosPhi);
        axis = axis.scale(-sinHalfOmega);

        return new Quaternionf(cosHalfOmega, axis.x, axis.y, axis.z);
    }
}
