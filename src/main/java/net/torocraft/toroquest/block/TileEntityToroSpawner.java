package net.torocraft.toroquest.block;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.torocraft.toroquest.entities.EntityGuard;

public class TileEntityToroSpawner extends TileEntity implements ITickable {

	private int activatingRangeFromPlayer = 40;

	public TileEntityToroSpawner() {

	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		return compound;
	}

	public void update() {
		if (worldObj.isRemote || !isRunTick()) {
			return;
		}

		if (withinRange()) {
			spawnCreature();
			worldObj.setBlockToAir(pos);
		}
	}

	private void spawnCreature() {
		EntityGuard entity = new EntityGuard(worldObj);
		EntityLiving entityliving = (EntityLiving) entity;
		entity.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldObj.rand.nextFloat() * 360.0F), 0.0F);
		entityliving.rotationYawHead = entityliving.rotationYaw;
		entityliving.renderYawOffset = entityliving.rotationYaw;
		entityliving.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData) null);
		worldObj.spawnEntityInWorld(entity);
	}

	private boolean withinRange() {
		return worldObj.isAnyPlayerWithinRangeAt((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, (double) this.activatingRangeFromPlayer);
	}

	private boolean isRunTick() {
		return worldObj.getTotalWorldTime() % 20L == 0L;
	}

	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}

	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
		nbttagcompound.removeTag("SpawnPotentials");
		return nbttagcompound;
	}


	public boolean onlyOpsCanSetNbt() {
		return true;
	}

}
