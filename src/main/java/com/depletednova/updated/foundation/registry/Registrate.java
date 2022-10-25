package com.depletednova.updated.foundation.registry;

import com.depletednova.updated.Updated;
import com.depletednova.updated.foundation.AbstractUpdate;
import com.depletednova.updated.foundation.registry.custom.IRunFactory;
import com.depletednova.updated.foundation.registry.custom.RegistryFactory;
import com.depletednova.updated.foundation.registry.custom.blockatlas.BlockAtlasRegistryFactory;
import com.depletednova.updated.updates.winter.WinterUpdate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Registrate<T extends RegistryType> {
    // Registered Updates
    public static final List<AbstractUpdate> UPDATES = Arrays.asList(
            new WinterUpdate() // Winter Wonderland Update
    );
    
    private static final Map<String, RegistryFactory> factories = Map.ofEntries(
            Map.entry("block_atlas", new BlockAtlasRegistryFactory())
    );
    
    public static void addToFactory(RegistryType registryToAdd) {
        if (!(registryToAdd instanceof IRunFactory)) throw new Error("RegistryType can not be added to a factory.");
        String regName = ((IRunFactory) registryToAdd).getFactoryName();
        if (!factories.containsKey(regName)) throw new Error("Factory does not exist: " + regName);
        factories.get(regName).addToRegistry(registryToAdd);
    }
    
    public static Map<Byte, Map<String, RegistryType>> registries = new HashMap<>();
    
    public static void addRegistry(RegistryType registry, byte priority) {
        if (!registries.containsKey(priority))
            registries.put(priority, new HashMap<>());
        registries.get(priority).put(registry.getTag(), registry);
    }

    public static void setupServer() {
        for (AbstractUpdate update : UPDATES) update.registerUpdate();
        Updated.currentUpdate = UPDATES.get(UPDATES.size() - 1);
        for (RegistryFactory factory : factories.values())
            if (factory.getFactoryType() == RegistryFactory.FactoryType.SERVER || factory.getFactoryType() == RegistryFactory.FactoryType.MIXED)
                factory.runFactory(false);
        for (byte x = Byte.MIN_VALUE; x < Byte.MAX_VALUE; x++) {
            if (!registries.containsKey(x)) continue;
            for (RegistryType registry : registries.get(x).values()) {
                registry.register(x);
            }
        }
    }

    public static void setupClient() {
        for (AbstractUpdate update : UPDATES) update.registerClient();
        for (RegistryFactory factory : factories.values())
            if (factory.getFactoryType() == RegistryFactory.FactoryType.CLIENT || factory.getFactoryType() == RegistryFactory.FactoryType.MIXED)
                factory.runFactory(true);
        for (byte x = Byte.MIN_VALUE; x < Byte.MAX_VALUE; x++) {
            if (!registries.containsKey(x)) continue;
            for (RegistryType registry : registries.get(x).values()) {
                registry.registerClient(x);
            }
        }
    }

    public static RegistryType getGenericRegistry(byte priority, String tag) {
        if (!registries.containsKey(priority)) throw new Error("Priority level " + priority + " does not exist.");
        if (!registries.get(priority).containsKey(tag)) throw new Error("\"" +tag + "\" does not exist under priority level " + priority);
        return registries.get(priority).get(tag);
    }
    
    public static <T extends RegistryType> T getRegistry(Class<T> tClass, byte priority, String tag) {
        RegistryType reg = getGenericRegistry(priority, tag);
        T newReg = tClass.cast(reg);
        if (newReg == null) throw new Error("Registry could not be found of a specified type.");
        return newReg;
    }
}
