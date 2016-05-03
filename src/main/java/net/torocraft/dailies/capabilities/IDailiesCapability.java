package net.torocraft.dailies.capabilities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IDailiesCapability {

	/**
	 * return true when quest target was hit
	 */
	boolean gather(EntityPlayer player, EntityItem item, int count);

	/**
	 * return true when quest target was hit
	 */
	boolean hunt(EntityPlayer player, EntityLiving mob, int count);
	

	NBTTagCompound writeNBT();

	void readNBT(NBTTagCompound c);

}