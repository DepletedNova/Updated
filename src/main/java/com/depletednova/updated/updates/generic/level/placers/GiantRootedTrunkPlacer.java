package com.depletednova.updated.updates.generic.level.placers;

import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.world.tree.TrunkPlacerRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.GiantTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class GiantRootedTrunkPlacer extends TrunkPlacer {
	public GiantRootedTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) { super(baseHeight, firstRandomHeight, secondRandomHeight); }
	
	public static final Codec<GiantRootedTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> GiantRootedTrunkPlacer.fillTrunkPlacerFields(instance).apply(instance, GiantRootedTrunkPlacer::new));
	
	@Override protected TrunkPlacerType<?> getType() { return Registrate.getRegistry(TrunkPlacerRegistry.class, (byte) -60,"giant_rooted_trunk_placer").type; }
	
	@Override public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
		BlockPos blockPos = startPos.down();
		// Dort
		GiantTrunkPlacer.setToDirt(world, replacer, random, blockPos, config);
		GiantTrunkPlacer.setToDirt(world, replacer, random, blockPos.east(), config);
		GiantTrunkPlacer.setToDirt(world, replacer, random, blockPos.south(), config);
		GiantTrunkPlacer.setToDirt(world, replacer, random, blockPos.south().east(), config);
		// Main Tree
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int y = 0; y < height; y++) {
			this.setLog(world, replacer, random, mutable, config, startPos, 0, y, 0);
			this.setLog(world, replacer, random, mutable, config, startPos, 1, y, 0);
			this.setLog(world, replacer, random, mutable, config, startPos, 1, y, 1);
			this.setLog(world, replacer, random, mutable, config, startPos, 0, y, 1);
		}
		// Root algorithm
		int[] logHeights = new int[8];
		int emptySpots = 2;
		for (int logX = 0; logX < 8; logX++) {
			// Test if spot should be empty
			if ((float)emptySpots / (8.0f - ((float)logX + 1.0f)) >= random.nextFloat()) {
				emptySpots--;
				logHeights[logX] = 0;
				continue;
			}
			// Set 0
			if (logX == 0) {
				logHeights[logX] = random.nextInt(3) + 1;
				continue;
			}
			// Set height based on other heights
			int x = random.nextInt(3) + 1;
			while (true) {
				if (logHeights[logX - 1] == x || logHeights[(logX + 1) % 8] == x) x = random.nextInt(3) + 1;
				else break;
			}
			logHeights[logX] = x;
		}
		// Place roots
		createRoot(world, replacer, random, mutable, config, startPos, 0, -1, logHeights[0]);
		createRoot(world, replacer, random, mutable, config, startPos, 1, -1, logHeights[1]);
		createRoot(world, replacer, random, mutable, config, startPos, 2, 0, logHeights[2]);
		createRoot(world, replacer, random, mutable, config, startPos, 2, 1, logHeights[3]);
		createRoot(world, replacer, random, mutable, config, startPos, 1, 2, logHeights[4]);
		createRoot(world, replacer, random, mutable, config, startPos, 0, 2, logHeights[5]);
		createRoot(world, replacer, random, mutable, config, startPos, -1, 1, logHeights[6]);
		createRoot(world, replacer, random, mutable, config, startPos, -1, 0, logHeights[7]);
		
		return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(height), 0, true));
	}
	
	private void setLog(TestableWorld testableWorld, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.Mutable mutable, TreeFeatureConfig treeFeatureConfig, BlockPos blockPos, int x, int y, int z) {
		mutable.set(blockPos, x, y, z);
		this.trySetState(testableWorld, biConsumer, random, mutable, treeFeatureConfig);
	}
	
	private void createRoot(TestableWorld testableWorld, BiConsumer<BlockPos, BlockState> biConsumer, Random random, BlockPos.Mutable mutable, TreeFeatureConfig treeFeatureConfig, BlockPos blockPos, int x, int z, int height) {
		if (height == 0) return;
		for (int y = -1; y < height; y++) {
			setLog(testableWorld, biConsumer, random, mutable, treeFeatureConfig, blockPos, x, y, z);
		}
	}
}
