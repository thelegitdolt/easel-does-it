package com.dolthhaven.easeldoesit.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class PaintingMaskModel extends Model {
    private static final String MASK = "mask";

    private final ModelPart root;
    private final ModelPart mask;

    public PaintingMaskModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.root = root;
        this.mask = root.getChild(MASK);
    }

    public ModelPart plate() {
        return this.mask;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild(MASK, CubeListBuilder.create()
                .texOffs(0, 0).addBox(0, 0, 0, 16, 16, 1), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 64, 64);
    }
}
