package com.depletednova.updated.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockAccessor {
	@Accessor()
	StateManager<Block, BlockState> getStateManager();
	
	@Accessor
	void setDefaultState(BlockState state);
}
