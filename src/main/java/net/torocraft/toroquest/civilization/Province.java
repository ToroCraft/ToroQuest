package net.torocraft.toroquest.civilization;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class Province {
	public int chunkX;
	public int chunkZ;

	public int lowerVillageBoundX;
	public int upperVillageBoundX;
	public int lowerVillageBoundZ;
	public int upperVillageBoundZ;

	public int xLength;
	public int zLength;
	public int area;

	public CivilizationType civilization;

	public void readNBT(NBTTagCompound c) {
		chunkX = c.getInteger("chunkX");
		chunkZ = c.getInteger("chunkZ");
		lowerVillageBoundX = c.getInteger("lX");
		upperVillageBoundX = c.getInteger("uX");
		lowerVillageBoundZ = c.getInteger("lZ");
		upperVillageBoundZ = c.getInteger("uZ");
		civilization = e(c.getString("civilization"));
		computeSize();
	}

	private CivilizationType e(String s) {
		try {
			return CivilizationType.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	public void addToBoundsAndRecenter(int newChunkX, int newChunkZ) {
		lowerVillageBoundX = Math.min(lowerVillageBoundX, newChunkX);
		upperVillageBoundX = Math.max(upperVillageBoundX, newChunkX);
		lowerVillageBoundZ = Math.min(lowerVillageBoundZ, newChunkZ);
		upperVillageBoundZ = Math.max(upperVillageBoundZ, newChunkZ);
		computeSize();
		recenter();
	}

	private void recenter() {
		chunkX = lowerVillageBoundX + xLength / 2;
		chunkZ = lowerVillageBoundZ + zLength / 2;
	}

	public void computeSize() {
		xLength = Math.abs(upperVillageBoundX - lowerVillageBoundX) + 1;
		zLength = Math.abs(upperVillageBoundZ - lowerVillageBoundZ) + 1;
		area = xLength * zLength;
	}

	public NBTBase writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("civilization", s(civilization));
		c.setInteger("chunkX", chunkX);
		c.setInteger("chunkZ", chunkZ);
		c.setInteger("lX", lowerVillageBoundX);
		c.setInteger("uX", upperVillageBoundX);
		c.setInteger("lZ", lowerVillageBoundZ);
		c.setInteger("uZ", upperVillageBoundZ);
		return c;
	}

	@Override
	public String toString() {
		return "Province of " + civilization.getLocalizedName() + " at chunk " + chunkX + "," + chunkZ + " with an area of " + area;
	}

	private String s(CivilizationType civ) {
		if (civ == null) {
			return "";
		}
		return civ.toString();
	}

	public double chunkDistanceSq(int toChunkX, int toChunkZ) {
		double dx = (double) chunkX - toChunkX;
		double dz = (double) chunkZ - toChunkZ;
		return dx * dx + dz * dz;
	}
}