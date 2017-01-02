package net.torocraft.toroquest.civilization.player;

import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.civilization.quests.util.QuestData;

public interface PlayerCivilizationCapability {

	void setReputation(CivilizationType civ, int amount);

	void adjustReputation(CivilizationType civ, int amount);

	int getReputation(CivilizationType civ);

	ReputationLevel getReputationLevel(CivilizationType civ);

	void setInCivilization(Province civ);

	Province getInCivilization();

	NBTTagCompound writeNBT();

	void syncClient();

	void readNBT(NBTBase c);

	void updatePlayerLocation(int chunkX, int chunkZ);

	/**
	 * get a Set of all quests currently accepted by the player
	 */
	Set<QuestData> getCurrentQuests();

	/**
	 * @return quest data or NULL if wasn't able to accept
	 */
	List<ItemStack> acceptQuest(List<ItemStack> in);

	/**
	 * 
	 * @param in
	 *            items to turn in
	 * @return NULL if quest was not completed or a List of gifts / return items
	 *         if the quest completed
	 */
	List<ItemStack> completeQuest(List<ItemStack> in);

	/**
	 * rejects the current quest and returns the next quest data or NULL on
	 * errors
	 */
	List<ItemStack> rejectQuest(List<ItemStack> in);

	/**
	 * get the current quest accepted for the given province or NULL is there is
	 * no accepted quest for the province
	 */
	QuestData getCurrentQuestFor(Province province);

	/**
	 * return the next quest for the given province, should never return NULL
	 */
	QuestData getNextQuestFor(Province province);

}
