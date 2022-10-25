package com.depletednova.updated.foundation.registry;

import com.depletednova.updated.Updated;
import com.depletednova.updated.foundation.registry.custom.IRunFactory;
import net.minecraft.util.Identifier;

public abstract class RegistryType {
    /*
        Generic Values:
        
        -100 : Particles
         -60 : Feature Placers
         -50 : Features
          10 : Blocks
          15 : Strippables
          30 : Entities
          40 : Items
          45 : Fuels
          75 : Enchantments
          90 : World Generation
         100 : Feature Generation
     */

    public RegistryType(String tag, byte priority) {
        // Set data values
        entryTag = tag;
        generatedIdentifier = Updated.ID(tag);

        // Add to registrate
        Registrate.addRegistry(this, priority);
        
        // Add to factory if applicable
        if (!(this instanceof IRunFactory)) return;
        Registrate.addToFactory(this);
    }

    private final String entryTag;
    public String getTag() { return entryTag; }
    private final Identifier generatedIdentifier;
    public Identifier getIdentifier() { return generatedIdentifier; }

    public abstract void register(byte registryType);
    public void registerClient(byte registryType) {}
}
