package net.torocraft.dailies.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class DailiesStorage implements Capability.IStorage<IDailiesCapability> {

	@Override
	public NBTBase writeNBT(Capability<IDailiesCapability> capability, IDailiesCapability instance, EnumFacing side) {
		return instance.writeNBT();
	}

	@Override
	public void readNBT(Capability<IDailiesCapability> capability, IDailiesCapability instance, EnumFacing side, NBTBase base) {
		if (instance == null) {
			return;
		}

		NBTTagCompound c = null;

		if (base != null && base instanceof NBTTagCompound) {
			c = (NBTTagCompound) base;
		}

		instance.readNBT(c);
	}
}