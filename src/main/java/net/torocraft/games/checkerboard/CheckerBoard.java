package net.torocraft.games.checkerboard;

import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CheckerBoard {

	private final World world;
	private final int xOrigin;
	private final int yOrigin;
	private final int zOrigin;

	private static final IBlockState stairsNorth = Blocks.quartz_stairs.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
	private static final IBlockState stairsSouth = Blocks.quartz_stairs.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
	private static final IBlockState stairsWest = Blocks.quartz_stairs.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
	private static final IBlockState stairsEast = Blocks.quartz_stairs.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);

	private static final IBlockState BORDER = Blocks.quartz_block.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED);
	
	/*
	 * Cursor Variables
	 */
	private int x;
	private int y;
	private int z;
	private IBlockState block;
	
	/*
	 * Draw Flags
	 */
	private boolean blockWasDrawable;

	private boolean onlyPlaceIfAir = false;

	public CheckerBoard(World world, BlockPos position) {
		this.world = world;
		this.xOrigin = position.getX() - 4;
		this.yOrigin = position.getY() + 1;
		this.zOrigin = position.getZ() - 4;
	}

	public void generate() {
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 *    01234567
		 * 
		 */
		placeCheckerBlocks();
		placeBorderBlocks();
		placeBorderStairs();
	}

	private void placeBorderBlocks() {
		block = BORDER;
		z = x = -1;
		drawLine(Axis.X, 10);
		drawLine(Axis.Z, 10);
		drawLine(Axis.X, -10);
		drawLine(Axis.Z, -10);
	}

	private void placeBorderStairs() {
		z = x = -2;
		int length = 12;

		drawStairBorder(length);

		onlyPlaceIfAir = true;

		for (int i = 0; i < 50; i++) {
			z--;
			x--;
			y--;
			length += 2;
			if(!drawStairBorder(length)){
				break;
			}
		}

		onlyPlaceIfAir = false;
	}

	private boolean drawStairBorder(int length) {
		boolean somethingDrawn = false;
		
		block = stairsSouth;
		somethingDrawn = drawLine(Axis.X, length) || somethingDrawn;

		block = stairsWest;
		somethingDrawn = drawLine(Axis.Z, length) || somethingDrawn;

		block = stairsNorth;
		somethingDrawn = drawLine(Axis.X, -length) || somethingDrawn;

		block = stairsEast;
		somethingDrawn = drawLine(Axis.Z, -length) || somethingDrawn;
		
		return somethingDrawn;
	}

	private boolean drawLine(Axis axis, int length) {
		int l = computeTravelDistance(length);
		boolean isPositive = length >= 0;
		boolean somethingDrawn = false;
		for (int i = 0; i < l; i++) {
			placeBlock();
			if (isPositive) {
				incrementAxis(axis, 1);
			} else {
				incrementAxis(axis, -1);
			}
			somethingDrawn = somethingDrawn || blockWasDrawable;
		}
		return somethingDrawn;
	}

	private int computeTravelDistance(int length) {
		return Math.abs(length) - 1;
	}

	private void incrementAxis(Axis axis, int amount) {
		switch (axis) {
		case X:
			x += amount;
			break;
		case Y:
			y += amount;
			break;
		case Z:
			z += amount;
			break;
		default:
			break;
		}
	}

	private void placeCheckerBlocks() {
		for (x = 0; x < 8; x++) {
			for (z = 0; z < 8; z++) {
				placeBlock(defineCheckerBlock());
			}
		}
	}

	private void placeBlock(IBlockState type) {
		if (okToPlaceBlock()) {
			blockWasDrawable = true;
			world.setBlockState(l(), type);
		}else{
			blockWasDrawable = false;
		}
	}

	private void placeBlock() {
		placeBlock(block);
	}

	private boolean okToPlaceBlock() {
		return !onlyPlaceIfAir || onAirBlock();
	}

	private boolean onAirBlock() {
		IBlockState currentBlock = world.getBlockState(l());
		return !currentBlock.isOpaqueCube();
	}

	private BlockPos l() {
		return new BlockPos(xOrigin + x, yOrigin + y, zOrigin + z);
	}

	private IBlockState defineCheckerBlock() {
		if (isWhiteBlock()) {
			return Blocks.quartz_block.getDefaultState();
		} else {
			return Blocks.obsidian.getDefaultState();
		}
	}

	private boolean isWhiteBlock() {
		return (x + z) % 2 == 0;
	}

}
