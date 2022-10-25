package com.depletednova.updated.foundation.registry.custom.blockatlas;

import com.depletednova.updated.foundation.registry.RegistryType;
import com.depletednova.updated.foundation.registry.custom.RegistryFactory;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.screen.PlayerScreenHandler;

public class BlockAtlasRegistryFactory extends RegistryFactory {
	@Override public void runFactory(boolean isClient) {
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((tex, reg) -> {
			for (RegistryType registry : this.registriesToRun) {
				((IUseBlockAtlas)registry).registerToAtlas(tex, reg);
			}
		});
	}
	
	@Override public FactoryType getFactoryType() { return FactoryType.CLIENT; }
}
