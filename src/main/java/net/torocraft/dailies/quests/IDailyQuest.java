package net.torocraft.dailies.quests;

import net.minecraft.entity.player.EntityPlayer;
import net.torocraft.dailies.capabilities.IDailiesCapability;

public interface IDailyQuest extends IDailiesCapability {

	boolean isComplete();

	String getStatusMessage();

	String getName();

	String getType();

	long getId();

	void reward(EntityPlayer player);

}
