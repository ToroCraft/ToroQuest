package net.torocraft.dailies.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

class Provider implements ICapabilityProvider {

	IDailiesCapability instance = new DailiesCapabilityImpl();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return CapabilityDailiesHandler.DAILIES_CAPABILITY != null && capability == CapabilityDailiesHandler.DAILIES_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		if (CapabilityDailiesHandler.DAILIES_CAPABILITY != null && capability == CapabilityDailiesHandler.DAILIES_CAPABILITY) {
			return CapabilityDailiesHandler.DAILIES_CAPABILITY.cast(instance);
		}

		return null;
	}
}