package com.depletednova.updated.foundation.registry.misc;

import com.depletednova.updated.foundation.registry.RegistryType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;

public class EnchantmentRegistry extends RegistryType {
	@Override public void register(byte registryType) {
		Registry.register(Registry.ENCHANTMENT, getIdentifier(), enchantment);
	}
	
	public EnchantmentRegistry(String tag, Enchantment toRegister) {
		super(tag, (byte) 75);
		this.enchantment = toRegister;
	}
	
	private final Enchantment enchantment;
}
