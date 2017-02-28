package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootTableList;
import net.torocraft.toroquest.entities.EntityBas;

public class BastionsLairGenerator extends WorldGenerator {

	private final int width = 40;
	private final int height = 25;

	private BlockPos origin;
	private int x, y, z;
	private World world;
	private Random rand;
	private IBlockState block;

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		this.world = world;
		this.rand = rand;

		findOrigin(pos);

		if (origin == null) {
			return false;
		}

		genMainChamber();
		genEntrance(width + 2, 0, EnumFacing.WEST);
		genEntrance(0, -width - 2, EnumFacing.NORTH);
		genEntrance(0, width + 2, EnumFacing.SOUTH);
		genEntrance(-width - 2, 0, EnumFacing.EAST);
		spawnBas();
		return true;
	}

	protected void findOrigin(BlockPos pos) {
		origin = null;
		BlockPos a = getSurfacePosition(pos.add(width, 0, 0));
		BlockPos b = getSurfacePosition(pos.add(0, 0, -width));
		BlockPos c = getSurfacePosition(pos.add(0, 0, width));
		BlockPos d = getSurfacePosition(pos.add(-width, 0, 0));

		if (a == null || b == null || c == null || d == null) {
			return;
		}

		origin = a;

		if (b.getY() < origin.getY()) {
			origin = b;
		}

		if (c.getY() < origin.getY()) {
			origin = c;
		}

		if (d.getY() < origin.getY()) {
			origin = d;
		}

		origin = origin.down(50);

		if (origin.getY() < 5) {
			origin = new BlockPos(origin.getX(), 5, origin.getZ());
		}
	}

	protected void genEntrance(int x, int z, EnumFacing facing) {
		BastionsLairEntranceGenerator g = new BastionsLairEntranceGenerator();
		g.setEntrance(facing);
		g.generate(world, rand, new BlockPos(x, walkwayHeight, z).add(origin));
	}

	private BlockPos getSurfacePosition(BlockPos start) {
		IBlockState blockState;
		BlockPos search = new BlockPos(start.getX(), world.getActualHeight(), start.getZ());
		while (search.getY() > 0) {
			search = search.down();
			blockState = world.getBlockState(search);
			if (isLiquid(blockState)) {
				return null;
			}
			if (isGroundBlock(blockState)) {
				break;
			}
		}
		return search;
	}

	private boolean isLiquid(IBlockState blockState) {
		return blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.LAVA;
	}

	private boolean isGroundBlock(IBlockState blockState) {
		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() == Blocks.LOG
				|| blockState.getBlock() instanceof BlockBush) {
			return false;
		}
		return blockState.isOpaqueCube();
	}

	private void genMainChamber() {
		for (y = 0; y <= height; y++) {
			for (x = -width; x <= width; x++) {
				for (z = -width; z <= width; z++) {
					placeTombBlock();
				}
			}
		}
	}

	private int radiusSq;
	private final int walkwayHeight = height - 10;

	protected void placeTombBlock() {
		block = null;

		radiusSq = (x * x) + (z * z);

		if (isOutside()) {
			return;

		} else if (isWall() || isRoof()) {
			block = Blocks.STONE.getDefaultState();

		} else if (isFloor()) {
			block = Blocks.DIRT.getDefaultState();

		} else if (isPlatform()) {

			if (isPlatformUnderstructure()) {
				if (isHiddenUnderPlatformChest()) {
					block = Blocks.CHEST.getDefaultState();
				} else {
					block = Blocks.DIRT.getDefaultState();
				}
			} else if (isPlatformEdge()) {
				block = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH);
			} else {
				block = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH);
			}

		} else if (isWalkway()) {

			if (isWalkwayEdge()) {
				block = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH);
			} else {
				block = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH);
			}

		} else if (isWalkwayTorch()) {
			block = Blocks.REDSTONE_TORCH.getDefaultState();

		} else if (isWalkwaySubstructure()) {
			block = Blocks.DIRT.getDefaultState();

		} else if (isLootChest()) {
			block = randomChest();
		} else {
			block = Blocks.AIR.getDefaultState();
		}

		placeBlock();
		addLootToChest();
	}

	protected boolean isHiddenUnderPlatformChest() {
		return y == 1 && x == 0 && z == 0;
	}

	private IBlockState randomChest() {
		int roll = rand.nextInt(4);
		switch (roll) {
		case 1:
			return Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH);
		case 2:
			return Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.SOUTH);
		case 3:
			return Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST);
		default:
			return Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST);
		}
	}

	protected void addLootToChest() {
		if (block == null) {
			return;
		}
		if (block.getBlock() == Blocks.CHEST) {
			TileEntity tileentity = world.getTileEntity(origin.add(x, y, z));
			if (tileentity instanceof TileEntityChest) {
				((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_END_CITY_TREASURE, world.rand.nextLong());
			}
		}
	}

	private boolean isLootChest() {
		return y == 1 && rand.nextInt(400) == 0;
	}

	private boolean isPlatformUnderstructure() {
		return y < walkwayHeight;
	}

	protected boolean isPlatform() {
		return y <= walkwayHeight && radiusSq < 55;
	}

	protected boolean isPlatformEdge() {
		return y == walkwayHeight && radiusSq > 45;
	}

	private boolean isWalkwayEdge() {
		if (Math.abs(x) <= 3 && Math.abs(z) <= 3) {
			return false;
		}
		return Math.abs(x) == 3 || Math.abs(z) == 3;
	}

	private boolean isWalkwaySubstructure() {

		if (y >= walkwayHeight || y < walkwayHeight - 5) {
			return false;
		}

		if (!(Math.abs(x) < 4 || Math.abs(z) < 4)) {
			return false;
		}

		int distanceUnderWalkway = walkwayHeight - y;

		if (distanceUnderWalkway > 5 || distanceUnderWalkway < 1) {
			return false;
		}

		int distanceFromWalkwayMiddle = Math.min(Math.abs(z), Math.abs(x));

		return distanceFromWalkwayMiddle + distanceUnderWalkway < 5;
	}

	private boolean isWalkway() {
		return y == walkwayHeight && (Math.abs(x) < 4 || Math.abs(z) < 4);
	}

	private boolean isWalkwayTorch() {
		return y == walkwayHeight + 1 && isWalkwayEdge() && (x % 6 == 0 || z % 6 == 0) && (Math.abs(x) > 4 || Math.abs(z) > 4);
	}

	private boolean isFloor() {
		return y == 0;
	}

	private boolean isRoof() {
		return y == height;
	}

	protected void placeBlock() {
		if (block == null) {
			return;
		}
		setBlockAndNotifyAdequately(world, origin.add(x, y, z), block);
	}

	private boolean isWall() {
		return Math.abs(x) + Math.abs(z) == width + 4;
	}

	protected boolean isOutside() {
		return Math.abs(x) + Math.abs(z) > width + 4;
	}

	private void spawnBas() {
		EntityBas e = new EntityBas(world);
		e.setPosition(origin.getX() + 0.5, origin.getY() + walkwayHeight + 1, origin.getZ() + 0.5);
		e.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(e)), (IEntityLivingData) null);
		world.spawnEntityInWorld(e);
	}

}
