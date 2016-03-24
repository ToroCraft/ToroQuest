package com.frodare.baitmod;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SugarBlockTileEntity extends TileEntity implements ITickable {
	
	@Override
	public void update() {

		if (this.worldObj.getTotalWorldTime() % 80L == 0L) {
			if (!this.worldObj.isRemote) {
				System.out.println("Game Tick from Sugar");
			}
		}

	}
	
	

	public void findEntities(World worldIn, BlockPos pos, IBlockState state, Random rand) {

		int radius = 50;

		AxisAlignedBB attractorBounds = new AxisAlignedBB(pos.getX() - 50d, pos.getY() - 50d, pos.getZ() - 50d,
				pos.getX() + 50d, pos.getY() + 50d, pos.getZ() + 50d);

		List<EntityLiving> entsInBounds = worldIn.getEntitiesWithinAABB(EntityLiving.class, attractorBounds);

		for (EntityLiving e : entsInBounds) {

		}

	}

}
