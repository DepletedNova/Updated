package com.depletednova.updated.updates.winter.entity.frozen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class FrozenEntity extends Entity {
	private static final String default_id = "minecraft:zombie";
	
	public FrozenEntity(EntityType<?> type, World world) {
		super(type, world);
		this.dataTracker.set(MIMIC, default_id);
	}
	
	private static final TrackedData<String> MIMIC = DataTracker.registerData(FrozenEntity.class, TrackedDataHandlerRegistry.STRING);
	public String getMimicPath() {
		String mimic = getMimic();
		if (mimic.isBlank() || mimic.split(":").length != 2) mimic = default_id;
		return mimic.split(":")[1];
	}
	public String getMimicNamespace() {
		String mimic = getMimic();
		if (mimic.isBlank() || mimic.split(":").length != 2) mimic = default_id;
		return mimic.split(":")[0];
	}
	public String getMimic() {
		String mimic = this.dataTracker.get(MIMIC);
		String[] split = mimic.split(":");
		if (split.length < 2) mimic = "minecraft:" + mimic;
		return mimic;
	}
	public void setMimicTarget(LivingEntity target) {
		this.dataTracker.set(MIMIC, target.getEntityName());
	}
	
	@Override protected void initDataTracker() {
		this.dataTracker.startTracking(MIMIC, "minecraft:zombie");
	}
	
	@Override protected void readCustomDataFromNbt(NbtCompound nbt) {
		this.dataTracker.set(MIMIC, nbt.getString("mimic"));
	}
	
	@Override protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putString("mimic", getMimic());
	}
	
	@Override public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
}
