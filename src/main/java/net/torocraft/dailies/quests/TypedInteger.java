package net.torocraft.dailies.quests;

import net.minecraft.nbt.NBTTagCompound;

public class TypedInteger {
	public int type;
	public int quantity;

	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setInteger("type", type);
		c.setInteger("quantity", quantity);
		return c;
	}

	public void readNBT(NBTTagCompound c) {
		if (c == null) {
			return;
		}
		type = c.getInteger("type");
		quantity = c.getInteger("quantity");
	}
}