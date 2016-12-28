package net.torocraft.toroquest.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;
import net.torocraft.toroquest.entities.EntityMage;

public class MageTowerGenerator extends WorldGenerator {

	private int floorHieght = 0;// 8;
	private int radius = 0;// 12;
	private int floors = 0;// 7;

	private int height = 0;// floors * floorHieght + 2;

	protected IBlockState[] aFloorBlock = { Blocks.NETHER_BRICK.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), ((BlockStoneBrick) Blocks.STONEBRICK).getStateFromMeta(1), Blocks.BRICK_BLOCK.getDefaultState() };
	protected IBlockState aWallDecorationBlock[] = { Blocks.GLOWSTONE.getDefaultState(), Blocks.SEA_LANTERN.getDefaultState() };
	protected IBlockState[] aWallBlock = { Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.STONEBRICK.getDefaultState() };
	protected IBlockState[] aWallRandBlock = { ((BlockQuartz) Blocks.QUARTZ_BLOCK).getStateFromMeta(1), Blocks.COBBLESTONE.getDefaultState() };

	protected BlockStairs stairsBlock = (BlockStairs) Blocks.QUARTZ_STAIRS;
	protected IBlockState stairsFoundationBlock = ((BlockStoneBrick) Blocks.STONEBRICK).getStateFromMeta(1);
	protected IBlockState stairsConnectorBlock = Blocks.QUARTZ_BLOCK.getDefaultState();
	protected IBlockState stairsColumnBlock = Blocks.QUARTZ_BLOCK.getDefaultState();

	protected IBlockState wallBlock;
	protected IBlockState wallRandBlock;
	protected IBlockState floorBlock;
	protected IBlockState wallDecorationBlock;

	protected IBlockState windowBlock = Blocks.DARK_OAK_FENCE.getDefaultState();

	public boolean generate(int floors, int radius, int floorHieght, World world, Random rand, BlockPos pos) {
		this.radius = radius;
		this.floors = floors;
		this.floorHieght = floorHieght;
		this.height = floors * floorHieght + 2;
		return generate(world, rand, pos);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		randomizeBlocks(rand);
		randomizeParameters(rand);
		BlockPos surface = findSurface(world, pos);
		if (surface == null) {
			return false;
		}
		placeTower(world, rand, surface);
		spawnMage(world, surface);
		return true;
	}

	protected void randomizeParameters(Random rand) {
		if (radius == 0) {
			radius = 7 + rand.nextInt(12);
		}

		if (floors == 0) {
			floors = 3 + rand.nextInt(7);
		}

		if (floorHieght == 0) {
			floorHieght = 5 + rand.nextInt(5);
		}

		this.height = floors * floorHieght + 2;
	}

	private void randomizeBlocks(Random rand) {
		wallBlock = randomPick(rand, aWallBlock);
		wallRandBlock = randomPick(rand, aWallRandBlock);
		floorBlock = randomPick(rand, aFloorBlock);
		wallDecorationBlock = randomPick(rand, aWallDecorationBlock);
	}

	private IBlockState randomPick(Random rand, IBlockState[] a) {
		// return a[rand.nextInt(a.length)];
		return a[0];
	}

	private void spawnMage(World world, BlockPos pos) {
		EntityMage e = new EntityMage(world);
		e.setPosition(pos.getX() + 3, pos.getY() + (floors * floorHieght) + 1, pos.getZ() + 3);
		world.spawnEntity(e);
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

		spawners = new ArrayList<BlockPos>();

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

		// placeSpikes(world, pos);

		for (BlockPos p : spawners) {
			placeSpawner(world, p.add(pos), randomMob(rand));
		}

	}

	private void placeSpikes(World world, BlockPos pos) {
		int l = radius / 2;
		int h = Long.valueOf(Math.round(l * 1.5)).intValue();

		BlockPos locPos = pos.add(0, height - 1, 0);
		int z = 0;
		for (int y = 0; y <= h; y++) {
			for (int x = l; x <= radius + 1; x++) {

				if ((x - l - 1) > (y / 2) || x == radius + 1) {
					setBlockAndNotifyAdequately(world, locPos.add(x, y, z), wallBlock);
					setBlockAndNotifyAdequately(world, locPos.add(-x, y, z), wallBlock);

					setBlockAndNotifyAdequately(world, locPos.add(z, y, x), wallBlock);
					setBlockAndNotifyAdequately(world, locPos.add(z, y, -x), wallBlock);
				}

			}
		}

	}

	private String randomMob(Random rand) {
		switch (rand.nextInt(7)) {
		case 0:
			return "cave_spider";
		case 1:
			return "blaze";
		case 2:
			return "skeleton";
		case 3:
			return "spider";
		case 5:
			return "zombie";
		case 6:
			return "zombie_pigman";
		}
		return "zombie";
	}

	public static void placeSpawner(World world, BlockPos pos, String mob) {
		placeBlock(world, pos, Blocks.MOB_SPAWNER);
		TileEntityMobSpawner theSpawner = (TileEntityMobSpawner) world.getTileEntity(pos);
		MobSpawnerBaseLogic logic = theSpawner.getSpawnerBaseLogic();
		logic.setEntityId(new ResourceLocation(mob));
	}

	public static void placeBlock(World world, BlockPos pos, net.minecraft.block.Block block) {
		world.setBlockState(pos, block.getDefaultState());
	}

	private void placeTowerBlock(World world, Random rand, BlockPos pos, int radiusSquared, int innerRadiusSquared, int y, int x, int z) {

		int magSq;
		IBlockState block = null;
		magSq = (x * x) + (z * z);

		if (!isOutsideTower(radiusSquared, magSq, y)) {
			block = Blocks.AIR.getDefaultState();
			block = getBlockAtLocation(rand, innerRadiusSquared, magSq, block, y, x, z);
		} else if (y == 0) {
			block = stairsFoundationBlock;
		}

		if (isDoorwayLocation(x, y, z)) {
			block = getDoorwayBlock(x, y, z);
		}

		if (block != null) {
			BlockPos placementPos = pos.add(x, y, z);
			setBlockAndNotifyAdequately(world, placementPos, block);
			addLootToChest(world, block, placementPos);
		}
	}

	protected void addLootToChest(World world, IBlockState block, BlockPos placementPos) {
		if (block.getBlock() == Blocks.CHEST) {
			TileEntity tileentity = world.getTileEntity(placementPos);

			if (tileentity instanceof TileEntityChest) {
				((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, world.rand.nextLong());
			}
		}
	}

	private IBlockState getDoorwayBlock(int x, int y, int z) {
		IBlockState block;
		block = Blocks.AIR.getDefaultState();

		int xm = Math.abs(x);
		int zm = Math.abs(z);

		if (y == 5 || y == 0) {
			block = wallBlock;
		}

		if (xm == 2 || zm == 2) {
			block = wallBlock;
		}
		return block;
	}

	List<BlockPos> spawners;

	private IBlockState getBlockAtLocation(Random rand, int innerRadiusSquared, int magSq, IBlockState currentBlock, int y, int x, int z) {

		if (isFloor(y)) {
			currentBlock = floorBlock;
		}

		if (isWallLocation(innerRadiusSquared, magSq)) {
			currentBlock = getWallBlock(rand, y, x, z);
		}

		if (isChestLocation(rand, x, y, z)) {
			currentBlock = getChestBlock(x, z);
		}

		if (isStairsLocation(x, y, z)) {
			currentBlock = getStairBlock(x, y, z);
		}

		if (isSpawnerLocation(x, y, z)) {
			spawners.add(new BlockPos(x, y, z));
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
			currentBlock = windowBlock;
		} else {
			if (isHelixLocation(y, x, z)) {
				currentBlock = wallDecorationBlock;
			} else {
				if (rand.nextInt(100) > 10) {
					currentBlock = wallBlock;
				} else {
					currentBlock = wallRandBlock;
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
			return stairsFoundationBlock;
		}

		if (x == 0 && z == 0) {
			return stairsColumnBlock;
		}

		int yAdj = (y + 1) % 4;

		switch (yAdj) {
		case 0:
			if (x == 1 && z == 0) {
				return stairsBlock.getStateFromMeta(2);
			}
			if (x == 1 && z == 1) {
				return stairsConnectorBlock;
			}
			break;
		case 1:
			if (x == 0 && z == 1) {
				return stairsBlock.getStateFromMeta(1);
			}
			if (x == -1 && z == 1) {
				return stairsConnectorBlock;
			}
			break;
		case 2:
			if (x == -1 && z == 0) {
				return stairsBlock.getStateFromMeta(3);
			}
			if (x == -1 && z == -1) {
				return stairsConnectorBlock;
			}
			break;
		case 3:
			if (x == 0 && z == -1) {
				return stairsBlock.getStateFromMeta(0);
			}
			if (x == 1 && z == -1) {
				return stairsConnectorBlock;
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

	private boolean isChestLocation(Random rand, int x, int y, int z) {

		if (y < height - 4 && rand.nextInt(10) > 1) {
			return false;
		}

		if (y % floorHieght != 1) {
			return false;
		}
		return (Math.abs(x) == radius - 2 && z == 0) || (Math.abs(z) == radius - 2 && x == 0);
	}

	private boolean isSpawnerLocation(int x, int y, int z) {
		if (y % floorHieght != floorHieght - 2) {
			return false;
		}
		return (Math.abs(x) == radius - (radius / 2) && z == 0) || (Math.abs(z) == radius - (radius / 2) && x == 0);
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
		return y % floorHieght == 0;// || y % FLOOR_HEIGHT == FLOOR_HEIGHT - 2;
	}

}
