package net.torocraft.toroquest.civilization.quests.util;

import net.minecraft.entity.player.EntityPlayer;
import net.torocraft.toroquest.civilization.Province;

public interface Quest {

	void complete(QuestData data);

	void reward(QuestData data);

	String getTitle(QuestData data);

	String getDescription(QuestData data);

	QuestData generateQuestFor(EntityPlayer player, Province province);

}