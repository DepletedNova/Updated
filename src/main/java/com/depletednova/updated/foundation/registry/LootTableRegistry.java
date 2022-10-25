package com.depletednova.updated.foundation.registry;

import com.depletednova.updated.foundation.data.GeneratorHub;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class LootTableRegistry extends RegistryType {
	@Override public void register(byte registryType) { }
	
	public void acceptConsumer(BiConsumer<Identifier, LootTable.Builder> BiConsumer) {
		LootTable.Builder builder = LootTable.builder();
		
		for (Pool pool : this.pools) {
			LootPool.Builder poolBuilder = LootPool.builder()
					.rolls(pool.roll)
					.bonusRolls(pool.bonus);
			
			for (Pool.Entry entry : pool.entries)
				poolBuilder.with(ItemEntry.builder(entry.item).weight(entry.weight));
			
			builder.pool(poolBuilder);
		}
		
		BiConsumer.accept(getIdentifier(), builder);
	}
	
	public LootTableRegistry(String tag, LootType lootType) {
		super(lootType.path + tag, (byte) 0);
		switch (lootType) {
			case CHEST -> GeneratorHub.addRegistry("chest_loot_table", this);
		}
	}
	
	public enum LootType {
		CHEST("chest/");
		
		String path;
		LootType(String path) {
			this.path = path;
		}
	}
	
	private List<Pool> pools = new ArrayList<>();
	public LootTableRegistry addPool(Pool pool) {
		this.pools.add(pool);
		return this;
	}
	
	public static class Pool {
		public LootNumberProvider roll = ConstantLootNumberProvider.create(1);
		public void constantRoll(int value) { this.roll = ConstantLootNumberProvider.create(value); }
		public void uniformRoll(int min, int max) { this.roll = UniformLootNumberProvider.create(min, max); }
		
		public LootNumberProvider bonus = ConstantLootNumberProvider.create(0);
		public void constantBonus(int value) { this.bonus = ConstantLootNumberProvider.create(value); }
		public void uniformBonus(int min, int max) { this.bonus = UniformLootNumberProvider.create(min, max); }
		
		public LootCondition condition;
		public Pool setCondition(LootCondition condition) {
			this.condition = condition;
			return this;
		}
		
		public List<Entry> entries = new ArrayList<>();
		public Pool Entry(int weight, Item item) {
			Entry entry = new Entry();
			entry.item = item;
			entry.weight = weight;
			entries.add(entry);
			return this;
		}
		public Pool ConditionalEntry(int weight, Item item, LootCondition condition) {
			this.Entry(weight, item);
			this.entries.get(this.entries.size() - 1).condition = condition;
			return this;
		}
		
		private class Entry {
			public int weight;
			public Item item;
			public LootCondition condition = null;
		}
	}
}
