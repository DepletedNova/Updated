package com.depletednova.updated.mixin;

import com.depletednova.updated.updates.winter.WinterUpdate;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
@Environment(value= EnvType.CLIENT)
public class BackgroundRendererMixin {
	@Unique private static boolean fogActive = false;
	@Unique private static float currentFogEnd = -5;
	@Unique private static float currentFogStart = -5;
	
	@Inject(method = "applyFog", at = @At("TAIL"), cancellable = true)
	private static void applyFogThick(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
		CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
		Entity entity = camera.getFocusedEntity();
		
		//boolean underground = entity.getBlockPos().getY() <= CaveYMax && entity.world.isSkyVisible(entity.getBlockPos());
		fogActive = //underground ||
				entity.getBlockPos().getY() >= 40.0f && WinterUpdate.matchBiomeKey(entity.world.getBiome(entity.getBlockPos()).getKey().get());
		
		if (cameraSubmersionType != CameraSubmersionType.NONE ||
				(entity instanceof LivingEntity && (((LivingEntity) entity).hasStatusEffect(StatusEffects.BLINDNESS) || ((LivingEntity) entity).hasStatusEffect(StatusEffects.DARKNESS))))
			ci.cancel();
		
		float fogStart = RenderSystem.getShaderFogStart();
		float fogEnd = RenderSystem.getShaderFogEnd();
		
		if (currentFogStart <= 0) currentFogStart = fogStart;
		if (currentFogEnd <= 0) currentFogEnd = fogEnd;
		
		float viewDistStart = Math.min(viewDistance, 160.0f) * 0.015f;
		float viewDistEnd = Math.min(viewDistance, 192.0f) * 0.35f;
		if (fogActive && !entity.isSpectator()) {
			if (currentFogStart != viewDistStart) {
				currentFogStart += (viewDistStart - fogStart) / 180;
				if (viewDistStart > currentFogStart) currentFogStart = viewDistStart;
			}
			if (currentFogEnd != viewDistEnd) {
				currentFogEnd += (viewDistEnd - fogEnd) / 270;
				if (viewDistEnd > currentFogEnd) currentFogEnd = viewDistEnd;
			}
		} else if (!entity.isSpectator() && currentFogStart != fogStart && currentFogEnd != fogEnd) {
			currentFogStart = MathHelper.clamp(currentFogStart + (fogStart - viewDistStart) / 180, currentFogStart, fogStart);
			currentFogEnd = MathHelper.clamp(currentFogEnd + (fogEnd - viewDistEnd) / 140, currentFogEnd, fogEnd);
		} else ci.cancel();
		//if (underground)
			//RenderSystem.setShaderFogColor(0.25f, 0.25f, 0.35f);
		RenderSystem.setShaderFogStart(currentFogStart);
		RenderSystem.setShaderFogEnd(currentFogEnd);
	}
}
