package com.depletednova.updated.updates.winter.entity.chillager;

import com.depletednova.updated.updates.generic.entity.mob.uSpellcastingIllagerEntity;
import com.depletednova.updated.updates.winter.entity.chillager.summons.FallingIceShardEntity;
import com.depletednova.updated.updates.winter.entity.chillager.summons.IceShardEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class ChillagerEntity extends uSpellcastingIllagerEntity {
	public ChillagerEntity(EntityType<? extends IllagerEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 20;
	}
	
	@Override public void addBonusForWave(int wave, boolean unused) { }
	
	public static DefaultAttributeContainer.Builder createChillagerAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5f)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0f)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 48.0f);
	}
	
	@Override protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new LookAtTargetGoal());
		this.goalSelector.add(2, new FleeEntityGoal<PlayerEntity>(this, PlayerEntity.class, 8.0f, 0.6f, 1.0f));
		this.goalSelector.add(3, new FleeEntityGoal<IronGolemEntity>(this, IronGolemEntity.class, 8.0f, 0.6f, 1.0f));
		this.goalSelector.add(5, new AvalancheSpellGoal());
		this.goalSelector.add(6, new RepelSpellGoal());
		this.goalSelector.add(7, new FreezeSpellGoal());
		this.goalSelector.add(8, new WanderAroundGoal(this, 0.6));
		this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0f, 1.0f));
		this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0f));
		this.targetSelector.add(1, new RevengeGoal(this, RaiderEntity.class).setGroupRevenge(new Class[0]));
		this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true).setMaxTimeWithoutVisibility(300));
		this.targetSelector.add(3, new ActiveTargetGoal(this, MerchantEntity.class, false).setMaxTimeWithoutVisibility(300));
		this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, false).setMaxTimeWithoutVisibility(300));
	}
	
	// Tracked Data
	@Override protected void initDataTracker() {
		super.initDataTracker();
	}
	
	@Override public void onTrackedDataSet(TrackedData<?> data) {
		super.onTrackedDataSet(data);
	}
	
	// Sounds
	@Override public SoundEvent getCelebratingSound() {
		return SoundEvents.ENTITY_EVOKER_CELEBRATE;
	}
	@Override protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_EVOKER_AMBIENT;
	}
	@Override protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_EVOKER_DEATH;
	}
	@Override protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_EVOKER_HURT;
	}
	
	// Avalanche
	private class AvalancheSpellGoal extends CastSpellGoal {
		@Override protected void castSpell() {
			FallingIceShardEntity.summonShard((ServerWorld) ChillagerEntity.this.world,
					ChillagerEntity.this.getTarget().getPos().add(0.0d, 5.0d, 0.0d)
							.add(random.nextFloat() * 0.5f - 0.25f, random.nextFloat() * 0.5f - 0.25f, random.nextFloat() * 0.5f - 0.25f), random.nextBoolean(),
					15 + random.nextInt(20), ChillagerEntity.this);
		}
		
		@Override protected int getSpellTicks() { return 80; }
		@Override protected int startTimeDelay() { return 200; }
		
		@Override protected Spell getSpell() { return Spell.AVALANCHE; }
	}
	
	// Repel
	private class RepelSpellGoal extends CastSpellGoal {
		@Override protected void castSpell() {
			
			IceShardEntity.SummonShardGroup(ChillagerEntity.this, true, 1.5f, 6);
			IceShardEntity.SummonShardGroup(ChillagerEntity.this, false, 3.0f, 10);
		}
		
		@Override protected int getSpellTicks() { return 120; }
		@Override protected int startTimeDelay() { return 200; }
		
		@Override protected Spell getSpell() { return Spell.REPEL; }
		
		@Override public boolean canStart() {
			return super.canStart() && ChillagerEntity.this.getTarget().distanceTo(ChillagerEntity.this) <= 3.0f && ChillagerEntity.this.onGround;
		}
	}
	
	private class FreezeSpellGoal extends CastSpellGoal {
		@Override protected void castSpell() {
			ChillagerEntity.this.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 2), ChillagerEntity.this);
		}
		
		@Override protected int getSpellTicks() { return 30; }
		@Override protected int startTimeDelay() { return 200; }
		
		@Override protected Spell getSpell() { return Spell.FREEZE; }
	}
}
