package net.torocraft.toroquest.civilization;

import java.util.List;

public interface CivilizationDataAccessor {
	Province atLocation(int chunkX, int chunkZ);

	Province register(int chunkX, int chunkZ);

	List<Province> getProvinces();

	boolean canGenStructure(String type, int chunkX, int chunkZ);

}