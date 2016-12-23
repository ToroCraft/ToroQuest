package net.torocraft.toroquest.civilization.quests.util;

public class QuestImpl implements Quest {

	public Quest getTypedQuest(QuestData data) {
		return null;
	};

	@Override
	public void complete(QuestData data) {
		getTypedQuest(data).complete(data);
	}

	@Override
	public void reward(QuestData data) {
		getTypedQuest(data).reward(data);
	}

	@Override
	public String getTitle(QuestData data) {
		return getTypedQuest(data).getTitle(data);
	}

	@Override
	public String getDescription(QuestData data) {
		return getTypedQuest(data).getDescription(data);
	}

}
