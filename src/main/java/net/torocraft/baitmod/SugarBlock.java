package net.torocraft.baitmod;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class SugarBlock extends BlockContainer {

	public static final String NAME = "sugarBlock";
	
	private SugarBlockTileEntity titleEntity;

	public SugarBlock() {
		super(Material.ground);
		setUnlocalizedName(NAME);
		setResistance(0.1f);
		setHardness(0.5f);
		setLightLevel(0);
		setCreativeTab(CreativeTabs.tabBlock);
		
		titleEntity = new SugarBlockTileEntity();
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
		titleEntity.cleanUp();
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		titleEntity.findEntities(worldIn);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {
			return false;
		}

		playerIn.addChatMessage(new TextComponentString("You touched my sugar"));
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return titleEntity;
	}
	
	/**
	 * Making Something Happen When Item Goes Into Water
	 * 
	 * I've noticed it is fairly common for people to want to have a mod item
	 * that does something special when tossed into water.
	 * 
	 * As mentioned above, when an Item is dropped it is turned into an
	 * EntityItem. An EntityItem extends the Entity class, so you can @Override
	 * the onUpdate() method for your custom EntityItem to check for whether it
	 * is in contact with water. Since EntityItem extends the Entity class are
	 * two possible methods to check for contact with water. If you want rain to
	 * also cause some effect, then use the isWet() method. If you only want it
	 * to have effect when submerged in water use the isInWater() method.
	 */

}
