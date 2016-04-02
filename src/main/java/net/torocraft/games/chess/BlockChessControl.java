package net.torocraft.games.chess;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.games.ToroGamesMod;
import net.torocraft.games.checkerboard.CheckerBoard;
import net.torocraft.games.chess.pieces.enities.EntityBishop;
import net.torocraft.games.chess.pieces.enities.EntityChessPiece;
import net.torocraft.games.chess.pieces.enities.EntityKing;
import net.torocraft.games.chess.pieces.enities.EntityKnight;
import net.torocraft.games.chess.pieces.enities.EntityPawn;
import net.torocraft.games.chess.pieces.enities.EntityQueen;
import net.torocraft.games.chess.pieces.enities.EntityRook;
import net.torocraft.games.chess.pieces.enities.IChessPiece.Side;

public class BlockChessControl extends Block {

	public static final String NAME = "chess_control";

	public BlockChessControl() {
		super(Material.ground);
		setUnlocalizedName(NAME);
		setResistance(0.1f);
		setHardness(0.5f);
		setLightLevel(0);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {

		if (!worldIn.isRemote) {
			new CheckerBoard().generate(worldIn, pos);
			worldIn.scheduleUpdate(pos, this, 4);
		}

		if (placer != null) {
			placer.moveEntity(0, 2, 0);
		}

		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			placePieces(worldIn, pos);
			addWand(worldIn, pos, 0);
		}
	}
	/*
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityChessControl();
	}*/
	
	/*00
	public static class TileEntityChessControl extends TileEntity {
		public static final String NAME = "tile-entity-chess-control";

	}*/

	/*
	 * /** Called when a neighboring block changes.
	 *
	 * public void onNeighborBlockChange(World worldIn, BlockPos pos,
	 * IBlockState state, Block neighborBlock) { if (worldIn.isRemote) { return;
	 * } updateOnState(worldIn, pos); if (isTurningOn()) {
	 * System.out.println("placePieces"); placePieces(); } else {
	 * worldIn.scheduleUpdate(pos, this, 8); } }
	 * 
	 * 
	 * private void updateOnState(World worldIn, BlockPos pos) { wasOn = isOn;
	 * isOn = worldIn.isBlockPowered(pos); }
	 * 
	 * public void updateTick(World worldIn, BlockPos pos, IBlockState state,
	 * Random rand) { if (worldIn.isRemote) { return; }
	 * 
	 * if (!worldIn.isBlockPowered(pos)) { wasOn = false; isOn = false; } }
	 * 
	 * private boolean isTurningOn() { return !wasOn && isOn; }
	 */

	public String getPositionName(BlockPos gamePos, BlockPos pos) {
		return CheckerBoard.getPositionName(gamePos, pos);
	}

	public BlockPos getPosition(BlockPos gamePos, String name) {
		return CheckerBoard.getPosition(gamePos, name);
	}

	public void placePieces(World world, BlockPos gameBlockPos) {
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "a2");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "b2");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "c2");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "d2");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "e2");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "f2");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "g2");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.WHITE, "h2");

		placeEntity(world, gameBlockPos, new EntityRook(world), Side.WHITE, "a1");
		placeEntity(world, gameBlockPos, new EntityKnight(world), Side.WHITE, "b1");
		placeEntity(world, gameBlockPos, new EntityBishop(world), Side.WHITE, "c1");
		placeEntity(world, gameBlockPos, new EntityKing(world), Side.WHITE, "d1");
		placeEntity(world, gameBlockPos, new EntityQueen(world), Side.WHITE, "e1");
		placeEntity(world, gameBlockPos, new EntityBishop(world), Side.WHITE, "f1");
		placeEntity(world, gameBlockPos, new EntityKnight(world), Side.WHITE, "g1");
		placeEntity(world, gameBlockPos, new EntityRook(world), Side.WHITE, "h1");

		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "a7");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "b7");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "c7");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "d7");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "e7");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "f7");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "g7");
		placeEntity(world, gameBlockPos, new EntityPawn(world), Side.BLACK, "h7");

		placeEntity(world, gameBlockPos, new EntityRook(world), Side.BLACK, "a8");
		placeEntity(world, gameBlockPos, new EntityKnight(world), Side.BLACK, "b8");
		placeEntity(world, gameBlockPos, new EntityBishop(world), Side.BLACK, "c8");
		placeEntity(world, gameBlockPos, new EntityKing(world), Side.BLACK, "d8");
		placeEntity(world, gameBlockPos, new EntityQueen(world), Side.BLACK, "e8");
		placeEntity(world, gameBlockPos, new EntityBishop(world), Side.BLACK, "f8");
		placeEntity(world, gameBlockPos, new EntityKnight(world), Side.BLACK, "g8");
		placeEntity(world, gameBlockPos, new EntityRook(world), Side.BLACK, "h8");
	}

	private void addWand(World world, BlockPos gameBlockPos, int index) {
		ItemChessControlWand wand = (ItemChessControlWand) GameRegistry.findItem(ToroGamesMod.MODID,
				ItemChessControlWand.NAME);
		
		
		
		ItemStack stack = new ItemStack(wand, 1);
		
		//stack.setStackDisplayName("Sweet Wand");
		
		
		
		
		System.out.println("addWand() setting wand game: " + gameBlockPos);
		
		wand.setGame(gameBlockPos, stack);

		IInventory chest = CheckerBoard.getWhiteChest(world, gameBlockPos);
		CheckerBoard.getWhiteChest(world, gameBlockPos).setInventorySlotContents(index, stack);
	}

	private void placeEntity(World world, BlockPos gameBlockPos, EntityChessPiece e, Side side, String position) {

		int x = gameBlockPos.getX() + RANDOM.nextInt(8);
		int z = gameBlockPos.getZ() + RANDOM.nextInt(8);

		e.setChessPosition(position);
		e.setGamePosition(gameBlockPos);
		e.setPosition(x, gameBlockPos.getY() + 2, z);
		e.setSide(side);
		world.spawnEntityInWorld(e);
	}

}
