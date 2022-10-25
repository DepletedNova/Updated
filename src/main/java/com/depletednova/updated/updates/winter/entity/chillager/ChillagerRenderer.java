package com.depletednova.updated.updates.winter.entity.chillager;

import com.depletednova.updated.Updated;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ChillagerRenderer extends MobEntityRenderer<ChillagerEntity, ChillagerModel> {
	public static final EntityModelLayer LAYER = new EntityModelLayer(Updated.ID("iceologer"), "main");
	
	public ChillagerRenderer(EntityRendererFactory.Context context) {
		super(context, new ChillagerModel(context.getPart(LAYER)), 0.5f);
	}
	
	@Override public Identifier getTexture(ChillagerEntity entity) {
		return Updated.ID("textures/entity/illager/chillager/main.png");
	}
	
	@Override public void render(ChillagerEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
}
