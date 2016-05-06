package net.torocraft.dailies.capabilities;

import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.torocraft.dailies.quests.DailyQuest;

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

	void acceptQuest(DailyQuest quest);

	void abandonQuest(DailyQuest quest);

	Set<DailyQuest> getAcceptedQuests();

	Set<DailyQuest> getCompletedQuests();

}