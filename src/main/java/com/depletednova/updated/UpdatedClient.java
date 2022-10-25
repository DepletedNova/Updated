package com.depletednova.updated;

import com.depletednova.updated.foundation.registry.Registrate;
import net.fabricmc.api.ClientModInitializer;

public class UpdatedClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        Registrate.setupClient();
    }
}
