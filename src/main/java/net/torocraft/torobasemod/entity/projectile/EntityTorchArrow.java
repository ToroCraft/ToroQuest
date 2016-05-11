package net.torocraft.torobasemod.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityTorchArrow extends EntityArrow {

	private enum CollideAction { PLACE, BREAK, DROP };
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public EntityTorchArrow(World worldIn) {
        super(worldIn);
    }

    public EntityTorchArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntityTorchArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	
    	if (worldObj.isRemote || !inGround) {
    		return;
    	}
    	
    	Block torch = Blocks.torch;
		IBlockState state = torch.getDefaultState();
		
		for (IProperty prop: state.getProperties().keySet()) {
			System.out.println(prop);
		}
		
		CollideAction action = CollideAction.PLACE;

		setDead();
		Vec3d vec3d1 = new Vec3d(posX, posY, posZ);
        Vec3d vec3d = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
        RayTraceResult rtr = worldObj.rayTraceBlocks(vec3d1, vec3d, false, true, false);
        
        BlockPos pos = rtr.getBlockPos();
        
        int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
        
        if (rtr.entityHit != null) {
        	action = CollideAction.DROP;
        } else {
        	if (worldObj.getBlockState(pos).getBlock().getMaterial(state).equals(Material.vine)) {
        		action = CollideAction.BREAK;
        	} else {        		
        		switch (rtr.sideHit) {
        			case DOWN:
        				y--;
        				break;
        			case UP:
        				y++;
        				break;
        			case NORTH:
        				state = state.withProperty(FACING, EnumFacing.NORTH);
        				z--;
        				break;
        			case SOUTH:
        				state = state.withProperty(FACING, EnumFacing.SOUTH);
        				z++;
        				break;
        			case WEST:
        				state = state.withProperty(FACING, EnumFacing.WEST);
        				x--;
        				break;
        			case EAST:
        				state = state.withProperty(FACING, EnumFacing.EAST);
        				x++;
        				break;
        		}
        		
        		pos = new BlockPos(x, y, z);
        	}
        }
        
        switch (action) {
        	case PLACE:
        		if (worldObj.isAirBlock(pos)) {
        			worldObj.setBlockState(pos, state);
        		} else {
        			dropTorch(torch, x, y, z);
        		}
        		break;
        	case BREAK:
        		worldObj.destroyBlock(pos, true);
        		worldObj.setBlockState(pos, torch.getDefaultState());
        		break;
        	case DROP:
        		dropTorch(torch, x, y, z);
        		break;
        }
    }

	private boolean dropTorch(Block torch, int x, int y, int z) {
		return worldObj.spawnEntityInWorld(new EntityItem(worldObj, x, y, z, new ItemStack(torch)));
	}
    
	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(Items.spectral_arrow);
	}
	
}
