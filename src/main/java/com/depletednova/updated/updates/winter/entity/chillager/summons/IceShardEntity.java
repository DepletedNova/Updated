package com.depletednova.updated.updates.winter.entity.chillager.summons;

import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.misc.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IceShardEntity extends Entity {
	private static final float BASE_DAMAGE = 4.0f;
	
	public IceShardEntity(EntityType<?> type, World world) { super(type, world); }
	private IceShardEntity(World world, boolean large) {
		super(Registrate.getRegistry(EntityRegistry.class, (byte) 30, "ice_shard").entityType, world);
		this.dataTracker.set(LARGE, large);
	}
	
	public static void SummonShardGroup(LivingEntity parent, boolean large, float distance, int amount) {
		ServerWorld world = (ServerWorld) parent.world;
		float increment = 360.0f / amount;
		double p = Math.PI / 180;
		for (float angle = 0; angle < 360; angle += increment) {
			float yaw = (parent.getYaw() + angle) % 360;
			float x = (float)Math.sin(yaw * p) * distance;
			float z = (float)Math.cos(yaw * p) * distance;
			Vec3d predicted = parent.getPos().add(x, 0.0f, z);
			BlockPos predictedBlockPos = new BlockPos(predicted);
			BlockState predictedBlockState = world.getBlockState(predictedBlockPos);
			BlockPos downBlockPos;
			VoxelShape voxelShape;
			if (!(world.getBlockState(downBlockPos = predictedBlockPos.down()).isSideSolidFullSquare(world, downBlockPos, Direction.UP))) continue;
			if (!predictedBlockState.isAir() && !(voxelShape = predictedBlockState.getCollisionShape(world, predictedBlockPos)).isEmpty())
				predicted.add(0.0d, voxelShape.getMax(Direction.Axis.Y), 0.0d);
			IceShardEntity.summonShard(world, predicted, large, parent, yaw);
		}
	}
	
	public static void summonShard(World world, Vec3d pos, boolean large, LivingEntity owner, float yawDeg) {
		IceShardEntity entity = new IceShardEntity(world, large);
		// Rot
		entity.setYaw(yawDeg);
		entity.prevYaw = yawDeg;
		// Pos
		entity.setPosition(pos);
		entity.prevX = pos.getX();
		entity.prevY = pos.getY();
		entity.prevZ = pos.getZ();
		// Misc
		entity.setOwner(owner);
		
		world.spawnEntity(entity);
	}
	
	@Override public void tick() {
		super.tick();
		// Deals damage once in area after x age
		boolean large = this.dataTracker.get(LARGE);
		if (!this.dealtDamage && this.age >= (large ? 2 : 10)) {
			this.dealtDamage = true;
			this.world.getOtherEntities(this, this.getBoundingBox(), EntityPredicates.EXCEPT_SPECTATOR)
					.forEach(entity -> {
						if (entity != getOwner()) {
							entity.damage(new ProjectileDamageSource("shard_ice", this, getOwner()).setUsesMagic(), BASE_DAMAGE * (large ? 1.25f : 1.0f));
							Vec3d rot = this.getRotationVector();
							float multi = large ? 1.2f : 1.8f;
							entity.setVelocity(entity.getVelocity().add(-rot.x * multi, (large ? 0.7f : 0.6f), rot.z * multi));
						}
					});
			if (this.world.isClient) {
				BlockState blockState;
				if ((blockState = this.world.getBlockState(this.getBlockPos())).isAir()) blockState = this.world.getBlockState(this.getBlockPos().down());
				Vec3d pos = this.getPos();
				double y = Math.floor(pos.y);
				for (int i = 0; i < 10; i++) {
					this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), pos.x, y, pos.z, 0.0d, 0.5d, 0.0d);
				}
			}
		} else if (this.dealtDamage && this.age > (large ? 60 : 70)) {
			this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
			if (this.world.isClient) {
				BlockState defaultIceState = Blocks.BLUE_ICE.getDefaultState();
				for (int i = 0; i < 10; i++) {
					this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, defaultIceState),
							this.getX(), this.getY(), this.getZ(), 0.0d, 0.0d, 0.0d);
				}
			} else this.discard();
		}
	}
	
	// Data
	private static final TrackedData<Boolean> LARGE = DataTracker.registerData(FallingIceShardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public boolean isLarge() { return this.dataTracker.get(LARGE); }
	
	private boolean dealtDamage = false;
	@Override protected void initDataTracker() {
		this.dataTracker.startTracking(LARGE, true);
	}
	
	@Override public EntityDimensions getDimensions(EntityPose pose) {
		boolean large = this.dataTracker.get(LARGE);
		return super.getDimensions(pose).scaled(large ? 1.2f : 0.8f, large ? 1.35f : 1.0f);
	}
	
	// NBT
	@Override protected void readCustomDataFromNbt(NbtCompound nbt) {
		this.dealtDamage = nbt.getBoolean("DealtDamaged");
		this.dataTracker.set(LARGE, nbt.getBoolean("Large"));
	}
	@Override protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putBoolean("DealtDamaged", this.dealtDamage);
		nbt.putBoolean("Large", this.dataTracker.get(LARGE));
	}
	
	// Owner
	@Nullable private LivingEntity owner;
	public void setOwner(LivingEntity owner) {
		this.owner = owner;
	}
	@Nullable
	public LivingEntity getOwner() {
		return this.owner;
	}
	
	@Override public Packet<?> createSpawnPacket() { return new EntitySpawnS2CPacket(this); }
}
