package com.depletednova.updated.updates.winter.entity.chillager.summons;

import com.depletednova.updated.Updated;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

public class IceShardRenderer extends EntityRenderer<IceShardEntity> {
	@Override public Identifier getTexture(IceShardEntity entity) { return TEX; }
	private static final Identifier TEX = Updated.ID("textures/entity/illager/chillager/iceshard.png");
	private static final RenderLayer RENDER_LAYER = RenderLayer.getEntityCutoutNoCull(TEX);
	
	public static final EntityModelLayer LAYER = new EntityModelLayer(Updated.ID("ice_shard"), "main");
	private static final String LARGE_NAME = "large_shard";
	private static final String SMALL_NAME = "small_shard";
	
	private final ModelPart Large_Shard;
	private final ModelPart Small_Shard;
	public IceShardRenderer(EntityRendererFactory.Context context) {
		super(context);
		ModelPart model = context.getPart(LAYER);
		this.Large_Shard = model.getChild(LARGE_NAME);
		this.Small_Shard = model.getChild(SMALL_NAME);
	}
	
	@Override public void render(IceShardEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw));
		
		float delta = entity.age + tickDelta;
		boolean large = entity.isLarge();
		ModelPart active = large ? Large_Shard : Small_Shard;
		if ((large && delta <= 8) || (!large && delta <= 16)) {
			float angle = active.pitch;
			double amount = 4 - (large ? delta : delta - 8) * 0.5d;
			matrices.translate(0.0d, amount * Math.cos(angle), amount * Math.sin(angle));
		}
		
		VertexConsumer consumer = vertexConsumers.getBuffer(RENDER_LAYER);
		active.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild(LARGE_NAME, ModelPartBuilder.create()
						.uv(12, 6)	 .cuboid(-5.0f, -22.0f, -5.0f, 10.0f, 25.0f, 10.0f, Dilation.NONE),
				ModelTransform.rotation((float)Math.PI + 0.4363f, 0.0f, 0.0f));
		
		modelPartData.addChild(SMALL_NAME, ModelPartBuilder.create()
				.uv(16, 8).cuboid(-4.0f, -15.0f, -4.0f, 8.0f, 22.0f, 8.0f, Dilation.NONE),
				ModelTransform.rotation((float)Math.PI + 1.0472f, 0.0f, 0.0f));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
}
