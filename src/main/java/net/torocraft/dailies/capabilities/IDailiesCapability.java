package net.torocraft.dailies.capabilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IDailiesCapability {

	/**
	 * return true when quest target was hit
	 */
	boolean gather(EntityPlayer player, EntityItem item);

	/**
	 * return true when quest target was hit
	 */
	boolean hunt(EntityPlayer player, EntityLivingBase mob);
	

	NBTTagCompound writeNBT();

	void readNBT(NBTTagCompound c);

}