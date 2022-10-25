package com.depletednova.updated.foundation.registry.world.tree;

import com.depletednova.updated.foundation.registry.RegistryType;
import com.depletednova.updated.mixin.accessors.FoliagePlacerTypeAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class FoliagePlacerRegistry<T extends FoliagePlacer> extends RegistryType {
	@Override public void register(byte registryType) {
		type = FoliagePlacerTypeAccessor.registerNew(getIdentifier().toTranslationKey(), codec);
	}
	
	public FoliagePlacerRegistry(String tag, Codec<T> codec) {
		super(tag, (byte) -60);
		this.codec = codec;
	}
	
	protected final Codec<T> codec;
	
	public FoliagePlacerType<T> type;
}
