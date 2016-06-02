package net.torocraft.torobasemod.generation;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.torocraft.torobasemod.entities.EntityMage;
import net.torocraft.torobasemod.entities.EntityMonolithEye;

public class MonolithGenerator extends WorldGenerator {

	private int shallowsDepth = 50;
	private int monolithRadius = 0;
	private int monolithHeight = 4;
	private int underseaHeight = 0;
	private int eyeRadius = 0;
	private int eyeHeight = 0;
	private int eyeFloatHeight = 1;
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
		spawnMonolithEye(world, surface);

		//		placeEye(world, rand, surface);
		
		return true;
	}

	private void spawnMonolithEye(World world, BlockPos pos) {
		EntityMonolithEye e = new EntityMonolithEye(world);
		e.setPosition(pos.getX() + .5, pos.getY() + (monolithHeight + underseaHeight + eyeFloatHeight), pos.getZ() + .5);
		
		world.spawnEntityInWorld(e);
		System.out.println("Spawning Monolith Eye [" + e.getPosition() + "]");

	}
	
	private BlockPos findSurface(World world, BlockPos start) {

		int minY = world.getActualHeight();
		int maxY = 0;

		BlockPos pos;

		IBlockState blockState;
		int verticalSpace;

		for (int x = -monolithRadius - 1; x <= monolithRadius + 1; x++) {
			for (int z = -monolithRadius - 1; z <= monolithRadius + 1; z++) {
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
				underseaHeight = (oY - y);
				return nPos;
			}
		}

		return null;
	}
	
	private boolean isGroundBlock(IBlockState blockState) {

		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() == Blocks.LOG || blockState.getBlock() instanceof BlockBush) {
			return false;
		}

		return blockState.isOpaqueCube();
	}

	private void placeMonolith(World world, Random rand, BlockPos pos) {
		int radiusSquared = monolithRadius * monolithRadius;
		int innerRadiusSquared = 0;
		int monolithRealHeight = monolithHeight + underseaHeight;

		for (int y = 0; y < monolithRealHeight; y++) {
			for (int x = -monolithRadius; x <= monolithRadius; x++) {
				for (int z = -monolithRadius; z <= monolithRadius; z++) {
					if((x*x + z*z) <= radiusSquared) {
						placeMonolithBlock(world, rand, pos, radiusSquared, innerRadiusSquared, y, x, z, getObsidianBlock());						
					}
				}
			}
		}
	}

	private void placeEye(World world, Random rand, BlockPos pos) {
		int radiusSquared = eyeRadius * eyeRadius;
		int innerRadiusSquared = 0;
		int monolithRealHeight = monolithHeight + underseaHeight;
		
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
