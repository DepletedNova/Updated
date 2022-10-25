package com.depletednova.updated;

import com.depletednova.updated.foundation.AbstractUpdate;
import com.depletednova.updated.foundation.registry.Registrate;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Updated implements ModInitializer {
    public static final String ID = "updated";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override public void onInitialize() {
        Registrate.setupServer();
    }

    public static Identifier ID(String path) {
        return new Identifier(ID, path);
    }

    public static String IDs(String path) { return ID + ":" + path; }
    
    public static final boolean devBuild = true;
    
    public static AbstractUpdate currentUpdate;
}