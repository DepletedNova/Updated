package com.depletednova.updated.foundation.data;

import com.depletednova.updated.foundation.registry.LootTableRegistry;
import com.depletednova.updated.foundation.registry.RegistryType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ChestLootTableGenerator extends SimpleFabricLootTableProvider {
	public ChestLootTableGenerator(FabricDataGenerator dataGenerator, LootContextType lootContextType) {
		super(dataGenerator, LootContextTypes.CHEST);
	}
	
	@Override public void accept(BiConsumer<Identifier, LootTable.Builder> BiConsumer) {
		for (RegistryType type : GeneratorHub.getList("chest_loot_table")) {
			if (!(type instanceof LootTableRegistry)) continue;
			((LootTableRegistry)type).acceptConsumer(BiConsumer);
		}
	}
}
