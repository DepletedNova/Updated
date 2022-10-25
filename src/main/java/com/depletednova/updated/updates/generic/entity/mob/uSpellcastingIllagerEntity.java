package com.depletednova.updated.updates.generic.entity.mob;

import com.depletednova.updated.updates.winter.entity.chillager.ChillagerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.EnumSet;

public abstract class uSpellcastingIllagerEntity extends IllagerEntity {
	public uSpellcastingIllagerEntity(EntityType<? extends IllagerEntity> entityType, World world) {
		super(entityType, world);
	}
	
	// Spellbook
	public enum Spell {
		NONE(0),
		AVALANCHE(1),
		REPEL(2),
		FREEZE(3);
		
		final byte val;
		Spell(int val) {
			this.val = (byte) val;
		}
		
		public static Spell byID(byte id) {
			for (Spell spell : Spell.values()) {
				if (spell.val == id) return spell;
			}
			return NONE;
		}
	}
	
	// Tracked Spell Data
	private static final TrackedData<Byte> SPELL = DataTracker.registerData(uSpellcastingIllagerEntity.class, TrackedDataHandlerRegistry.BYTE);
	private Spell currentSpell;
	private int spellTicks;
	
	protected int getSpellTicks() { return this.spellTicks; }
	
	public Spell getSpell() { return this.world.isClient ? Spell.byID(this.dataTracker.get(SPELL)) : currentSpell; }
	protected void setSpell(Spell spell) { this.currentSpell = spell; this.dataTracker.set(SPELL, spell.val); }
	protected boolean isSpellcasting() { return this.world.isClient ? this.dataTracker.get(SPELL) > 0 : spellTicks > 0; }
	
	@Override protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(SPELL, (byte) 0);
	}
	@Override public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.spellTicks = nbt.getInt("SpellTicks");
	}
	@Override public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("SpellTicks", this.spellTicks);
	}
	
	// Illager States
	@Override public IllagerEntity.State getState() {
		if (this.isSpellcasting()) return IllagerEntity.State.SPELLCASTING;
		if (this.isCelebrating()) return IllagerEntity.State.CELEBRATING;
		return IllagerEntity.State.NEUTRAL;
	}
	
	// Spell ticks
	@Override
	protected void mobTick() {
		super.mobTick();
		if (this.spellTicks > 0) --this.spellTicks;
	}
	
	// Base Goals
	protected abstract class CastSpellGoal extends Goal {
		protected int spellCooldown;
		protected int startTime;
		
		@Override public boolean canStart() {
			LivingEntity livingEntity = uSpellcastingIllagerEntity.this.getTarget();
			if (livingEntity == null || !livingEntity.isAlive()) return false;
			if (uSpellcastingIllagerEntity.this.isSpellcasting()) return false;
			return uSpellcastingIllagerEntity.this.age >= this.startTime;
		}
		
		@Override public boolean shouldContinue() {
			LivingEntity livingEntity = uSpellcastingIllagerEntity.this.getTarget();
			return livingEntity != null && livingEntity.isAlive() && this.spellCooldown > 0;
		}
		
		@Override
		public void start() {
			super.start();
			this.spellCooldown = this.getTickCount(this.getInitialCooldown());
			uSpellcastingIllagerEntity.this.spellTicks = this.getSpellTicks();
			this.startTime = uSpellcastingIllagerEntity.this.age + this.startTimeDelay();
			// TODO sound
			uSpellcastingIllagerEntity.this.setSpell(this.getSpell());
		}
		
		@Override public void tick() {
			--this.spellCooldown;
			// TODO particles
			if (this.spellCooldown == 0) {
				castSpell();
			}
		}
		
		protected int getInitialCooldown() {
			return 20;
		}
		
		protected abstract int getSpellTicks();
		protected abstract int startTimeDelay();
		
		protected abstract void castSpell();
		protected abstract Spell getSpell();
		
		// TODO sound
	}
	
	protected class LookAtTargetGoal extends Goal {
		public LookAtTargetGoal() { this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK)); }
		
		@Override public boolean canStart() { return uSpellcastingIllagerEntity.this.getSpellTicks() > 0; }
		
		@Override public void start() {
			super.start();
			uSpellcastingIllagerEntity.this.navigation.stop();
		}
		
		@Override public void stop() {
			super.stop();
			uSpellcastingIllagerEntity.this.setSpell(ChillagerEntity.Spell.NONE);
		}
		
		@Override public void tick() {
			if (uSpellcastingIllagerEntity.this.getTarget() != null) {
				uSpellcastingIllagerEntity.this.getLookControl()
						.lookAt(uSpellcastingIllagerEntity.this.getTarget(), uSpellcastingIllagerEntity.this.getMaxHeadRotation(), uSpellcastingIllagerEntity.this.getMaxLookPitchChange());
			}
		}
	}
}