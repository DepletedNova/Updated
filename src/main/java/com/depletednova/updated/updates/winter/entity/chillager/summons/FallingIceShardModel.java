package com.depletednova.updated.updates.winter.entity.chillager.summons;

import com.depletednova.updated.Updated;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class FallingIceShardModel extends EntityModel<FallingIceShardEntity> {
	@Override public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) { }
	
	public static final EntityModelLayer LAYER = new EntityModelLayer(Updated.ID("falling_ice_shard"), "main");
	
	private static final String CHUNK_SMALL = "chunk_small";
	private static final String CHUNK_LARGE = "chunk_large";
	private final ModelPart chunk_small;
	private final ModelPart chunk_large;
	public FallingIceShardModel(ModelPart root) {
		this.chunk_small = root.getChild(CHUNK_SMALL);
		this.chunk_large = root.getChild(CHUNK_LARGE);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		// Small Chunk
		modelPartData.addChild(CHUNK_SMALL, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0f, 4.0f, -8.0f, 16.0f, 28.0f, 16.0f),
				ModelTransform.of(0.0f, 32.0f, 0.0f, (float)Math.PI, 0.0f, 0.0f));
		// Large Chunk
		modelPartData.addChild(CHUNK_LARGE, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-16.0f, 0.0f, -16.0f, 16.0f, 32.0f, 16.0f)
						.cuboid(-16.0f, 0.0f, 0.0f, 16.0f, 28.0f, 16.0f)
						.cuboid(0.0f, 0.0f, -16.0f, 16.0f, 24.0f, 16.0f)
						.cuboid(0.0f, 0.0f, 0.0f, 16.0f, 20.0f, 16.0f),
				ModelTransform.of(0.0f, 32.0f, 0.0f, (float)Math.PI, 0.0f, 0.0f));
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	@Override public void setAngles(FallingIceShardEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		ModelPart chunk = entity.isLarge() ? chunk_large : chunk_small;
		chunk.pivotX = 0.0f;
		chunk.pivotZ = 0.0f;
		if (entity.getHoverTicks() > 0) {
			chunk.pivotX = (float) Math.cos(animationProgress * 1.7f);
			chunk.pivotZ = (float) Math.cos(animationProgress * 1.4f + 0.2f);
		}
	}
	
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, FallingIceShardEntity entity) {
		(entity.isLarge() ? chunk_large : chunk_small).render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}