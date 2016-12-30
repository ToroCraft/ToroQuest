package net.torocraft.toroquest.civilization.quests.util;

import java.util.List;

import net.minecraft.item.ItemStack;

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

	public List<ItemStack> complete(List<ItemStack> in) {
		return getTypedQuest(data).complete(data, in);
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

	public List<ItemStack> reject(List<ItemStack> in) {
		return getTypedQuest(data).reject(data, in);
	}

	public List<ItemStack> accept(List<ItemStack> in) {
		return getTypedQuest(data).accept(data, in);
	}

}
