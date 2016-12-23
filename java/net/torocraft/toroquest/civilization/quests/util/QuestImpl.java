package net.torocraft.toroquest.civilization.quests.util;

import net.minecraft.entity.player.EntityPlayer;
import net.torocraft.toroquest.civilization.Province;

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

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {
		throw new UnsupportedOperationException();
	}

}
