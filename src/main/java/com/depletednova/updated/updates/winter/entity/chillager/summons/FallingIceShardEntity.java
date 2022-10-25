package com.depletednova.updated.updates.winter.entity.chillager.summons;

import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.misc.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FallingIceShardEntity extends Entity {
	private static final int BASE_DAMAGE = 10;
	
	public FallingIceShardEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	private FallingIceShardEntity(World world, boolean isLarge, int hoverTime) {
		super(Registrate.getRegistry(EntityRegistry.class, (byte) 30, "falling_ice_shard").entityType, world);
		this.dataTracker.set(LARGE, this.isLarge = isLarge);
		this.setHoverTicks(hoverTime + (isLarge() ? 20 : 0));
	}
	
	public static void summonShard(World world, Vec3d position, boolean isLarge, int hoverTime, MobEntity owner) {
		FallingIceShardEntity entity = new FallingIceShardEntity(world, isLarge, hoverTime);
		entity.setPosition(position);
		entity.prevX = position.getX();
		entity.prevY = position.getY();
		entity.prevZ = position.getZ();
		entity.setOwner(owner);
		world.spawnEntity(entity);
	}
	
	private static final TrackedData<Integer> HOVER = DataTracker.registerData(FallingIceShardEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> LARGE = DataTracker.registerData(FallingIceShardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> GROUNDED = DataTracker.registerData(FallingIceShardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	@Nullable private MobEntity owner;
	private boolean isLarge;
	private int hoverTicks;
	
	public int getHoverTicks() { return this.world.isClient ? this.dataTracker.get(HOVER) : hoverTicks; }
	public void setHoverTicks(int ticks) { this.hoverTicks = ticks; this.dataTracker.set(HOVER, ticks); }
	
	@Override public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		this.world.getOtherEntities(this, this.getBoundingBox(), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR)
				.forEach(entity -> {
					if (entity != getOwner())
						entity.damage(new ProjectileDamageSource("falling_ice", this, getOwner()).setUsesMagic(), BASE_DAMAGE * (this.isLarge() ? 1.25f : 1));
				});
		// TODO drop ice chunk block
		return false;
	}
	
	@Override public void tick() {
		super.tick();
		// Calculate movement
		if (this.getHoverTicks() == 0) {
			if (!this.hasNoGravity()) {
				this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
			}
			this.move(MovementType.SELF, this.getVelocity());
		} else this.setHoverTicks(this.getHoverTicks() - 1);
		
		// Discard on contact with ground
		if (this.dataTracker.get(GROUNDED)) {
			this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
			if (this.world.isClient) {
				BlockState defaultIceState = Blocks.BLUE_ICE.getDefaultState();
				for (int i = 0; i < 10; i++) {
					this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, defaultIceState),
							this.getX(), this.getY(), this.getZ(), 0.0d, 0.0d, 0.0d);
				}
			} else this.discard();
		}
		if (!this.world.isClient && this.onGround) this.dataTracker.set(GROUNDED, true);
	}
	
	// Tracked Data
	@Override protected void initDataTracker() {
		this.dataTracker.startTracking(HOVER, 5);
		this.dataTracker.startTracking(LARGE, false);
		this.dataTracker.startTracking(GROUNDED, false);
	}
	
	// NBT
	@Override protected void readCustomDataFromNbt(NbtCompound nbt) {
		this.hoverTicks = nbt.getInt("Hover");
		this.dataTracker.set(LARGE, this.isLarge = nbt.getBoolean("Large"));
	}
	@Override protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putInt("Hover", hoverTicks);
		nbt.putBoolean("Large", this.isLarge);
	}
	
	@Override public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
	
	@Override public EntityDimensions getDimensions(EntityPose pose) {
		return super.getDimensions(pose).scaled((this.isLarge() ? 2.0f : 1.0f), 1.0f);
	}
	
	@Override public void onTrackedDataSet(TrackedData<?> data) {
		super.onTrackedDataSet(data);
		if (LARGE.equals(data)) {
			calculateDimensions();
		}
	}
	
	public void setOwner(@Nullable MobEntity owner) {
		this.owner = owner;
	}
	@Nullable public MobEntity getOwner() {
		return this.owner;
	}
	
	public boolean isLarge() {
		return this.world.isClient ? this.dataTracker.get(LARGE) : this.isLarge;
	}
}
