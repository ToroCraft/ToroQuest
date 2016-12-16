package net.torocraft.toroquest.generation;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BastionsLairGenerator extends WorldGenerator {

	private final int width = 40;
	private final int height = 25;
	private final int bottomMargin = 2;

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
			System.out.println("unable to find origin");
			return false;
		}

		System.out.println("start [" + pos + " ] origin [" + origin + "]");

		place();
		return true;
	}

	private void findOrigin(BlockPos start) {
		origin = null;
		BlockPos pos = new BlockPos(start);
		IBlockState blockState;
		while (pos.getY() > bottomMargin) {
			System.out.println("testing " + pos);
			pos = pos.down();
			blockState = world.getBlockState(pos);
			if (isLiquid(blockState)) {
				return;
			}
			if (isGroundBlock(blockState)) {
				origin = pos.down(30);
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

	private void place() {
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

		} else if (isWall() || isFloor() || isRoof()) {
			block = Blocks.STONEBRICK.getDefaultState();

		} else if (isPlatform()) {

			if (isPlatformUnderstructure()) {
				block = Blocks.DIRT.getDefaultState();
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

		} else if (isWater()) {
			block = Blocks.WATER.getDefaultState();
		} else {
			block = Blocks.AIR.getDefaultState();
		}

		placeBlock();
	}

	private boolean isWater() {
		return y == 1;
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

	private boolean isRoof() {
		return y == 0;
	}

	private boolean isFloor() {
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

}
