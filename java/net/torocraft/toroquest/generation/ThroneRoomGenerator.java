package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlab.EnumType;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class ThroneRoomGenerator extends WorldGenerator {
	
	/*
	width=29
	length=84
	height=15
	
	from back right,
	lava is 5 from the side, 13 from the back, 18 between them
	
	*/
	

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
	protected final IBlockState netherStairsN = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
	protected final IBlockState netherStairsS = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
	protected final IBlockState netherStairsE = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
	protected final IBlockState netherStairsW = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
	
	private int width = 30;
	private int length = 85;
	private int height = 16;
	
	protected final IBlockState oakFence = Blocks.OAK_FENCE.getDefaultState();
	protected final IBlockState chiseledStone = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, net.minecraft.block.BlockStoneBrick.EnumType.CHISELED);
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		buildThroneRoom(worldIn, rand, position);
		spawnKing();
		spawnRainbowGuard();
		return true;
	}

	private void buildThroneRoom(World world, Random rand, BlockPos startPos) {
		for (int y = 0; y <= height; y++) {
			for (int x = 0; x <= width; x++) {
				for (int z = 0; z <= length; z++) {
					IBlockState currentBlock = air;
					if ((x == 0 || x == width) || (y == 0 || y == height) || (z == 0 || z == length)) {
						currentBlock = stone;
					}
					
					if ((y == 0 || y == height) && (x == 5 || x == width - 5) && (z == 15 || z == 34 || z == 52 || z == 70)) {
						if (y == 0) {
							currentBlock = air;
						} else {
							currentBlock = lava;
						}
					}
					
					if (y == 1 && (x == 2 || x == 8 || x == width - 2 || x == width - 8) && ((z - 2) % 6 == 0)) {
						currentBlock = torch;
					}
					
					if (y == 1 && (x > 13 && x < 17) && (z != 0 && z != length)) {
						currentBlock = redCarpet;
					}
					
					if (y == 1 && (x == 13 || x == 17) && (z != 0 && z != length)) {
						currentBlock = stoneBrickSlabs;
					}
						
					setBlockAndNotifyAdequately(world, startPos.add(x, y, z), currentBlock);
				}
			}
		}
	}

	private void spawnKing() {
		// TODO Auto-generated method stub
		
	}

	private void spawnRainbowGuard() {
		// TODO Auto-generated method stub
		
	}

}
