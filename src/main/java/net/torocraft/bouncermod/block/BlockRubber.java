package net.torocraft.bouncermod.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRubber extends Block {

	public static final String NAME = "bounceBlock";
		
	protected Double springiness = 1.1;
	protected Integer bounceDirection;
	protected Double minBounceSpeed;
	protected Double maxBounceSpeed;
	
	private Double entityBounceSpeed;
	
	public BlockRubber() {
		super(Material.ground);
		setUnlocalizedName(NAME);
		setResistance(0.1f);
		setHardness(5f);
		setLightLevel(0);
		setCreativeTab(CreativeTabs.tabBlock);
		setStepSound(SoundType.SLIME);
	}

	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return BounceModBlocks.rubberBlockItem;
    }
	
	@Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		if(entityIn.isSneaking()) {
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		} else {
			entityIn.fall(fallDistance, 0F);
		}
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
	}
	
	@Override
	public void onLanded(World worldIn, Entity entityIn) {
		if (!worldIn.isRemote) {
			return; 
		}
		if(entityIn.isSneaking()) {
			super.onLanded(worldIn, entityIn);
		} else {
			bounce(entityIn);
		}
	}
	
	@Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
    {
		if(entityIn.isSneaking()) {
			super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
		} else {
			bounce(entityIn);
			super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
		}
    }
    
	private void bounce(Entity entityIn) {
		setIncomingSpeed(entityIn);
		modifyBounceSpeedBySpringiness();
		setToMinimumSpeedIfGoingTooSlow();
		setToMaximumSpeedIfGoingTooFast();
		setEntitySpeed(entityIn);
	}
	
	private void setIncomingSpeed(Entity entityIn) {
		entityBounceSpeed = Math.abs(entityIn.motionY);
	}
	
	private void modifyBounceSpeedBySpringiness() {
		if(springiness == null) {
			return;
		}
		entityBounceSpeed = springiness * entityBounceSpeed;
	}

	private void setToMinimumSpeedIfGoingTooSlow() {
		if(minBounceSpeed == null) {
			return;
		}
		if(minBounceSpeed < entityBounceSpeed) {
			entityBounceSpeed = minBounceSpeed;
		}
	}

	private void setToMaximumSpeedIfGoingTooFast() {
		if(maxBounceSpeed == null) {
			return;
		}
		if(maxBounceSpeed > entityBounceSpeed) {
			entityBounceSpeed = maxBounceSpeed;
		}
	}
	
	private void setEntitySpeed(Entity entityIn) {
		entityIn.motionY = entityBounceSpeed;
	}
}
