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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockChessControl extends BlockContainer {

	public static final String NAME = "chess_control";

	private ChessGame game;
	private TileEntity tileEntity;

	private boolean isOn = false;
	private boolean wasOn = false;

	public BlockChessControl() {
		super(Material.ground);
		setUnlocalizedName(NAME);
		setResistance(0.1f);
		setHardness(0.5f);
		setLightLevel(0);
		setCreativeTab(CreativeTabs.tabBlock);
		tileEntity = new TileEntityChessControl();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		getGame().placePieces();
	}

	public ChessGame getGame() {

		if (game == null) {
			System.out.println("game is null, creating now");
			game = new ChessGame(tileEntity.getWorld(), tileEntity.getPos());
		}

		return game;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {

		game = new ChessGame(worldIn, pos);

		if (!worldIn.isRemote) {
			game.generate();
		}

		if (placer != null) {
			placer.moveEntity(0, 2, 0);
		}

		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return tileEntity;
	}

	public static class TileEntityChessControl extends TileEntity {
		public static final String NAME = "tile-entity-chess-control";
	}

	/**
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (worldIn.isRemote) {
			return;
		}

		updateOnState(worldIn, pos);

		if (isTurningOn()) {
			getGame().placePieces();
		} else {
			worldIn.scheduleUpdate(pos, this, 8);
		}
	}

	private void updateOnState(World worldIn, BlockPos pos) {
		wasOn = isOn;
		isOn = worldIn.isBlockPowered(pos);
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (worldIn.isRemote) {
			return;
		}

		if (!worldIn.isBlockPowered(pos)) {
			wasOn = false;
			isOn = false;
		}
	}

	private boolean isTurningOn() {
		return !wasOn && isOn;
	}

}
