package net.torocraft.toroquest.civilization.quests.util;

import java.util.HashMap;
import java.util.Map;

import net.torocraft.toroquest.civilization.quests.QuestFarm;
import net.torocraft.toroquest.civilization.quests.QuestKillBoss;
import net.torocraft.toroquest.civilization.quests.QuestKillEnemy;
import net.torocraft.toroquest.civilization.quests.QuestKillMobs;
import net.torocraft.toroquest.civilization.quests.QuestMine;

public class Quests {

	private static final Map<Integer, Quest> REGISTRY = new HashMap<Integer, Quest>();

	public static void registerQuest(int id, Quest instance) {
		REGISTRY.put(id, instance);
	}

	public static Quest getQuestForId(Integer id) {
		if (id == null) {
			throw new NullPointerException("quest ID is null");
		}
		return REGISTRY.get(id);
	}

	public static void init() {
		QuestMine.init(1);
		QuestKillMobs.init(2);
		QuestKillEnemy.init(3);
		QuestKillBoss.init(4);
		QuestFarm.init(5);
	}

}
