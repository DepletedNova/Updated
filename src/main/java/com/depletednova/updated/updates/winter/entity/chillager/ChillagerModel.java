package com.depletednova.updated.updates.winter.entity.chillager;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class ChillagerModel extends EntityModel<ChillagerEntity> implements ModelWithArms, ModelWithHead {
	// Mob
	private final ModelPart Torso;
	private final ModelPart Head;
	private final ModelPart LeftArm;
	private final ModelPart RightArm;
	private final ModelPart LeftLeg;
	private final ModelPart RightLeg;
	private final ModelPart Cape;
	
	public ChillagerModel(ModelPart root) {
		// Mob
		this.Torso = root.getChild("Torso");
		this.Head = root.getChild("Head");
		this.LeftArm = root.getChild("LeftArm");
		this.RightArm = root.getChild("RightArm");
		this.LeftLeg = root.getChild("LeftLeg");
		this.RightLeg = root.getChild("RightLeg");
		this.Cape = root.getChild("Cape");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("Torso", ModelPartBuilder.create().uv(0, 44).cuboid(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, Dilation.NONE).uv(0, 18).cuboid(-4.0F, -24.0F, -3.0F, 8.0F, 18.0F, 6.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		modelPartData.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, Dilation.NONE).uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, Dilation.NONE).uv(32, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("LeftArm", ModelPartBuilder.create().uv(28, 46).mirrored().cuboid(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, Dilation.NONE).mirrored(false), ModelTransform.pivot(4.0F, 2.0F, 0.0F));
		modelPartData.addChild("RightArm", ModelPartBuilder.create().uv(28, 46).cuboid(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, Dilation.NONE), ModelTransform.pivot(-4.0F, 2.0F, 0.0F));
		modelPartData.addChild("LeftLeg", ModelPartBuilder.create().uv(44, 46).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, Dilation.NONE).mirrored(false), ModelTransform.pivot(2.0F, 12.0F, 0.0F));
		modelPartData.addChild("RightLeg", ModelPartBuilder.create().uv(44, 46).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, Dilation.NONE), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));
		modelPartData.addChild("Cape", ModelPartBuilder.create().uv(28, 25).cuboid(-5.0F, -0.5F, 0.0F, 10.0F, 18.0F, 1.0F, Dilation.NONE), ModelTransform.pivot(0.0F, 1.0F, 3.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override public void setAngles(ChillagerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float val = ((float)Math.PI / 180);
		this.Head.yaw = netHeadYaw * val;
		this.Head.pitch = headPitch * val;
		this.Cape.pitch = MathHelper.cos(ageInTicks * 0.04f) * 0.065f + (float)Math.PI * 0.025f;
		if (this.riding) {
			this.RightArm.pitch = -0.62831855f;
			this.RightArm.yaw = 0.0f;
			this.RightArm.roll = 0.0f;
			this.LeftArm.pitch = -0.62831855f;
			this.LeftArm.yaw = 0.0f;
			this.LeftArm.roll = 0.0f;
			this.RightLeg.pitch = -1.4137167f;
			this.RightLeg.yaw = 0.31415927f;
			this.RightLeg.roll = 0.07853982f;
			this.LeftLeg.pitch = -1.4137167f;
			this.LeftLeg.yaw = -0.31415927f;
			this.LeftLeg.roll = -0.07853982f;
		} else {
			this.RightArm.pitch = MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 2.0f * limbSwingAmount * 0.5f;
			this.RightArm.yaw = 0.0f;
			this.RightArm.roll = 0.0f;
			this.LeftArm.pitch = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f;
			this.LeftArm.yaw = 0.0f;
			this.LeftArm.roll = 0.0f;
			this.RightLeg.pitch = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount * 0.5f;
			this.RightLeg.yaw = 0.0f;
			this.RightLeg.roll = 0.0f;
			this.LeftLeg.pitch = MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount * 0.5f;
			this.LeftLeg.yaw = 0.0f;
			this.LeftLeg.roll = 0.0f;
		}
		IllagerEntity.State state = entity.getState();
		if (state == IllagerEntity.State.SPELLCASTING) {
			this.RightArm.pitch = -(float)Math.PI * 0.85f + (float)Math.cos(ageInTicks) * 0.05f;
			this.RightArm.yaw = (float)Math.PI / 70.0f;
			this.RightArm.roll = 0.0f;
			this.LeftArm.pitch = -(float)Math.PI * 0.85f + (float)Math.cos(ageInTicks) * 0.05f;
			this.LeftArm.yaw = -(float)Math.PI / 70.0f;
			this.LeftArm.roll = 0.0f;
		} else if (state == IllagerEntity.State.CELEBRATING) {
			this.RightArm.pivotZ = 0.0f;
			this.RightArm.pivotX = -5.0f;
			this.RightArm.pitch = MathHelper.cos(ageInTicks * 0.6662f) * 0.05f;
			this.RightArm.roll = 2.670354f;
			this.RightArm.yaw = 0.0f;
			this.LeftArm.pivotZ = 0.0f;
			this.LeftArm.pivotX = 5.0f;
			this.LeftArm.pitch = MathHelper.cos(ageInTicks * 0.6662f) * 0.05f;
			this.LeftArm.roll = -2.3561945f;
			this.LeftArm.yaw = 0.0f;
		}
	}
	
	@Override public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		Torso.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		Head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		LeftArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		RightArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		LeftLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		RightLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		Cape.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
	
	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		(arm == Arm.LEFT ? LeftArm : RightArm).rotate(matrices);
	}
	
	@Override
	public ModelPart getHead() {
		return Head;
	}
}