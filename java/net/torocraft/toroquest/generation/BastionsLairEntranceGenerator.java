package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BastionsLairEntranceGenerator extends WorldGenerator {

	private final int boxRadius = 6;

	private final int radius = 5;
	private final int radiusSq = 5 * 5;
	private final int halfRadius = radius / 2;
	private final int innerRadiusSquared = (radius - 1) * radius;
	private int bottom;
	private BlockPos entrancePosition;
	private BlockPos origin;
	private int magSq;

	private BlockPos surface;
	private int x, y, z;
	private World world;
	private Random rand;
	private IBlockState block;
	private EnumFacing entranceFacing;

	@Override
	public boolean generate(World world, Random rand, BlockPos entrancePosition) {
		this.world = world;
		this.rand = rand;
		this.entrancePosition = entrancePosition;
		origin = entrancePosition;

		findSurface(entrancePosition);
		genEntrance();
		return true;
	}

	public void setEntrance(EnumFacing facing) {
		this.entranceFacing = facing;
	}

	private void findSurface(BlockPos start) {
		surface = null;
		BlockPos pos = new BlockPos(start);
		IBlockState blockState;
		while (pos.getY() < world.getActualHeight()) {
			pos = pos.up();
			blockState = world.getBlockState(pos);
			surface = pos;
			if (!isGroundBlock(blockState) && !isLiquid(blockState)) {
				break;
			}
		}
	}

	private boolean isLiquid(IBlockState blockState) {
		return blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.LAVA;
	}

	private boolean isGroundBlock(IBlockState blockState) {
		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() == Blocks.LOG || blockState.getBlock() instanceof BlockBush) {
			return false;
		}
		return blockState.isOpaqueCube();
	}

	private void genEntrance() {

		System.out.println("Gen Entrance " + origin);

		for (y = -20; y <= surface.getY() + 5; y++) {
			for (x = -boxRadius; x <= boxRadius; x++) {
				for (z = -boxRadius; z <= boxRadius; z++) {
					magSq = (x * x) + (z * z);
					chooseAndPlaceBlock();
				}
			}
		}
	}

	protected void chooseAndPlaceBlock() {

		if (isOutside()) {
			block = null;

		} else if (isWall() && !isEntrance()) {
			block = Blocks.STONEBRICK.getDefaultState();
		} else {
			block = Blocks.AIR.getDefaultState();
		}

		placeBlock();
	}

	private boolean isEntrance() {
		if (y > 5 || y < 1) {
			return false;
		}
		switch (entranceFacing) {
		case EAST:
			return x > halfRadius;
		case NORTH:
			return z > halfRadius;
		case SOUTH:
			return z < -halfRadius;
		case WEST:
			return x < -halfRadius;
		default:
			return false;
		}
	}

	private boolean isWall() {
		return magSq >= innerRadiusSquared;
	}

	private boolean isOutside() {
		return magSq > radiusSq + 5;
	}

	protected void placeBlock() {
		if (block == null) {
			return;
		}
		setBlockAndNotifyAdequately(world, origin.add(x, y, z), block);
	}
}
