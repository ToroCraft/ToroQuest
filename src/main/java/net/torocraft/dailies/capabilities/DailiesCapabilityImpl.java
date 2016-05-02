package net.torocraft.dailies.capabilities;

public class DailiesCapabilityImpl implements IDailiesCapability {

	private int gatherCount = 0;
	private int huntCount = 0;

	@Override
	public String statusMessage() {
		return "Hunt[" + huntCount + "] Gather[" + gatherCount + "]";
	}

	@Override
	public void gather(int count) {
		gatherCount += count;
	}

	@Override
	public void hunt(int count) {
		huntCount += count;
	}

	@Override
	public int getGatherCount() {
		return gatherCount;
	}

	@Override
	public void setGatherCount(int gatherCount) {
		this.gatherCount = gatherCount;
	}

	@Override
	public int getHuntCount() {
		return huntCount;
	}

	@Override
	public void setHuntCount(int huntCount) {
		this.huntCount = huntCount;
	}


}