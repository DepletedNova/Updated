package com.depletednova.updated.foundation.registry.world.tree;

import com.depletednova.updated.foundation.registry.RegistryType;
import com.depletednova.updated.mixin.accessors.TrunkPlacerTypeAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class TrunkPlacerRegistry<T extends TrunkPlacer> extends RegistryType {
	@Override public void register(byte registryType) {
		type = TrunkPlacerTypeAccessor.registerNew(getIdentifier().toTranslationKey(), codec);
	}
	
	public TrunkPlacerRegistry(String tag, Codec<T> codec) {
		super(tag, (byte) -60);
		this.codec = codec;
	}
	
	protected final Codec<T> codec;
	
	public TrunkPlacerType<T> type;
}
