package net.torocraft.toroquest.block;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityToroSpawner extends TileEntity implements ITickable {

	private int activatingRangeFromPlayer = 16;

	private boolean isActivated() {
		return worldObj.isAnyPlayerWithinRangeAt((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, (double) this.activatingRangeFromPlayer);
	}



	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		// this.spawnerLogic.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		// this.spawnerLogic.writeToNBT(compound);
		return compound;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		// this.spawnerLogic.updateSpawner();
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

	/*
	 * public boolean receiveClientEvent(int id, int type) { return
	 * this.spawnerLogic.setDelayToMin(id) ? true : super.receiveClientEvent(id,
	 * type); }
	 */

	public boolean onlyOpsCanSetNbt() {
		return true;
	}

	/*
	 * public MobSpawnerBaseLogic getSpawnerBaseLogic() { return
	 * this.spawnerLogic; }
	 */
}
