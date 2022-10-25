package com.depletednova.updated.foundation.registry.custom.blockatlas;

import com.depletednova.updated.foundation.registry.custom.IRunFactory;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;

public interface IUseBlockAtlas extends IRunFactory {
	void registerToAtlas(SpriteAtlasTexture tex, ClientSpriteRegistryCallback.Registry reg);
}
