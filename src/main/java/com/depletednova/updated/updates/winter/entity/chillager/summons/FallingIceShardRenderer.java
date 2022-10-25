package com.depletednova.updated.updates.winter.entity.chillager.summons;

import com.depletednova.updated.Updated;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FallingIceShardRenderer extends EntityRenderer<FallingIceShardEntity> {
	private static final Identifier TEX = Updated.ID("textures/entity/illager/chillager/iceshard.png");
	@Override public Identifier getTexture(FallingIceShardEntity entity) {
		return TEX;
	}
	
	private final FallingIceShardModel model;
	public FallingIceShardRenderer(EntityRendererFactory.Context context) {
		super(context);
		
		this.model = new FallingIceShardModel(context.getPart(FallingIceShardModel.LAYER));
	}
	
	@Override public void render(FallingIceShardEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		this.model.setAngles(entity, 0.0f, 0.0f, entity.age + tickDelta, 0.0f, 0.0f);
		VertexConsumer consumer = vertexConsumers.getBuffer(this.model.getLayer(TEX));
		
		this.model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f, entity);
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
