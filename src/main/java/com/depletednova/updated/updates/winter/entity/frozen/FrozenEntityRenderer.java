package com.depletednova.updated.updates.winter.entity.frozen;

import com.depletednova.updated.Updated;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3f;

import java.util.Map;

public class FrozenEntityRenderer extends EntityRenderer<FrozenEntity> {
	public static final EntityModelLayer LAYER = new EntityModelLayer(Updated.ID("frozen_entity"), "main");
	
	private EntityRendererFactory.Context context;
	public FrozenEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		this.context = ctx;
	}
	
	private static Identifier DEFAULT_TEX = Updated.ID("textures/entity/frozen_entity.png");
	@Override public Identifier getTexture(FrozenEntity entity) {
		Identifier tex = Identifier.of(entity.getMimicNamespace(), "textures/entity/frozen/" + entity.getMimicPath() + ".png");
		if (context.getResourceManager().getResource(tex).isPresent()) return tex;
		return DEFAULT_TEX;
	}
	
	private String currentMimic = "init";
	private ModelPart part;
	private EntityModelLayer layer;
	
	@Override public void render(FrozenEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		if (!currentMimic.equals(entity.getMimicPath())) {
			updateModel(entity.getMimicPath());
		}
		
		if (part != null && layer != null) {
			// Model generic translation & orientation fix
			part.pitch = (float)Math.PI;
			matrices.translate(0.0, 1.5, 0.0);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(yaw));
			
			// Render model
			VertexConsumer y = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(entity)));
			part.render(matrices, y, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	
	private void updateModel(String ID) {
		currentMimic = ID;
		Pair<EntityModelLayer, TexturedModelData> data = getModelData(currentMimic);
		this.part = data.getRight().createModel();
		this.layer = data.getLeft();
	}
	
	private static Pair<EntityModelLayer, TexturedModelData> getModelData(String name) {
		for (Map.Entry<EntityModelLayer, TexturedModelData> entry : EntityModels.getModels().entrySet()) {
			if (!entry.getKey().getId().getPath().equals(name)) continue;
			return new Pair<>(entry.getKey(), entry.getValue());
		}
		EntityModelLayer layer = EntityModelLayers.ZOMBIE;
		return new Pair<>(layer, EntityModels.getModels().get(layer));
	}
}
