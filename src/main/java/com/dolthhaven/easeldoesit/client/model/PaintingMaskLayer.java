package com.dolthhaven.easeldoesit.client.model;

import com.dolthhaven.easeldoesit.other.util.PaintingUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
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
    private static final float SKIBIDI_TOILET = Mth.PI;

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

        pose.pushPose();

        this.getParentModel().getHead().translateAndRotate(pose);
        ModelPart.Cube cube = this.getParentModel().getHead().getRandomCube(living.getRandom());

        float cubeXSize, cubeXStart, cubeYStart, cubeZStart, scaleFactor;

        if (this.getParentModel().young) {
            cubeXSize = cube.maxX - cube.minX;
            cubeXStart = (cube.minX + cube.maxX) / 2 / 16;
            cubeYStart = (cube.maxY + cube.minY) / 2 / 16;
            cubeZStart = cube.minZ / 16 - 0.0325f;
            scaleFactor = (cubeXSize + 0.6f) / 16;
            this.getParentModel();
        }
        else {
            cubeXSize = cube.maxX - cube.minX;
            cubeXStart = (cube.minX + cube.maxX) / 2 / 16;
            cubeYStart = (cube.maxY + cube.minY) / 2 / 16;
            cubeZStart = cube.minZ / 16 - 0.0325f;
            scaleFactor = (cubeXSize + 0.6f) / 16;
        }


        Painting painting = new Painting(living.level(), living.blockPosition(), Direction.SOUTH, PaintingUtil.holder(variant));

        pose.scale(scaleFactor, scaleFactor, scaleFactor);
        this.dispatcher.render(painting, cubeXStart / scaleFactor, cubeYStart / scaleFactor, cubeZStart / scaleFactor,
                180, partialTicks, pose, buffer, packedLight);
        pose.popPose();
    }

    private static Quaternionf getRotations(float angle, Vec3 axis) {
        float sin = Mth.sin(angle / 2);
        float cos = Mth.cos(angle / 2);
        axis = axis.scale(sin);
        return new Quaternionf(cos, axis.x, axis.y, axis.z);
    }

    private static Quaternionf followHeadRotations(float headYawInDeg, float headPitchInDeg) {
        // i found the formula

        float headYaw = headYawInDeg * Mth.DEG_TO_RAD;
        float headPitch = headPitchInDeg * Mth.DEG_TO_RAD;
        float angle = Math.acos(Mth.cos(headYaw) * Mth.cos(headPitch));

        float sinTheta = Mth.sin(headYaw);
        float cosTheta = Mth.cos(headYaw);
        float sinPhi = Mth.sin(headPitch);
        float cosPhi = Mth.cos(headPitch);

        // SPHERICAL COORDINATES
        Vec3 axis = new Vec3(cosTheta * sinPhi, sinTheta * sinPhi, cosPhi);

        Quaternionf quad = getRotations(angle, axis);
        return quad;
    }

    private static float sec2(float value) {
        return Mth.square(1 / Mth.cos(value));
    }
}
