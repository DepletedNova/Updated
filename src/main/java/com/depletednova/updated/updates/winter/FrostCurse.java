package com.depletednova.updated.updates.winter;

import com.depletednova.updated.Updated;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

public class FrostCurse extends Enchantment {
	protected FrostCurse() {
		super(Rarity.RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET });
	}
	
	@Override public boolean isCursed() { return true; }
	@Override public boolean isTreasure() { return true; }
	
	@Override public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
		Map<EquipmentSlot, ItemStack> armor = getEquipment(user);
		byte enchants = 0;
		for (Map.Entry<EquipmentSlot, ItemStack> armorItem : armor.entrySet())
			for (NbtElement ench : armorItem.getValue().getEnchantments())
				if (((NbtCompound)ench).getString("id").equals(Updated.IDs("frost"))) enchants++;
		user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 50 * enchants, MathHelper.clamp(1 * enchants, 1, 3)));
		super.onUserDamaged(user, attacker, level);
	}
}
