package net.torocraft.dailies.capabilities;

public interface IDailiesCapability {

	void gather(int count);

	void hunt(int count);

	String statusMessage();

	int getGatherCount();

	int getHuntCount();

	void setGatherCount(int gatherCount);

	void setHuntCount(int huntCount);

}