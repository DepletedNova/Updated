package com.depletednova.updated.updates.winter.pine;

import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.world.tree.FoliagePlacerRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.*;
import java.util.function.BiConsumer;

public class WinterPineFoliagePlacer extends FoliagePlacer {
	public WinterPineFoliagePlacer(IntProvider radius, IntProvider offset) {
		super(radius, offset);
	}
	
	public static final Codec<WinterPineFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> fillFoliagePlacerFields(instance).apply(instance, WinterPineFoliagePlacer::new));
	
	@Override protected FoliagePlacerType<?> getType() {
		return Registrate.getRegistry(FoliagePlacerRegistry.class, (byte) -60, "winter_pine_foliage_placer").type;
	}
	
	// Immutable Fill Array
	public static final List<BoxFill> premadeTree = Arrays.asList(
			// Center
			new BoxFill(0, 4, 0, 0, 8, 0), // 1x1 Needle
			new BoxFill(-1, -3, -1, 1, 1, 1), // 3x3 Base
			new BoxFill(-2, -8, -2, 2, -4, 2), // 5x5 Base
			// X Thru
			new BoxFill(-1, 0, 0, 1, 4, 0), // Needle
			new BoxFill(-2, 0, 0, 2, 0, 0), // 5w Point
			new BoxFill(-2, -1, -1, 2, -9, 1), // 5w Base
			// Y Thru
			new BoxFill(0, 2, -1, 0, 4, 1), // Needle
			new BoxFill(0, 0, -2, 0, 0, 2), // 5w Point
			new BoxFill(-1, -1, -2, 1, -9, 2), // 5w Base
			// +X
			new BoxFill(3, -2, 0, 3, -2, 0), // 3o Point
			new BoxFill(3, -3, 1, 3, -9, -1), // 3o Panel
			new BoxFill(3, -6, 2, 3, -8, -2), // 3o Lower Panel
			new BoxFill(4, -8, -1, 4, -8, -1), // 4o Left
			new BoxFill(4, -5, 0, 4, -8, 0), // 4o Center
			new BoxFill(4, -7, 1, 4, -8, 1), // 4o Right
			// -X
			new BoxFill(-3, -6, 1, -3, -8, 1), // 3o Left
			new BoxFill(-3, -3, 0, -3, -8, 0), // 3o Center
			new BoxFill(-3, -5, -1, -3, -8, -1), // 3o Right
			// -Z
			new BoxFill(-1, -6, -3, -1, -8, -3), // 3o - 1
			new BoxFill(0, -4, -3, 0, -8, -3), // 3o - 2
			new BoxFill(1, -5, -3, 1, -8, -3), // 3o - 3
			new BoxFill(2, -7, -3, 2, -8, -3), // 3o - 4
			// +Z
			new BoxFill(0, -3, 3, 0, -4, 3), // 3o Point
			new BoxFill(-1, -5, 3, 1, -8, 3), // 3o Base
			new BoxFill(2, -7, 3, 2, -8, 3), // 3o Left
			new BoxFill(1, -9, 3, 0, -9, 3), // 3o Bottom
			new BoxFill(1, -7, 4, 1, -8, 4), // 4o Left
			new BoxFill(0, -6, 4, 0, -8, 4), // 4o Right
			// Bottom
			new BoxFill(2, -9, 2, 2, -9, -2), // l9 Fix
			new BoxFill(2, -10, 1, -1, -10, -1), // l10 Box
			new BoxFill(0, -10, 2, 1, -10, 2) // l10 Extension
	);
	
	// Generate tree
	@Override protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
		// Create & add to mutable array
		List<Vec3i> newBlocks = new ArrayList<>();
		for (BoxFill fill : premadeTree)
			for (int x = fill.min(BoxFill.vector.X); x <= fill.max(BoxFill.vector.X); x++)
				for (int y = fill.min(BoxFill.vector.Y); y <= fill.max(BoxFill.vector.Y); y++)
					for (int z = fill.min(BoxFill.vector.Z); z <= fill.max(BoxFill.vector.Z); z++)
						newBlocks.add(new Vec3i(x, y, z));
		
		// Random Orientation
		double radianAngle = Math.PI / 2 * ((double)random.nextInt(4));
		for (int m = 0; m < newBlocks.size(); m++) {
			Vec3i pos = newBlocks.get(m);
			int x2 = (int) Math.floor((pos.getX() - 0.5d) * Math.cos(radianAngle) - (pos.getZ() - 0.5d) * Math.sin(radianAngle) + 0.6d);
			int z2 = (int) Math.floor((pos.getZ() - 0.5d) * Math.cos(radianAngle) + (pos.getX() - 0.5d) * Math.sin(radianAngle) + 0.6d);
			//int y = pos.getY();
			// new Vec3i(x2, y, z2)
			newBlocks.set(m, pos.add(x2 - pos.getX(), 0, z2 - pos.getZ()));
		}
		
		for (Map.Entry<Vec3i, Boolean> entry : getBlocksToModify(newBlocks, random).entrySet()) {
			if (entry.getValue()) {
				newBlocks.add(entry.getKey());
			} else {
				newBlocks.remove(entry.getKey());
			}
		}
		
		// TODO Add extra "branches"
		
		// Place blocks from array
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (Vec3i pos : newBlocks) {
			mutable.set(pos.add(treeNode.getCenter()));
			FoliagePlacer.placeFoliageBlock(world, replacer, random, config, mutable);
		}
	}
	
	// Settings
	@Override public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
		return 3; // TODO fix this
	}
	
	@Override protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
		return false; // TODO also fix this
	}
	
	protected static Map<Vec3i, Boolean> getBlocksToModify(List<Vec3i> base, Random random) {
		Map<Vec3i, Boolean> modifications = new HashMap<>();
		for (Vec3i pos : base) {
			if (pos.getY() >= 4) continue; // Don't touch needle
			if (random.nextInt(9) < 5) continue; // Pass rate
			List<Vec3i> surroundings = getSurroundingBlocks(base, pos);
			if (surroundings.size() == 0) continue; // Continue if no near empties
			if (random.nextInt(3) != 0) {
				// Remove
				modifications.put(pos, false);
			} else {
				// Add
				int pickSize = random.nextInt(surroundings.size());
				Collections.shuffle(surroundings);
				for (int m = 0; m < pickSize; m++) {
					modifications.put(surroundings.get(m), true);
				}
			}
		}
		return modifications;
	}
	
	protected static List<Vec3i> getSurroundingBlocks(List<Vec3i> base, Vec3i pos) {
		List<Vec3i> surroundings = new ArrayList<>();
		if (!base.contains(pos.north())) surroundings.add(pos.north());
		if (!base.contains(pos.south())) surroundings.add(pos.south());
		if (!base.contains(pos.east())) surroundings.add(pos.east());
		if (!base.contains(pos.west())) surroundings.add(pos.west());
		if (!base.contains(pos.up())) surroundings.add(pos.up());
		if (!base.contains(pos.down())) surroundings.add(pos.down());
		return surroundings;
	}
	
	protected static class BoxFill {
		public final Vec3i fillFrom;
		public final Vec3i fillTo;
		
		public BoxFill(Vec3i from, Vec3i to) {
			this.fillFrom = from;
			this.fillTo = to;
		}
		
		public BoxFill(int x1, int y1, int z1, int x2, int y2, int z2) {
			this(new Vec3i(x1, y1, z1), new Vec3i(x2, y2, z2));
		}
		
		public int min(vector from) {
			switch(from) {
				case X: return Math.min(fillFrom.getX(), fillTo.getX());
				case Y: return Math.min(fillFrom.getY(), fillTo.getY());
				case Z: return Math.min(fillFrom.getZ(), fillTo.getZ());
			}
			return 0;
		}
		public int max(vector from) {
			switch (from) {
				case X: return Math.max(fillFrom.getX(), fillTo.getX());
				case Y: return Math.max(fillFrom.getY(), fillTo.getY());
				case Z: return Math.max(fillFrom.getZ(), fillTo.getZ());
			}
			return 0;
		}
		
		public enum vector { X, Y, Z }
	}
}
