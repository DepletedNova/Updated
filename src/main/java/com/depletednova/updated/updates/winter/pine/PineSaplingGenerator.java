package com.depletednova.updated.updates.winter.pine;

import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.world.TreeRegistry;
import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class PineSaplingGenerator extends LargeTreeSaplingGenerator {
    @Nullable @Override protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getLargeTreeFeature(Random random)
    { return Registrate.getRegistry(TreeRegistry.class, (byte) -50, "pine").tree; }
    
    @Nullable @Override protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) { return null; }
}
