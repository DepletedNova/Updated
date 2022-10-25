package com.depletednova.updated.foundation.registry.world;

import com.depletednova.updated.Updated;
import com.depletednova.updated.foundation.registry.RegistryType;
import com.depletednova.updated.foundation.registry.custom.blockatlas.IUseBlockAtlas;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry extends RegistryType implements IUseBlockAtlas {
	@Override public void register(byte registryType) {
		particle = Registry.register(Registry.PARTICLE_TYPE, getIdentifier(), particleType);
	}
	
	@Override public void registerClient(byte registryType) {
		ParticleFactoryRegistry.getInstance().register(particleType, factory);
	}
	
	public DefaultParticleType particle;
	
	private final ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType> factory;
	private final DefaultParticleType particleType;
	public ParticleRegistry(String tag, ParticleFactoryRegistry.PendingParticleFactory<DefaultParticleType> factory) {
		super(tag, (byte)-100);
		particleType = FabricParticleTypes.simple();
		this.factory = factory;
	}
	
	// Block Atlas Texture Factory
	@Override public String getFactoryName() { return "block_atlas"; }
	@Override public void registerToAtlas(SpriteAtlasTexture tex, ClientSpriteRegistryCallback.Registry reg) {
		reg.register(Updated.ID("particle/" + getTag()));
	}
}
