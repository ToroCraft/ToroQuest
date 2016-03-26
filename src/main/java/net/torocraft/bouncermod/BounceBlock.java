package net.torocraft.bouncermod;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BounceBlock extends BlockContainer {

	public static final String NAME = "bounceBlock";
	
	protected BounceBlockTileEntity titleEntity;
	protected Double Springiness = 1.2;
	
	public BounceBlock() {
		super(Material.ground);
		setUnlocalizedName(NAME);
		setResistance(0.1f);
		setHardness(5f);
		setLightLevel(0);
		this.translucent = true;
		setCreativeTab(CreativeTabs.tabBlock);
		
		titleEntity = new BounceBlockTileEntity();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		cleanUp();
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		cleanUp();
	}

	public void cleanUp() {
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
	}
	
	@Override
	public void onLanded(World worldIn, Entity entityIn) {
		if (!worldIn.isRemote) {
			return;
		}
		
		entityIn.motionY = -(Springiness*entityIn.motionY);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return titleEntity;
	}
}
