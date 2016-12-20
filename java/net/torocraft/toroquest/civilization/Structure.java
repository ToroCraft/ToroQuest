package net.torocraft.toroquest.civilization;

import net.minecraft.nbt.NBTTagCompound;

public class Structure {
	public String type;
	public int chunkX;
	public int chunkZ;

	public double distanceSqFrom(int chunkX, int chunkZ) {
		double d0 = this.chunkX - chunkX;
		double d1 = this.chunkZ - chunkZ;
		return d0 * d0 + d1 * d1;
	}

	public void readNBT(NBTTagCompound c) {
		chunkX = c.getInteger("chunkX");
		chunkZ = c.getInteger("chunkZ");
		type = c.getString("type");
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("type", type);
		c.setInteger("chunkX", chunkX);
		c.setInteger("chunkZ", chunkZ);
		return c;
	}
}