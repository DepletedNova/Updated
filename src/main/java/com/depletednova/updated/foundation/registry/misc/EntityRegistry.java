package com.depletednova.updated.foundation.registry.misc;

import com.depletednova.updated.foundation.registry.RegistryType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import java.util.Optional;
import java.util.function.Function;

public class EntityRegistry<E extends Entity> extends RegistryType {
	@Override public void register(byte registryType) {
		// Entity
		entityType = Registry.register(Registry.ENTITY_TYPE, getIdentifier(), entityBuilder.build());
		
		// Attributes
		attributeBuilder.ifPresent(attribute -> FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) entityType, attribute));
		
		if (MobEntity.class.isAssignableFrom(entityClass)) {
			EntityType<? extends MobEntity> type = (EntityType<? extends MobEntity>) entityType;
			
			if (spawnEggCallback != null) spawnEggCallback.apply(type);
			//if (setSpawnRestrictions) SpawnRestrictionAccessor.callRegister(type, location, heightmap, predicate);
		}
	}
	
	@Override public void registerClient(byte registryType) {
		// Renderer
		EntityRendererRegistry.register(entityType, rendererFactory);
		
		// Model
		texProvider.ifPresent(provider -> EntityModelLayerRegistry.registerModelLayer(modelLayer, provider));
	}
	
	public EntityType<E> entityType;
	private final Class<E> entityClass;
	private final FabricEntityTypeBuilder entityBuilder;
	private final EntityRendererFactory<E> rendererFactory;
	private final EntityModelLayer modelLayer;
	private final Optional<EntityModelLayerRegistry.TexturedModelDataProvider> texProvider;
	private final Optional<DefaultAttributeContainer.Builder> attributeBuilder;
	public EntityRegistry(String tag, Class<E> entityClass, FabricEntityTypeBuilder entityBuilder,
						  EntityRendererFactory<E> rendererFactory, EntityModelLayer modelLayer, Optional<EntityModelLayerRegistry.TexturedModelDataProvider> texProvider,
						  Optional<DefaultAttributeContainer.Builder> attributeBuilder) {
		super(tag, (byte) 30);
		this.entityClass = entityClass;
		this.entityBuilder = entityBuilder;
		this.attributeBuilder = attributeBuilder;
		this.rendererFactory = rendererFactory;
		this.modelLayer = modelLayer;
		this.texProvider = texProvider;
	}
	
	private Function<EntityType<? extends MobEntity>, ItemRegistry> spawnEggCallback = null;
	
	public void setSpawnEgg(Function<EntityType<? extends MobEntity>, ItemRegistry> callBack) {
		this.spawnEggCallback = callBack;
	}
	
	private boolean setSpawnRestrictions = false;
	private SpawnRestriction.Location location;
	private Heightmap.Type heightmap;
	private SpawnRestriction.SpawnPredicate predicate;
	public EntityRegistry<E> setSpawnRestrictions(SpawnRestriction.Location location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate predicate) {
		this.setSpawnRestrictions = true;
		this.location = location;
		this.heightmap = heightmapType;
		this.predicate = predicate;
		return this;
	}
}
