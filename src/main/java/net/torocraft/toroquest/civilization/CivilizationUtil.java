package net.torocraft.toroquest.civilization;

import net.minecraft.world.World;

public class CivilizationUtil {

	public static Province getProvinceAt(World world, int chunkX, int chunkZ) {
		return CivilizationsWorldSaveData.get(world).atLocation(chunkX, chunkZ);
	}

	public static Province registerNewCivilization(World world, int chunkX, int chunkZ) {
		return CivilizationsWorldSaveData.get(world).register(chunkX, chunkZ);
	}

}
