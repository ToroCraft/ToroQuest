package net.torocraft.dailies.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class DailiesStorage implements Capability.IStorage<IDailiesCapability> {

	@Override
	public NBTBase writeNBT(Capability<IDailiesCapability> capability, IDailiesCapability instance, EnumFacing side) {

		NBTTagCompound c = new NBTTagCompound();
		if (instance != null) {
			c.setInteger("gather", instance.getGatherCount());
			c.setInteger("hunt", instance.getHuntCount());
		}
		return c;
	}

	@Override
	public void readNBT(Capability<IDailiesCapability> capability, IDailiesCapability instance, EnumFacing side, NBTBase base) {

		if (base == null || instance == null) {
			return;
		}

		NBTTagCompound c = (NBTTagCompound) base;
		instance.setGatherCount(c.getInteger("gather"));
		instance.setHuntCount(c.getInteger("hunt"));

	}
}