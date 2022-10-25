package com.depletednova.updated.updates.winter.pine;

import com.depletednova.updated.updates.generic.block.ExtendableLeavesBlock;
import net.minecraft.state.property.IntProperty;

public class PineLeavesBlock extends ExtendableLeavesBlock {
	public PineLeavesBlock(Settings settings) { super(settings); }
	
	public static final IntProperty DISTANCE = IntProperty.of("distance_",1, 13);
	
	@Override public int getMaxDistance() { return 13; }
	
	@Override public IntProperty getDISTANCE() { return DISTANCE; }
}
