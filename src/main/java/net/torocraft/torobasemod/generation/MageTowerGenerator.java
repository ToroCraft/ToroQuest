package net.torocraft.torobasemod.generation;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class MageTowerGenerator extends WorldGenerator {

	private static final int FLOOR_HEIGHT = 8;
	private int radius = 12;
	private int floors = 7;
	private int height = floors * FLOOR_HEIGHT + 2;

	protected IBlockState getFloorBlock() {
		return ((BlockPlanks) Blocks.PLANKS).getStateFromMeta(5);
	}

	protected IBlockState getWallBlock() {
		return Blocks.QUARTZ_BLOCK.getDefaultState();
	}

	protected IBlockState getWallDecorationBlock() {
		// return ((BlockQuartz) Blocks.QUARTZ_BLOCK).getStateFromMeta(4);

		return Blocks.GLOWSTONE.getDefaultState();
	}

	protected IBlockState getWallRandBlock() {
		return ((BlockQuartz) Blocks.QUARTZ_BLOCK).getStateFromMeta(1);
	}

	protected BlockStairs getStairsBlock() {
		return (BlockStairs) Blocks.QUARTZ_STAIRS;
	}

	protected IBlockState getStairsFoundationBlock() {
		return ((BlockStoneBrick) Blocks.STONEBRICK).getStateFromMeta(1);
	}

	protected IBlockState getStairsConnectorBlock() {
		return Blocks.QUARTZ_BLOCK.getDefaultState();
	}

	protected IBlockState getStairsColumnBlock() {
		return Blocks.QUARTZ_BLOCK.getDefaultState();
	}

	protected IBlockState getWindowBlock() {
		return Blocks.DARK_OAK_FENCE.getDefaultState();
		// return ((BlockGlass)Blocks.GLASS).getStateId(15);
	}

	public boolean generate(int floors, int radius, World world, Random rand, BlockPos pos) {
		this.radius = radius;
		this.floors = floors;
		this.height = floors * FLOOR_HEIGHT + 2;
		return generate(world, rand, pos);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		BlockPos surface = findSurface(world, pos);

		if (surface == null) {
			return false;
		}

		placeTower(world, rand, surface);
		return true;
	}

	private BlockPos findSurface(World world, BlockPos start) {

		int minY = world.getActualHeight();
		int maxY = 0;

		BlockPos pos;

		int radiusSquared = radius * radius;
		int magSq;
		IBlockState blockState;
		int verticalSpace;

		for (int x = -radius - 1; x <= radius + 1; x++) {
			for (int z = -radius - 1; z <= radius + 1; z++) {
				magSq = (x * x) + (z * z);
				if (isOutsideTower(radiusSquared, magSq, 0)) {
					continue;
				}

				verticalSpace = 0;

				for (int y = world.getActualHeight(); y > 0; y--) {

					pos = new BlockPos(start.getX() + x, y, start.getZ() + z);
					blockState = world.getBlockState(pos);

					if (isLiquid(blockState)) {
						return null;
					}

					if (isGroundBlock(blockState)) {
						if (y < minY) {
							minY = y;
						}
						if (y > maxY) {
							maxY = y;
						}

						if (verticalSpace < height) {
							return null;
						}

						break;
					}

					verticalSpace++;
				}

			}
		}

		if (maxY - minY > 4) {
			return null;
		}

		return new BlockPos(start.getX(), minY, start.getZ());
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

	private void placeTower(World world, Random rand, BlockPos pos) {
		int radiusSquared = radius * radius;
		int innerRadiusSquared = (radius - 2) * radius;
		int magSq;

		for (int y = 0; y < 6; y++) {
			for (int x = -radius - 1; x <= radius + 1; x++) {
				for (int z = -radius - 1; z <= radius + 1; z++) {
					placeTowerBlock(world, rand, pos, radiusSquared, innerRadiusSquared, y, x, z);
				}
			}
		}

		for (int y = 6; y < height; y++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					placeTowerBlock(world, rand, pos, radiusSquared, innerRadiusSquared, y, x, z);
				}
			}
		}

	}

	private void placeTowerBlock(World world, Random rand, BlockPos pos, int radiusSquared, int innerRadiusSquared, int y, int x, int z) {

		int magSq;
		IBlockState block = null;
		magSq = (x * x) + (z * z);

		if (!isOutsideTower(radiusSquared, magSq, y)) {
			block = Blocks.AIR.getDefaultState();
			block = getBlockAtLocation(rand, innerRadiusSquared, magSq, block, y, x, z);
		} else if (y == 0) {
			block = getStairsFoundationBlock();
		}

		if (isDoorwayLocation(x, y, z)) {
			block = getDoorwayBlock(x, y, z);
		}

		if (block != null) {
			setBlockAndNotifyAdequately(world, pos.add(x, y, z), block);
		}
	}

	private IBlockState getDoorwayBlock(int x, int y, int z) {
		IBlockState block;
		block = Blocks.AIR.getDefaultState();

		int xm = Math.abs(x);
		int zm = Math.abs(z);

		if (y == 5 || y == 0) {
			block = getWallBlock();
		}

		if (xm == 2 || zm == 2) {
			block = getWallBlock();
		}
		return block;
	}

	private IBlockState getBlockAtLocation(Random rand, int innerRadiusSquared, int magSq, IBlockState currentBlock, int y, int x, int z) {
		if (isFloor(y)) {
			currentBlock = getFloorBlock();
		}

		if (isWallLocation(innerRadiusSquared, magSq)) {
			currentBlock = getWallBlock(rand, y, x, z);
		}

		if (isChestLocation(innerRadiusSquared, magSq, x, y, z)) {
			currentBlock = getChestBlock(x, z);
		}

		if (isStairsLocation(x, y, z)) {
			currentBlock = getStairBlock(x, y, z);
		}

		return currentBlock;
	}

	private boolean isDoorwayLocation(int x, int y, int z) {
		if (y >= 0 && y < 6) {
			int xm = Math.abs(x);
			int zm = Math.abs(z);
			return isXDoorwayLocation(xm, zm) || isZDoorwayLocation(xm, zm);
		}
		return false;
	}

	private boolean isXDoorwayLocation(int xm, int zm) {
		return (xm <= 2 && zm >= (radius - 2));
	}

	private boolean isZDoorwayLocation(int xm, int zm) {
		return (zm <= 2 && xm >= (radius - 2));
	}

	private IBlockState getChestBlock(int x, int z) {
		IBlockState currentBlock;
		if (x > 1) {
			currentBlock = ((BlockChest) Blocks.CHEST).getStateFromMeta(4);
		} else if (x < -1) {
			currentBlock = ((BlockChest) Blocks.CHEST).getStateFromMeta(5);
		} else if (z > 1) {
			currentBlock = ((BlockChest) Blocks.CHEST).getStateFromMeta(2);
		} else if (z < -1) {
			currentBlock = ((BlockChest) Blocks.CHEST).getStateFromMeta(3);
		} else {
			currentBlock = Blocks.AIR.getDefaultState();
		}

		return currentBlock;
	}

	private IBlockState getWallBlock(Random rand, int y, int x, int z) {
		IBlockState currentBlock;
		if (isWindowLocation(x, y, z)) {
			currentBlock = getWindowBlock();
		} else {

			// if (x - (y % FLOOR_HEIGHT) <= 1) { wegdes

			// if (Math.abs(x - z) <= 1) { //corner columns WIP

			if (isHelixLocation(y, x, z)) {
				currentBlock = getWallDecorationBlock();
			} else {
				if (rand.nextInt(100) > 10) {
					currentBlock = getWallBlock();
				} else {
					currentBlock = getWallRandBlock();
				}
			}
		}
		return currentBlock;
	}

	private IBlockState getStairBlock(int x, int y, int z) {

		if (y > height - 2) {
			return Blocks.AIR.getDefaultState();
		}

		if (y < 1) {
			return getStairsFoundationBlock();
		}

		if (x == 0 && z == 0) {
			return getStairsColumnBlock();
		}

		int yAdj = (y + 1) % 4;

		switch (yAdj) {
		case 0:
			if (x == 1 && z == 0) {
				return getStairsBlock().getStateFromMeta(2);
			}
			if (x == 1 && z == 1) {
				return getStairsConnectorBlock();
			}
			break;
		case 1:
			if (x == 0 && z == 1) {
				return getStairsBlock().getStateFromMeta(1);
			}
			if (x == -1 && z == 1) {
				return getStairsConnectorBlock();
			}
			break;
		case 2:
			if (x == -1 && z == 0) {
				return getStairsBlock().getStateFromMeta(3);
			}
			if (x == -1 && z == -1) {
				return getStairsConnectorBlock();
			}
			break;
		case 3:
			if (x == 0 && z == -1) {
				return getStairsBlock().getStateFromMeta(0);
			}
			if (x == 1 && z == -1) {
				return getStairsConnectorBlock();
			}
			break;
		default:
			break;
		}
		return Blocks.AIR.getDefaultState();
	}

	private boolean isStairsLocation(int x, int y, int z) {
		return Math.abs(x) < 2 && Math.abs(z) < 2;
	}

	private boolean isHelixLocation(int y, int x, int z) {
		int angle = (int) Math.round(getAngle(x, z));
		int coAngle = coAngle(angle);
		int yAdj = (y * 16) % 360;
		return isClose(yAdj, angle, 20) || isClose(yAdj, coAngle(angle), 20);
	}

	private int coAngle(int angle) {
		int a = angle - 180;

		if (a < 0) {
			a += 360;
		}

		return a;
	}

	public static boolean isClose(int a, int b, int tolerance) {
		return Math.abs(a - b) <= tolerance;
	}

	public static double getAngle(int x, int z) {
		double angle = Math.toDegrees(Math.atan2((double) z, (double) x));
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	private boolean isWallLocation(int innerRadiusSquared, int magSq) {
		return magSq >= innerRadiusSquared;
	}

	private boolean isChestLocation(int innerRadiusSquared, int magSq, int x, int y, int z) {
		if (y % FLOOR_HEIGHT != 1) {
			return false;
		}
		return (Math.abs(x) == radius - 2 && z == 0) || (Math.abs(z) == radius - 2 && x == 0);
	}

	private boolean isWindowLocation(int x, int y, int z) {
		/*
		 * int offset = y % FLOOR_HEIGHT; return offset > 1 && offset < 5 &&
		 * (Math.abs(x) >= radius - 1 || Math.abs(z) >= radius - 1);
		 */
		return false;
	}

	private boolean isOutsideTower(int radiusSquared, int magSq, int y) {

		if (y < height - 3) {
			return magSq > radiusSquared - 2;
		}

		return magSq > radiusSquared + 25;
	}

	private boolean isFloor(int y) {
		return y % FLOOR_HEIGHT == 0;
	}

}
