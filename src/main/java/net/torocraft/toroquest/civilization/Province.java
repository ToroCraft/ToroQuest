package net.torocraft.toroquest.civilization;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class Province {
	public int chunkX;
	public int chunkZ;
	public CivilizationType civilization;

	public void readNBT(NBTTagCompound c) {
		chunkX = c.getInteger("chunkX");
		chunkZ = c.getInteger("chunkZ");
		civilization = e(c.getString("civilization"));
	}

	private CivilizationType e(String s) {
		try {
			return CivilizationType.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	public NBTBase writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("civilization", s(civilization));
		c.setInteger("chunkX", chunkX);
		c.setInteger("chunkZ", chunkZ);
		return c;
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