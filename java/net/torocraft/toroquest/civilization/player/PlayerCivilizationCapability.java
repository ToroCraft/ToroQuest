package net.torocraft.toroquest.civilization.player;

import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.civilization.quests.util.QuestData;

public interface PlayerCivilizationCapability {

	void setPlayerReputation(CivilizationType civ, int amount);

	void adjustPlayerReputation(CivilizationType civ, int amount);

	int getPlayerReputation(CivilizationType civ);

	ReputationLevel getReputationLevel(CivilizationType civ);

	void setPlayerInCivilization(Province civ);

	Province getPlayerInCivilization();

	NBTTagCompound writeNBT();

	void syncClient();

	void readNBT(NBTBase c);

	void updatePlayerLocation(int chunkX, int chunkZ);

	/*
	 * Quests
	 */
	Set<QuestData> getCurrentQuests();

	boolean removeQuest(QuestData quest);

	QuestData getCurrentQuestFor(Province province);

	QuestData getNextQuestFor(Province province);
}
