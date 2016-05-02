package net.torocraft.dailies.capabilities;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.torocraft.dailies.DailiesMod;

public class CapabilityDailiesHandler {

	public static final String NAME = DailiesMod.MODNAME + ":CapabilityDailies";

	@CapabilityInject(IDailiesCapability.class)
	public static Capability<IDailiesCapability> DAILIES_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IDailiesCapability.class, new DailiesStorage(), DailiesCapabilityImpl.class);
		MinecraftForge.EVENT_BUS.register(new Events());
	}

}