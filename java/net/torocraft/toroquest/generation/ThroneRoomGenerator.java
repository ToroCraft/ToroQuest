package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlab.EnumType;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.torocraft.toroquest.entities.EntityRainbowGuard;
import net.torocraft.toroquest.entities.EntityRainbowGuard.Color;
import net.torocraft.toroquest.entities.EntityRainbowKing;

public class ThroneRoomGenerator extends WorldGenerator {	

	protected final IBlockState stone = Blocks.STONE.getDefaultState();
	protected final IBlockState stoneBrickSlabs = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, EnumType.SMOOTHBRICK);
	protected final IBlockState stoneBrick = Blocks.STONEBRICK.getDefaultState();
	protected final IBlockState torch = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.UP);
	protected final IBlockState air = Blocks.AIR.getDefaultState();
	protected final IBlockState goldBlock = Blocks.GOLD_BLOCK.getDefaultState();
	protected final IBlockState redstoneBlock = Blocks.REDSTONE_BLOCK.getDefaultState();
	protected final IBlockState lava = Blocks.FLOWING_LAVA.getDefaultState();
	protected final IBlockState redCarpet = Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED);
	protected final IBlockState stoneBrickStairsN = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
	protected final IBlockState stoneBrickStairsS = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
	protected final IBlockState stoneBrickStairsE = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
	protected final IBlockState stoneBrickStairsW = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
	protected final IBlockState oakFence = Blocks.OAK_FENCE.getDefaultState();
	
	private int width = 30;
	private int length = 85;
	private int height = 16;
	
	private World world;
	private BlockPos origin;
	private Random rand;

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos origin) {
		this.world = worldIn;
		this.origin = origin;
		this.rand = rand;
		buildThroneRoom(worldIn, rand, origin);
		spawnKing();
		spawnRainbowGuards();
		return true;
	}

	private void buildThroneRoom(World world, Random rand, BlockPos startPos) {
		for (int y = 0; y <= height; y++) {
			for (int x = 0; x <= width; x++) {
				for (int z = 0; z <= length; z++) {
					IBlockState currentBlock = air;
					if (isWallOrFloor(y, x, z)) {
						currentBlock = stone;
					}
					
					if (isEntranceStoneBrick(y, x, z)) {
						currentBlock = stoneBrick;
					}
					
					if (isEntranceOpening(y, x, z)) {
						currentBlock = air;
					}
					
					if (isEntranceTorchHolder(y, x, z)) {
						currentBlock = oakFence;
					}
					
					if (isEntranceTorch(y, x, z)) {
						currentBlock = torch;
					}
					
					if (z == 0 && y == 7 && x == 13) {
						currentBlock = stoneBrickStairsW.withProperty(BlockStairs.HALF, EnumHalf.TOP);
					}
					
					if (z == 0 && y == 7 && x == 17) {
						currentBlock = stoneBrickStairsE.withProperty(BlockStairs.HALF, EnumHalf.TOP);
					}
					
					if (isLavaLampSourceOrDest(y, x, z)) {
						if (y == 0) {
							currentBlock = air;
						} else {
							currentBlock = lava;
						}
					}
					
					if (isInTorchGrid(y, x, z)) {
						currentBlock = torch;
					}
					
					if (isAisleCarpet(y, x, z) || isThroneCarpet(y, x, z) || isEntranceCarpet(y, x, z)) {
						currentBlock = redCarpet;
					}
					
					if (isAisleEdging(y, x, z)) {
						currentBlock = stoneBrickSlabs;
					}
					
					if (isAisleEdging(y, x, z) && isAisleTorch(z)) {
						currentBlock = stoneBrick;
					}
					
					if (y == 2 && (x == 13 || x == 17) && isAisleTorch(z)) {
						currentBlock = torch;
					}
					
					if (isEntrancePlatform(y, x, z)) {
						currentBlock = stone;
					}
					
					if (isEntranceStairsEast(y, x, z)) {
						currentBlock = stoneBrickStairsE;
					}
					
					if (isEntranceStairsWest(y, x, z)) {
						currentBlock = stoneBrickStairsW;
					}
					
					if (isEntranceStairsNorth(y, x, z)) {
						currentBlock = stoneBrickStairsN;
					}
					
					if (isThronePlatform(y, x, z)) {
						currentBlock = stone;
					}
					
					if (isThroneArms(y, x, z)) {
						currentBlock = goldBlock;
					}
					
					if (isBackOfThrone(y, x, z)) {
						currentBlock = redstoneBlock;
					}
					
					if (isThroneTorch(y, x, z) || isEntrancePlatformTorch(y, x, z)) {
						currentBlock = torch;
					}
					
					if (isThroneStairsNorth(y, x, z)) {
						currentBlock = stoneBrickStairsN;
					}
					
					if (isThroneStairsEast(y, x, z)) {
						currentBlock = stoneBrickStairsE;
					}
					
					if (isThroneStairsWest(y, x, z)) {
						currentBlock = stoneBrickStairsW;
					}
					
					if (isThroneStairsSouth(y, x, z)) {
						currentBlock = stoneBrickStairsS;
					}
					
					if (isThroneSideLavaDitch(y, x, z)) {
						currentBlock = air;
					}
					
					if (isThroneSideLavaFlow(y, x, z) || isEntranceLavaFlow(y, x, z)) {
						currentBlock = lava;
					}
					
					if (isEntranceLavaCatcherRight(y, x, z) || isEntranceLavaCatcherLeft(y, x, z)) {
						currentBlock = stoneBrickStairsN.withProperty(BlockStairs.HALF, EnumHalf.TOP);
					}
					
					if (isEntranceLavaCatcherCornerEast(y, x, z)) {
						currentBlock = stoneBrickStairsE.withProperty(BlockStairs.HALF, EnumHalf.TOP);
					}
					
					if (isEntranceLavaCatcherCornerWest(y, x, z)) {
						currentBlock = stoneBrickStairsW.withProperty(BlockStairs.HALF, EnumHalf.TOP);
					}
					
					setBlockAndNotifyAdequately(world, startPos.add(x, y, z), currentBlock);
				}
			}
		}
	}

	private boolean isAisleEdging(int y, int x, int z) {
		return y == 1 && (x == 13 || x == 17) && (z != 0 && z != length);
	}

	private boolean isAisleCarpet(int y, int x, int z) {
		return y == 1 && (x > 13 && x < 17) && (z != 0 && z != length);
	}

	private boolean isInTorchGrid(int y, int x, int z) {
		return y == 1 && (x == 2 || x == 8 || x == width - 2 || x == width - 8) && ((z - 2) % 6 == 0);
	}

	private boolean isEntranceTorch(int y, int x, int z) {
		return z == 0 && y == 6 && (x == 13 || x == 17);
	}

	private boolean isEntranceTorchHolder(int y, int x, int z) {
		return z == 0 && y == 5 && (x == 13 || x == 17);
	}

	private boolean isWallOrFloor(int y, int x, int z) {
		return (x == 0 || x == width) || (y == 0 || y == height) || (z == 0 || z == length);
	}

	private boolean isEntranceStoneBrick(int y, int x, int z) {
		return (z == 0 && (y == 3 || y == 8) & x >= 12 && x <= 18) || (z == 0 && (x == 12 || x == 18) && y >= 4 && y <= 7) || (z == 0 && (x == 13 || x == 17) && y == 4);
	}

	private boolean isEntranceOpening(int y, int x, int z) {
		return z == 0 && y >= 4 && y <= 7 && x >= 14 && x <= 16;
	}

	private boolean isLavaLampSourceOrDest(int y, int x, int z) {
		return (y == 0 || y == height) && (x == 5 || x == width - 5) && (z == 15 || z == 34 || z == 52 || z == 70);
	}

	private boolean isThroneStairsSouth(int y, int x, int z) {
		return (y == 1 && z == length - 10 && x >= 12 && x <= 18) || (y == 2 && z == length - 9 && x >= 13 && x <= 17) || (y == 3 && z == length - 8 && x >= 14 && x <= 16) || (y == 4 && z == length - 5 && x >= 14 && x <= 16) || (y == 5 && z == length - 4 && x >= 14 && x <= 16);
	}

	private boolean isThroneStairsWest(int y, int x, int z) {
		return (y == 1 && x == 19 && z <= length - 6 && z >= length - 10) || (y == 2 && x == 18 && z <= length - 7 && z >= length - 9) || (y == 3 && x == 17 && z <= length - 7 && z >= length - 8);
	}

	private boolean isThroneStairsEast(int y, int x, int z) {
		return (y == 1 && x == 11 && z <= length - 6 && z >= length - 10) || (y == 2 && x == 12 && z <= length - 7 && z >= length - 9) || (y == 3 && x == 13 && z <= length - 7 && z >= length - 8);
	}

	private boolean isThroneStairsNorth(int y, int x, int z) {
		return (y == 1 && (x == 11 || x == 12 || x == 18 || x == 19) && z == length - 5) || (y == 2 && (x == 12 || x == 18) && z == length - 6);
	}

	private boolean isEntranceLavaCatcherCornerWest(int y, int x, int z) {
		return z == 2 && y == 4 && (x == 7 || x == width - 3);
	}

	private boolean isEntranceLavaCatcherCornerEast(int y, int x, int z) {
		return z == 2 && y == 4 && (x == 3 || x == width - 7);
	}

	private boolean isEntranceLavaCatcherLeft(int y, int x, int z) {
		return (z == 1 && y == 3 && x <= width - 4 && x >= width - 6) || (z == 1 && y == 4 && (x == width - 3 || x == width - 7)) || (z == 2 && y == 4 && x <= width - 4 && x >= width - 6);
	}

	private boolean isEntranceLavaCatcherRight(int y, int x, int z) {
		return (z == 1 && y == 3 && x >= 4 && x <= 6) || (z == 1 && y == 4 && (x == 3 || x == 7)) || (z == 2 && y == 4 && x >= 4 && x <= 6);
	}

	private boolean isEntranceLavaFlow(int y, int x, int z) {
		return z == 0 && y == height - 3 && (x == 5 || x == width - 5);
	}

	private boolean isThroneSideLavaFlow(int y, int x, int z) {
		return z == length && y == height - 3 && (x == 4 || x == 9 || x == width - 4 || x == width - 9);
	}

	private boolean isThroneSideLavaDitch(int y, int x, int z) {
		return y == 0 && z == length - 1 && x != 0 && x != width && (x < 13 || x > 17);
	}

	private boolean isAisleTorch(int z) {
		return z == 9 || z == 11 || z == 16 || z == 21 || z == 23 || z == 28 || z == 33 || z == 35 || z == 40 || z == 45 || z == 47 || z == 52 || z == 57 || z == 59 || z == 64 || z == 69 || z == 71;
	}

	private boolean isBackOfThrone(int y, int x, int z) {
		return (y >= 6 && y <= 10) && x > 13 && x < 17 && z == length - 1;
	}

	private boolean isEntrancePlatformTorch(int y, int x, int z) {
		return (y == 2 && z == 5 && (x == 10 || x == width - 10) || (y == 3 && z == 3 && (x == 12 || x == width - 12)));
	}

	private boolean isEntranceCarpet(int y, int x, int z) {
		return (y == 2 && x > 13 && x < 17 && z == 5) || (y == 3 && z == 3 && x > 13 && x < 17) || (y == 4 && z == 1 && x > 13 && x < 17);
	}

	private boolean isThroneCarpet(int y, int x, int z) {
		return (y == 6 && x > 13 && x < 17 && (z == length - 3 || z == length - 2)) || (y == 4 && x > 13 && x < 17 && (z == length - 6 || z == length - 7));
	}

	private boolean isThroneTorch(int y, int x, int z) {
		return (y == 5 && (x == 13 || x == 17) && z == length - 5) || (y == 8 && (x == 13 || x == 17) && z == length - 1);
	}

	private boolean isThroneArms(int y, int x, int z) {
		return (y == 3 && (x == 13 || x == 17) && z == length - 6) || (y == 4 && (x == 13 || x == 17) && z == length - 5) || (y == 5 && (x == 13 || x == 17) && z == length -4) || 
				(y == 6 && (x == 13 || x == 17) && (z == length - 3 || z == length - 2)) ||	(y == 7 && (x == 13 || x == 17) && z == length - 1);
	}

	private boolean isThronePlatform(int y, int x, int z) {
		return (y == 1 && (x >= 13 && x <= 17) && z >= length - 9) || (y == 1 && (x == 12 || x == 18) && z >= length -9 && z <= length - 6) || (y == 2 && (x >= 13 && x <= 17) && z >= length -8) || (y == 3 && x >= 13 && x <= 17 && z >= length - 5)
				|| (y == 3 && x >= 14 && x <= 16 && z >= length - 7) || (y == 4 && x >= 13 && x <= 17 && z >= length - 4) || (y == 5 && x >= 13 && x <= 17 && z >= length - 3) || (y == 6 && (x == 13 || x == 17) && z == length - 1);
	}

	private boolean isEntranceStairsNorth(int y, int x, int z) {
		return (y == 1 && z == 6 && x >= 10 && x <= width - 10) || (y == 2 && z == 4 && x >= 12 && x <= width - 12) || (y == 3 && z == 2 && x >= 14 && x <= width - 14);
	}

	private boolean isEntranceStairsWest(int y, int x, int z) {
		return (y == 1 && x == width - 9 && z > 0 && z <= 6) || (y == 2 && x == width - 11 && z > 0 && z <= 4) || (y == 3 && x == width - 13 && z > 0 && z <= 2);
	}

	private boolean isEntranceStairsEast(int y, int x, int z) {
		return (y == 1 && x == 9 && z > 0 && z <= 6) || (y == 2 && x == 11 && z > 0 && z <= 4) || (y == 3 && x == 13 && z > 0 && z <= 2);
	}

	private boolean isEntrancePlatform(int y, int x, int z) {
		return (y == 1 && (x >= 10 && x <= width - 10) && z <= 5) || (y == 2 && (x >= 12 && x <= width - 12) && z <= 3) || (y == 3 && (x >= 14 && x <= width - 14) && z == 1);
	}

	private void spawnKing() {
		EntityRainbowKing king = new EntityRainbowKing(world);
		king.setPosition(origin.getX() + 15 + 0.5D, origin.getY() + 7, origin.getZ() + (length - 3) + 0.5D);
		king.onInitialSpawn(world.getDifficultyForLocation(origin), (IEntityLivingData) null);
		world.spawnEntityInWorld(king);
	}

	private void spawnRainbowGuards() {
		spawnRainbowGuard(13, 10, Color.RED);
		spawnRainbowGuard(17, 10, Color.RED);
		spawnRainbowGuard(13, 22, Color.ORANGE);
		spawnRainbowGuard(17, 22, Color.ORANGE);
		spawnRainbowGuard(13, 34, Color.YELLOW);
		spawnRainbowGuard(17, 34, Color.YELLOW);
		spawnRainbowGuard(13, 46, Color.GREEN);
		spawnRainbowGuard(17, 46, Color.GREEN);
		spawnRainbowGuard(13, 58, Color.BLUE);
		spawnRainbowGuard(17, 58, Color.BLUE);
		spawnRainbowGuard(13, 70, Color.PURPLE);
		spawnRainbowGuard(17, 70, Color.PURPLE);
	}

	private void spawnRainbowGuard(int x, int z, Color color) {
		EntityRainbowGuard entity = new EntityRainbowGuard(world);
		entity.setColor(color);
		entity.setPosition(origin.getX() + x + 0.5d, origin.getY() + 2, origin.getZ() + z + 0.5d);
		if (x < 15) {
			entity.setLookAt(origin.add(x + 100, 4, z));
		} else {
			entity.setLookAt(origin.add(x - 100, 4, z));
		}
		entity.onInitialSpawn(world.getDifficultyForLocation(origin), (IEntityLivingData) null);
		world.spawnEntityInWorld(entity);
	}

}
