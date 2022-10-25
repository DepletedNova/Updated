package com.depletednova.updated.foundation.data;

import com.depletednova.updated.foundation.registry.RegistryType;
import com.depletednova.updated.foundation.registry.blocks.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
	public BlockTagGenerator(FabricDataGenerator dataGenerator) { super(dataGenerator); }
	
	@Override protected void generateTags() {
		for (RegistryType type : GeneratorHub.getList("block_tag")) {
			if (!(type instanceof BlockRegistry)) continue;
			BlockRegistry registry = (BlockRegistry)type;
			for (TagKey<Block> tag : ((BlockRegistry)type).tags) {
				this.getOrCreateTagBuilder(tag).add(registry.block);
			}
		}
	}
}
