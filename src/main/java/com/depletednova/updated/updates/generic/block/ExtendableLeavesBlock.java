package com.depletednova.updated.updates.generic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public abstract class ExtendableLeavesBlock extends LeavesBlock {
	public ExtendableLeavesBlock(Settings settings) {
		super(settings);
		this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState())
				.with(getDISTANCE(), getMaxDistance()))
				.with(PERSISTENT, false))
				.with(WATERLOGGED, false))
				.with(LeavesBlock.DISTANCE, LeavesBlock.MAX_DISTANCE));
	}
	
	public abstract int getMaxDistance();
	public abstract IntProperty getDISTANCE();
	
	@Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(getDISTANCE(), PERSISTENT, WATERLOGGED, DISTANCE);
	}
	
	@Override public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, updateDistanceFromLogs(state, world, pos, getMaxDistance(), getDISTANCE()), Block.NOTIFY_ALL);
	}
	
	@Override public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		BlockState blockState = (BlockState)((BlockState)this.getDefaultState().with(PERSISTENT, true)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
		return updateDistanceFromLogs(blockState, ctx.getWorld(), ctx.getBlockPos(), getMaxDistance(), getDISTANCE());
	}
	
	@Override public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		int i;
		if (state.get(WATERLOGGED).booleanValue()) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if ((i = getDistanceFromLog(neighborState, getMaxDistance(), getDISTANCE()) + 1) != 1 || state.get(getDISTANCE()) != i) {
			world.createAndScheduleBlockTick(pos, this, 1);
		}
		return state;
	}
	
	@Override protected boolean shouldDecay(BlockState state) {
		return !state.get(PERSISTENT) && state.get(getDISTANCE()) == getMaxDistance();
	}
	
	@Override public boolean hasRandomTicks(BlockState state) {
		return state.get(getDISTANCE()) == getMaxDistance() && !state.get(PERSISTENT);
	}
	
	private static BlockState updateDistanceFromLogs(BlockState state, WorldAccess world, BlockPos pos, int maxDist, IntProperty dist) {
		int i = maxDist;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (Direction direction : Direction.values()) {
			mutable.set((Vec3i)pos, direction);
			i = Math.min(i, getDistanceFromLog(world.getBlockState(mutable), maxDist, dist) + 1);
			if (i == 1) break;
		}
		return (BlockState)state.with(dist, i);
	}
	
	private static int getDistanceFromLog(BlockState state, int maxDist, IntProperty dist) {
		if (state.isIn(BlockTags.LOGS)) {
			return 0;
		}
		Block block = state.getBlock();
		boolean inofExt = block instanceof ExtendableLeavesBlock;
		if (inofExt && state.contains(dist)) {
			return state.get(dist);
		}
		if (!inofExt && block instanceof LeavesBlock) {
			return state.get(LeavesBlock.DISTANCE);
		}
		return maxDist;
	}
}
