package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.block.BlockBush;
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
import net.torocraft.toroquest.entities.EntityMonolithEye;

public class MonolithGenerator extends WorldGenerator {

	private static int shallowsDepth = 50;
	private static int monolithRadius = 0;
	private static int monolithHeightBase = 4;
	private static int underseaHeight = 0;
	private static int eyeRadius = 0;
	private static int eyeHeight = 0;
	private static int eyeFloatHeight = 1;

	private int monolithHeight;
	private int height;

	protected IBlockState getObsidianBlock() {
		return Blocks.OBSIDIAN.getDefaultState();
	}

	protected IBlockState getEyeBlock() {
		return Blocks.SEA_LANTERN.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		setHeight(rand);
		BlockPos surface = findSurface(world, pos);
		if (surface == null) {
			return false;
		}
		placeMonolith(world, rand, surface);
		spawnMonolithEye(world, surface);
		placeDungeonRoom(world, surface.getX(), surface.getZ());
		return true;
	}

	private void placeDungeonRoom(World world, int xCenter, int zCenter) {
		int halfX = 6;
		int halfZ = 6;
		int height = 6;

		int xMin = xCenter - halfX;
		int zMin = zCenter - halfZ;
		int yMin = 10;

		int xMax = xCenter + halfX;
		int zMax = zCenter + halfZ;
		int yMax = yMin + height;

		IBlockState block;
		BlockPos pos;
		Random rand = new Random();

		for (int y = yMin; y <= yMax; y++) {
			for (int x = xMin; x <= xMax; x++) {
				for (int z = zMin; z <= zMax; z++) {
					if (y == yMax) {
						block = Blocks.LAVA.getDefaultState();
					} else if (x == xMin || x == xMax || z == zMax || z == zMin || y == yMax - 1 || y == yMin) {
						block = Blocks.MOSSY_COBBLESTONE.getDefaultState();
					} else if (y == yMin + 1) {
						block = Blocks.LAVA.getDefaultState();
					} else {
						block = Blocks.AIR.getDefaultState();
					}
					pos = new BlockPos(x, y, z);
					setBlockAndNotifyAdequately(world, pos, block);
				}
			}
		}

		for (int x = xMin + 1; x < xMax; x++) {
			for (int z = zMin + 1; z < zMax; z++) {
				if (rand.nextInt(100) < 3) {
					pos = new BlockPos(x, yMin + 1, z);
					placeChest(world, pos);
				}
			}
		}

		for (int x = xMin + 1; x < xMax; x++) {
			for (int z = zMin + 1; z < zMax; z++) {
				if (rand.nextInt(100) < 3) {
					pos = new BlockPos(x, yMax - 1, z);
					placeSpawner(world, pos);
				}
			}
		}

	}

	protected void placeChest(World world, BlockPos placementPos) {
		setBlockAndNotifyAdequately(world, placementPos, Blocks.CHEST.getDefaultState());
		TileEntity tileentity = world.getTileEntity(placementPos);
		if (tileentity instanceof TileEntityChest) {
			((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, world.rand.nextLong());
		}
	}

	public void placeSpawner(World world, BlockPos pos) {
		setBlockAndNotifyAdequately(world, pos, Blocks.MOB_SPAWNER.getDefaultState());
		TileEntityMobSpawner theSpawner = (TileEntityMobSpawner) world.getTileEntity(pos);
		MobSpawnerBaseLogic logic = theSpawner.getSpawnerBaseLogic();
		logic.func_190894_a(new ResourceLocation("magma_cube"));
	}

	private void setHeight(Random rand) {
		monolithHeight = (int) Math.round(monolithHeightBase * (1 + rand.nextDouble()));
		height = monolithHeight + eyeFloatHeight + eyeHeight + 1;
	}

	private void spawnMonolithEye(World world, BlockPos pos) {
		BlockPos entityPos = new BlockPos(pos.getX(), pos.getY() + (monolithHeight + underseaHeight + eyeFloatHeight) - 2, pos.getZ());
		EntityMonolithEye e = new EntityMonolithEye(world);
		e.setPosition(entityPos.getX() + .5, entityPos.getY() + .5, entityPos.getZ() + .5);
		world.spawnEntityInWorld(e);
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

						if (pos == null) {
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
			BlockPos nPos = new BlockPos(oX, y, oZ);
			IBlockState blockState = world.getBlockState(nPos);
			if (isGroundBlock(blockState)) {
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
		pos = new BlockPos(pos.getX(), pos.getY() + monolithHeight - 1, pos.getZ());
		while (pos.getY() > 16) {
			setBlockAndNotifyAdequately(world, pos, Blocks.OBSIDIAN.getDefaultState());
			pos = pos.down();
		}
	}

	public static void placeBlock(World world, BlockPos pos, net.minecraft.block.Block block) {
		world.setBlockState(pos, block.getDefaultState());
	}

}
