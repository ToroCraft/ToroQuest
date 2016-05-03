package net.torocraft.dailies.quests;

import net.minecraft.entity.player.EntityPlayer;

public class Reward extends TypedInteger {

	public static final int XP = 1;

	public void reward(EntityPlayer player) {
		if (type == XP) {
			xpReward(player);
		}
	}

	private void xpReward(EntityPlayer player) {
		player.addExperience(quantity);
	}
}