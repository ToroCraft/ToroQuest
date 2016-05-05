package net.torocraft.dailies.capabilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.dailies.quests.DailyQuest;
import net.torocraft.dailies.quests.IDailyQuest;

public class DailiesCapabilityImpl implements IDailiesCapability {

	private Set<IDailyQuest> quests;
	private Set<IDailyQuest> completedQuests;

	@Override
	public boolean gather(EntityPlayer player, EntityItem item) {

		List<IDailyQuest> completedQuests = new ArrayList<IDailyQuest>();

		boolean hit = false;

		for (IDailyQuest quest : quests) {
			if (quest.gather(player, item)) {

				if (quest.isComplete()) {
					quest.reward(player);
					displayAchievement((DailyQuest)quest, player);
					completedQuests.add(quest);
				}

				hit = true;

				break;
			}
		}

		for (IDailyQuest quest : completedQuests) {
			quests.remove(quest);
		}

		return hit;
	}

	@Override
	public boolean hunt(EntityPlayer player, EntityLivingBase mob) {
		List<IDailyQuest> completedQuests = new ArrayList<IDailyQuest>();
		boolean hit = false;

		for (IDailyQuest quest : quests) {
			if (quest.hunt(player, mob)) {
				if (quest.isComplete()) {
					quest.reward(player);
					displayAchievement((DailyQuest)quest, player);
					completedQuests.add(quest);
				}
				hit = true;
				break;
			}
		}

		for (IDailyQuest quest : completedQuests) {
			quests.remove(quest);
		}
		return hit;
	}

	@SideOnly(Side.CLIENT)
	private void displayAchievement(DailyQuest quest, EntityPlayer player) {
		Achievement achievement = new Achievement(quest.getDisplayName(), "dailyquestcompleted", 0, 0, Item.getItemById(quest.target.type), (Achievement) null);
		Minecraft.getMinecraft().guiAchievement.displayAchievement(achievement);
	}


	@Override
	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		writeQuestsList(c, "quests", quests);
		writeQuestsList(c, "completedQuests", completedQuests);
		return c;
	}

	private void writeQuestsList(NBTTagCompound c, String key, Set<IDailyQuest> quests) {
		NBTTagList list = new NBTTagList();
		for (IDailyQuest quest : quests) {
			list.appendTag(quest.writeNBT());
		}
		c.setTag(key, list);
	}

	@Override
	public void readNBT(NBTTagCompound b) {

		/*
		 * 
		 * if (true) { setDefaultQuests(); return; }
		 */
		if (b == null || !(b.getTag("quests") instanceof NBTTagList)) {
			// setDefaultQuests();
			return;
		}

		quests = readQuestList(b, "quests");
		completedQuests = readQuestList(b, "completedQuests");

		if (quests.size() < 1) {
			// setDefaultQuests();
		}
	}

	private Set<IDailyQuest> readQuestList(NBTTagCompound b, String key) {
		Set<IDailyQuest> quests = new HashSet<IDailyQuest>();
		NBTTagList list = (NBTTagList) b.getTag(key);

		if (list == null) {
			return quests;
		}

		for (int i = 0; i < list.tagCount(); i++) {
			DailyQuest quest = new DailyQuest();
			quest.readNBT(list.getCompoundTagAt(i));
			quests.add(quest);
		}

		return quests;
	}
	
	@Override
	public void acceptQuest(DailyQuest quest) {
		if (quests == null) {
			return;
		}

		DailyQuest playerQuest = quest.clone();
		playerQuest.date = System.currentTimeMillis();
		quests.add(playerQuest);

	}
	
	@Override
	public void abandonQuest(DailyQuest quest) {
		if (quests == null) {
			return;
		}
		if (quests.remove(quest)) {
			completedQuests.add(quest);
		}

	}
	/*
	 * private void setDefaultQuests() { quests = new ArrayList<IDailyQuest>();
	 * DailyQuest quest = new DailyQuest(); Reward reward = new Reward();
	 * reward.quantity = 20; reward.type = 384; TypedInteger target = new
	 * TypedInteger(); target.type = 12; target.quantity = 2; quest.type =
	 * "gather"; quest.reward = reward; quest.target = target;
	 * 
	 * quests.add(quest);
	 * 
	 * quest = new DailyQuest(); reward = new Reward(); reward.quantity = 50;
	 * reward.type = 264; target = new TypedInteger(); target.type = 3;
	 * target.quantity = 2; quest.type = "gather"; quest.reward = reward;
	 * quest.target = target;
	 * 
	 * quests.add(quest);
	 * 
	 * quest = new DailyQuest(); reward = new Reward(); reward.quantity = 30;
	 * reward.type = 384; target = new TypedInteger(); target.type = 101;
	 * target.quantity = 2; quest.type = "hunt"; quest.reward = reward;
	 * quest.target = target;
	 * 
	 * quests.add(quest); }
	 */

}