package com.depletednova.updated.foundation.data;

import com.depletednova.updated.foundation.registry.RegistryType;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GeneratorHub implements DataGeneratorEntrypoint {
    private static Map<String, Pair<Function<FabricDataGenerator, ? extends DataProvider>, List<RegistryType>>> generators = Map.ofEntries(
            Map.entry("block_tag", Pair.of(BlockTagGenerator::new, new ArrayList<>()))
            //Map.entry("chest_loot_table", Pair.of(LootTableProvider::new, new ArrayList<>()))
    );
    
    @Override public void onInitializeDataGenerator(FabricDataGenerator generator) {
        for (Pair<Function<FabricDataGenerator, ? extends DataProvider>, List<RegistryType>> reg : generators.values()) {
            generator.addProvider(reg.getFirst());
        }
    }
    
    public static void addRegistry(String id, RegistryType self) {
        generators.get(id).getSecond().add(self);
    }
    public static List<RegistryType> getList(String id) {
        return generators.get(id).getSecond();
    }
}
