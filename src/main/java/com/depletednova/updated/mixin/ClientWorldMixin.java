package com.depletednova.updated.mixin;

import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.world.ParticleRegistry;
import com.depletednova.updated.updates.winter.WinterUpdate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.BiomeParticleConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
@Environment(value= EnvType.CLIENT)
public abstract class ClientWorldMixin {
	@Shadow public abstract void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);
	
	@Unique BiomeParticleConfig config = new BiomeParticleConfig(Registrate.getRegistry(ParticleRegistry.class, (byte) -100, "flurry").particle, 0.01f);
	
	@Inject(method = "randomBlockDisplayTick", at = @At("TAIL"))
	public void randomBlockDisplayTickSnow(int centerX, int centerY, int centerZ, int radius, Random random, Block block, BlockPos.Mutable pos, CallbackInfo ci) {
		ClientWorld world = MinecraftClient.getInstance().world;
		int i = centerX + random.nextInt(radius) - random.nextInt(radius);
		int j = centerY + random.nextInt(radius) - random.nextInt(radius);
		int k = centerZ + random.nextInt(radius) - random.nextInt(radius);
		pos.set(i, j, k);
		BlockState blockState = world.getBlockState(pos);
		
		if (!blockState.isFullCube(world, pos) && !world.isRaining()) {
			if (WinterUpdate.matchBiomeKey(world.getBiome(pos).getKey().get())) {
				if (config.shouldAddParticle(random)) {
					this.addParticle(config.getParticle(), (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
				}
			}
		}
		
	}
}
