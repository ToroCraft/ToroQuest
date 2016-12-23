package net.torocraft.toroquest.civilization.quests.util;

public interface Quest {

	void complete(QuestData data);

	void reward(QuestData data);

	String getTitle(QuestData data);

	String getDescription(QuestData data);

}