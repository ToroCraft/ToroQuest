package net.torocraft.bouncermod;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class BounceBlockTileEntity extends TileEntity implements ITickable {
	
	@Override
	public void update() {
		if (!isRunTick()) {
			return;
		}
	}

	private boolean isRunTick() {
		return worldObj.getTotalWorldTime() % 80L == 0L;
	}
	

}
