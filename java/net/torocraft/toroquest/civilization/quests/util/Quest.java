package net.torocraft.toroquest.civilization.quests.util;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.torocraft.toroquest.civilization.Province;

public interface Quest {

	/**
	 * Attempt to complete the request
	 * 
	 * @param data
	 *            quest data
	 * @param in
	 *            items to turn in
	 * @return gift items or NULL if failed to complete
	 */
	List<ItemStack> complete(QuestData data, List<ItemStack> in);

	/**
	 * Gives the quest a chance to handle a rejected quest
	 * 
	 * @param data
	 *            the current quest data
	 */
	void reject(QuestData data);

	/**
	 * 
	 * @param data
	 *            quest to accept
	 * @param in
	 *            items to turn in to accept the quest
	 * @return null if not accepted (maybe a missing turn in item) or a list of
	 *         quest or return items
	 */
	List<ItemStack> accept(QuestData data, List<ItemStack> in);

	String getTitle(QuestData data);

	String getDescription(QuestData data);

	/**
	 * 
	 * @param player
	 * @param province
	 * @return NULL if quest would not be completed
	 */
	QuestData generateQuestFor(EntityPlayer player, Province province);

}