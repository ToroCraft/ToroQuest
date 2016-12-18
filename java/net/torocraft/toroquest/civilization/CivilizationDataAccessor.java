package net.torocraft.toroquest.civilization;

public interface CivilizationDataAccessor {
	Province atLocation(int chunkX, int chunkZ);

	Province register(int chunkX, int chunkZ);

	boolean canGenStructure(String type, int chunkX, int chunkZ);

}