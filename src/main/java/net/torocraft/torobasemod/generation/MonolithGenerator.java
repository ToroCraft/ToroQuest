package net.torocraft.torobasemod.generation;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;
import net.torocraft.torobasemod.entities.EntityMage;

public class MonolithGenerator extends WorldGenerator {

	private int shallowsDepth = 50;
	private int monolithRadius = 0;
	private int monolithHeight = 20;
	private int eyeRadius = 0;
	private int eyeHeight = 0;
	private int eyeFloatHeight = 2;
	private int height = monolithHeight + eyeFloatHeight + eyeHeight + 1;
	
	protected IBlockState getObsidianBlock() {
		return Blocks.OBSIDIAN.getDefaultState();
	}

	protected IBlockState getEyeBlock() {
		return Blocks.SEA_LANTERN.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		BlockPos surface = findSurface(world, pos);

		if (surface == null) {
			return false;
		}

		System.out.println("Spawning Monolith [" + surface + "]");

		placeMonolith(world, rand, surface);

		return true;
	}

	private BlockPos findSurface(World world, BlockPos start) {

		int minY = world.getActualHeight();
		int maxY = 0;

		BlockPos pos;

		int radiusSquared = monolithRadius * monolithRadius;
		int magSq;
		IBlockState blockState;
		int verticalSpace;

		for (int x = -monolithRadius - 1; x <= monolithRadius + 1; x++) {
			for (int z = -monolithRadius - 1; z <= monolithRadius + 1; z++) {
				magSq = (x * x) + (z * z);

				verticalSpace = 0;

				for (int y = world.getActualHeight(); y > 0; y--) {

					pos = new BlockPos(start.getX() + x, y, start.getZ() + z);
					blockState = world.getBlockState(pos);

					if (isLiquid(blockState)) {
						pos = findSeafloor(world, pos);

						if(pos == null) {
							return null;							
						}
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
	
	private BlockPos findSeafloor(World world, BlockPos pos) {
		int oY = pos.getY();
		int oX = pos.getX();
		int oZ = pos.getZ();

		for (int y = oY; y >= oY - shallowsDepth; y--) {
			BlockPos nPos = new BlockPos(oX,y,oZ);
			IBlockState blockState = world.getBlockState(nPos);
			if(isGroundBlock(blockState)) {
				monolithHeight += oY - y;
				return nPos;
			}
		}

		return null;
	}
	
	private boolean isShallow(BlockPos pos, World world) {
		BlockPos depthPos = new BlockPos(pos.getX(),pos.getY()+3, pos.getZ());
		IBlockState blockState = world.getBlockState(depthPos);
		
		
		return blockState.getBlock() == Blocks.AIR;
	}
	
	private boolean isGroundBlock(IBlockState blockState) {

		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() == Blocks.LOG || blockState.getBlock() instanceof BlockBush) {
			return false;
		}

		return blockState.isOpaqueCube();

	}

	private void placeMonolith(World world, Random rand, BlockPos pos) {
		int radiusSquared = monolithRadius * monolithRadius;
		int innerRadiusSquared = (monolithRadius - 2) * monolithRadius;
		int monolithRealHeight = (int) Math.round(monolithHeight * rand.nextDouble());

		for (int y = 0; y < monolithRealHeight; y++) {
			for (int x = -monolithRadius; x <= monolithRadius; x++) {
				for (int z = -monolithRadius; z <= monolithRadius; z++) {
					placeMonolithBlock(world, rand, pos, radiusSquared, innerRadiusSquared, y, x, z, getObsidianBlock());
				}
			}
		}
		
		for (int y = monolithRealHeight + eyeFloatHeight; y <= monolithRealHeight + eyeHeight + eyeFloatHeight; y++) {
			for (int x = -eyeRadius; x <= eyeRadius; x++) {
				for (int z = -eyeRadius; z <= eyeRadius; z++) {
					placeMonolithBlock(world, rand, pos, radiusSquared, innerRadiusSquared, y, x, z, getEyeBlock());
				}
			}
		}

	}

	public static void placeBlock(World world, BlockPos pos, net.minecraft.block.Block block) {
		world.setBlockState(pos, block.getDefaultState());
	}

	private void placeMonolithBlock(World world, Random rand, BlockPos pos, int radiusSquared, int innerRadiusSquared, int y, int x, int z, IBlockState block) {

		if (block != null) {
			BlockPos placementPos = pos.add(x, y, z);
			setBlockAndNotifyAdequately(world, placementPos, block);
		}
	}
}
