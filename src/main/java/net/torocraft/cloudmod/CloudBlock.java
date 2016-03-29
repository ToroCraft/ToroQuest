package net.torocraft.cloudmod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CloudBlock extends Block {

	public static final String NAME = "cloudBlock";
		
	public final Double viscosity = .2D;
		
	public CloudBlock() {
		super(Material.ground);
		setUnlocalizedName(NAME);
		setResistance(0.1f);
		setHardness(5f);
		setLightLevel(0);
		setCreativeTab(CreativeTabs.tabBlock);
		this.translucent = true;
		this.isOpaqueCube(getDefaultState());
	}
	
	@Override
    public boolean isCollidable()
    {
        return false;
    }
	
	@Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return null;
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
    {
		entityIn.motionX = entityIn.motionX * viscosity;
		entityIn.motionY = entityIn.motionY * viscosity;
		entityIn.motionZ = entityIn.motionZ * viscosity;
    }
	
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
