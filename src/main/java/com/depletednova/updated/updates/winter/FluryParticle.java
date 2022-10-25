package com.depletednova.updated.updates.winter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FluryParticle extends SpriteBillboardParticle {
	Random random = new Random();
	protected FluryParticle(ClientWorld world, double xCoord, double yCoord, double zCoord, double xd, double yd, double zd) {
		super(world, xCoord, yCoord, zCoord, xd, yd, zd);
		
		this.velocityMultiplier = 0.6f;
		this.x = xd;
		this.y = yd;
		this.z = zd;
		this.scale *= 0.3f;
		this.maxAge = 50;
		
		this.red = 1f;
		this.blue = 1f;
		this.green = 1f;
	}
	
	float stepX = random.nextFloat();
	float stepZ = random.nextFloat();
	@Override public void tick() {
		super.tick();
		this.alpha = -(1/(float)maxAge) * age + 1; // Fading out
		this.velocityY = -0.05f;
		
		stepX = (stepX + 0.01f);
		stepZ = (stepZ + 0.01f);
		this.velocityX = Math.sin(stepX) * 0.05f;
		this.velocityZ = Math.sin(stepZ) * 0.075f;
	}
	
	@Override public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider sprites;
		
		public Factory(SpriteProvider spriteSet) {
			this.sprites = spriteSet;
		}
		
		@Nullable @Override public Particle createParticle(DefaultParticleType particleType, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			FluryParticle particle = new FluryParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSpriteForAge(sprites);
			return particle;
		}
	}
}
