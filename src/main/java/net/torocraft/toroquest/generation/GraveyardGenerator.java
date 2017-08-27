package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.torocraft.toroquest.item.armor.ItemRoyalArmor;

public class GraveyardGenerator extends WorldGenerator {

	private BlockPos origin;
	private World world;
	private Random rand;
	private IBlockState block;

	private static final IBlockState GRAVE_SIDE = Blocks.GRAVEL.getDefaultState();

	private static final IBlockState GRAVE_TOP = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH);
	private static final IBlockState FENCE = Blocks.OAK_FENCE.getDefaultState();
	private static final IBlockState GRAVE_STONE = Blocks.STANDING_SIGN.getDefaultState();

	private static final IBlockState GRAVE_TOP_2 = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE_SMOOTH);
	private static final IBlockState FENCE_2 = Blocks.COBBLESTONE_WALL.getDefaultState();
	private static final IBlockState GRAVE_STONE_2 = Blocks.COBBLESTONE_WALL.getDefaultState();

	@Override
	public boolean generate(World world, Random rand, BlockPos origin) {
		this.world = world;
		this.rand = rand;
		this.origin = origin;

		int rows = 1 + rand.nextInt(3);
		int cols = 2 + rand.nextInt(4);
		int theme = rand.nextInt(2);

		if (!levelEnough(origin, rows, cols)) {
			return false;
		}

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				genGrave(theme, col * 2, row * 4);
			}
		}

		for (int x = -2; x <= cols * 2; x++) {
			place(fencePos(x, -2), getWall(theme));
			place(fencePos(x, (rows * 4) - 1), getWall(theme));
		}

		for (int z = -1; z <= rows * 4 - 2; z++) {
			place(fencePos(-2, z), getWall(theme));
			place(fencePos(2 * cols, z), getWall(theme));
		}

		return true;
	}

	private boolean levelEnough(BlockPos pos, int rows, int cols) {
		int max = 0, min = 1000;
		int y;
		BlockPos surf;
		for (int x = -2; x <= cols * 2; x++) {
			for (int z = -1; z <= rows * 4 - 2; z++) {
				surf = findSurface(pos.add(x, 0, z));
				if (surf == null) {
					return false;
				}
				y = surf.getY();
				max = Math.max(y, max);
				min = Math.min(y, min);
			}
		}
		return max - min < 5;
	}

	private IBlockState getWall(int theme) {
		if (theme == 0) {
			return FENCE;
		} else {
			return FENCE_2;
		}
	}

	private IBlockState getGraveTop(int theme) {
		if (theme == 0) {
			return GRAVE_TOP;
		} else {
			return GRAVE_TOP_2;
		}
	}

	private IBlockState getGraveStone(int theme) {
		if (theme == 0) {
			return GRAVE_STONE;
		} else {
			return GRAVE_STONE_2;
		}
	}

	protected BlockPos fencePos(int x, int z) {
		BlockPos sur = findSurface(origin.add(x, 0, z));
		if (sur == null) {
			return null;
		}
		return sur.add(0, 1, 0);
	}

	private void genGrave(int theme, int x, int z) {
		BlockPos surface = findSurface(origin.add(x, 0, z));

		if (surface == null) {
			return;
		}

		place(surface.add(0, 0, 0), getGraveTop(theme));
		place(surface.add(0, 0, 1), getGraveTop(theme));

		placeChest(surface.add(0, -1, 0));
		placeChest(surface.add(0, -1, 1));

		placeDownward(surface.add(1, 0, -1), Blocks.GRAVEL.getDefaultState());
		placeDownward(surface.add(1, 0, 0), Blocks.GRAVEL.getDefaultState());
		placeDownward(surface.add(1, 0, 1), Blocks.GRAVEL.getDefaultState());
		placeDownward(surface.add(1, 0, 2), Blocks.GRAVEL.getDefaultState());

		placeDownward(surface.add(-1, 0, -1), Blocks.GRAVEL.getDefaultState());
		placeDownward(surface.add(-1, 0, 0), Blocks.GRAVEL.getDefaultState());
		placeDownward(surface.add(-1, 0, 1), Blocks.GRAVEL.getDefaultState());
		placeDownward(surface.add(-1, 0, 2), Blocks.GRAVEL.getDefaultState());

		placeDownward(surface.add(0, 0, -1), Blocks.GRAVEL.getDefaultState());
		placeDownward(surface.add(0, 0, 2), Blocks.GRAVEL.getDefaultState());

		place(surface.add(1, 1, -1), Blocks.AIR.getDefaultState());
		place(surface.add(1, 1, 0), Blocks.AIR.getDefaultState());
		place(surface.add(1, 1, 1), Blocks.AIR.getDefaultState());
		place(surface.add(1, 1, 2), Blocks.AIR.getDefaultState());
		place(surface.add(-1, 1, -1), Blocks.AIR.getDefaultState());
		place(surface.add(-1, 1, 0), Blocks.AIR.getDefaultState());
		place(surface.add(-1, 1, 1), Blocks.AIR.getDefaultState());
		place(surface.add(-1, 1, 2), Blocks.AIR.getDefaultState());
		place(surface.add(0, 1, -1), Blocks.AIR.getDefaultState());
		place(surface.add(0, 1, 0), Blocks.AIR.getDefaultState());
		place(surface.add(0, 1, 1), Blocks.AIR.getDefaultState());
		place(surface.add(0, 1, 2), Blocks.AIR.getDefaultState());

		place(surface.add(0, 1, -1), getGraveStone(theme));

	}

	protected void placeChest(BlockPos pos) {
		place(pos, Blocks.CHEST.getDefaultState());
		addLootToChest(pos);
	}

	protected void addLootToChest(BlockPos placementPos) {
		TileEntity tileentity = world.getTileEntity(placementPos);
		if (tileentity instanceof TileEntityChest) {
			addLootToChest((TileEntityChest) tileentity);
		}
	}

	private static final Item[] NICE_STUFF = { Items.DIAMOND, Items.EMERALD, Items.GOLD_INGOT, ItemRoyalArmor.helmetItem };

	protected void addLootToChest(IInventory chest) {
		for (int i = 0; i < chest.getSizeInventory(); i++) {
			int roll = rand.nextInt(8);

			if (roll == 0) {
				chest.setInventorySlotContents(i, new ItemStack(Items.BONE, rand.nextInt(5) + 1));
			} else if (roll == 1) {
				chest.setInventorySlotContents(i, new ItemStack(Items.ROTTEN_FLESH, rand.nextInt(5) + 1));
			} else if (roll == 2) {
				if (rand.nextInt(20) == 0) {
					chest.setInventorySlotContents(i, new ItemStack(NICE_STUFF[rand.nextInt(NICE_STUFF.length)]));
				}
			}
		}
	}

	private void placeDownward(BlockPos pos, IBlockState block) {
		IBlockState blockState = null;
		while (pos.getY() > 0 && !isGroundBlock(blockState)) {
			place(pos, block);
			pos = pos.down();
			blockState = world.getBlockState(pos);
		}
	}

	private BlockPos findSurface(BlockPos start) {
		IBlockState blockState;
		BlockPos surface = new BlockPos(start.getX(), world.getActualHeight(), start.getZ());
		while (surface.getY() > 5) {
			blockState = world.getBlockState(surface);
			if (isLiquid(blockState)) {
				return null;
			}
			if (isGroundBlock(blockState)) {
				return surface;
			}
			surface = surface.down();
		}
		return null;
	}

	private boolean isLiquid(IBlockState blockState) {
		return blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.LAVA;
	}

	private boolean isGroundBlock(IBlockState blockState) {
		if (blockState == null || blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() == Blocks.LOG || blockState.getBlock() == Blocks.LOG2
				|| blockState.getBlock() instanceof BlockBush) {
			return false;
		}
		return blockState.isOpaqueCube();
	}

	protected void place(BlockPos pos, IBlockState block) {
		if (block == null || pos == null) {
			return;
		}
		setBlockAndNotifyAdequately(world, pos, block);
	}

}
