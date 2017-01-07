package net.torocraft.toroquest.civilization;

import java.util.List;
import java.util.UUID;

public interface CivilizationDataAccessor {
	Province atLocation(int chunkX, int chunkZ);

	Province register(int chunkX, int chunkZ);

	List<Province> getProvinces();

	boolean canGenStructure(String type, int chunkX, int chunkZ);

	void setProvinceHasLord(UUID provinceId, boolean hasLord);

	boolean provinceHasLord(UUID provinceId);

}