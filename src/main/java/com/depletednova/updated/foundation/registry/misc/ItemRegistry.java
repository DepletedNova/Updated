package com.depletednova.updated.foundation.registry.misc;

import com.depletednova.updated.foundation.registry.RegistryType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ItemRegistry extends RegistryType {
	@Override public void register(byte registryType) {
		Registry.register(Registry.ITEM, getIdentifier(), item);
	}
	
	private final Item item;
	public ItemRegistry(String tag, Item item) {
		super(tag, (byte)40);
		this.item = item;
	}
}
