package net.torocraft.toroquest.civilization.quests.util;

public class QuestDelegator {

	private QuestData data;

	public QuestDelegator(QuestData data) {
		setData(data);
	}

	public Quest getTypedQuest(QuestData data) {
		if (data == null) {
			throw new NullPointerException("quest data is null");
		}
		return Quests.getQuestForId(data.getQuestType());
	};

	public void complete() {
		getTypedQuest(data).complete(data);
	}

	public void reward() {
		getTypedQuest(data).reward(data);
	}

	public String getTitle() {
		return getTypedQuest(data).getTitle(data);
	}

	public String getDescription() {
		return getTypedQuest(data).getDescription(data);
	}

	public QuestData getData() {
		return data;
	}

	public void setData(QuestData data) {
		if (data == null) {
			throw new NullPointerException("quest delegator was set with a null");
		}
		this.data = data;
	}

}
