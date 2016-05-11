package net.torocraft.dailies.capabilities;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.torocraft.dailies.quests.DailyQuest;

public class DailiesCapabilityImpl implements IDailiesCapability {

	private Set<DailyQuest> quests;
	private Set<DailyQuest> completedQuests;

	@Override
	public boolean gather(EntityPlayer player, EntityItem item) {
		DailyQuest quest = gatherNextQuest(player, item);

		if (quest == null) {
			return false;
		}

		if (quest.isComplete()) {
			quest.reward(player);
			try {
				displayAchievement(quest, player);
			} catch (Exception e) {

			}
			quests.remove(quest);
			if (completedQuests == null) {
				completedQuests = new HashSet<DailyQuest>();
			}
			completedQuests.add(quest);
		}

		return true;
	}

	private DailyQuest gatherNextQuest(EntityPlayer player, EntityItem item) {
		if (quests == null) {
			return null;
		}
		for (DailyQuest q : quests) {
			if (q.gather(player, item)) {
				return q;
			}
		}
		return null;
	}

	@Override
	public boolean hunt(EntityPlayer player, EntityLivingBase mob) {
		DailyQuest quest = huntNextQuest(player, mob);

		if (quest == null) {
			return false;
		}

		if (quest.isComplete()) {
			quest.reward(player);
			try {
				if (player.worldObj.isRemote) {
					displayAchievement(quest, player);
				}
			} catch (Exception e) {

			}
			quests.remove(quest);
			if (completedQuests == null) {
				completedQuests = new HashSet<DailyQuest>();
			}
			completedQuests.add(quest);
		}

		return true;
	}

	private DailyQuest huntNextQuest(EntityPlayer player, EntityLivingBase mob) {
		if (quests == null) {
			return null;
		}
		for (DailyQuest q : quests) {
			if (q.hunt(player, mob)) {
				return q;
			}
		}
		return null;
	}


	private void displayAchievement(DailyQuest quest, EntityPlayer player) {
		// need to figure out how to make this work without crashing the server

		/*
		 * Achievement achievement = new Achievement(quest.getDisplayName(),
		 * "dailyquestcompleted", 0, 0, Item.getItemById(quest.target.type),
		 * (Achievement) null);
		 * Minecraft.getMinecraft().guiAchievement.displayAchievement(
		 * achievement);
		 */
	}

	@Override
	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		writeQuestsList(c, "quests", quests);
		writeQuestsList(c, "completedQuests", completedQuests);
		return c;
	}

	@Override
	public void readNBT(NBTTagCompound b) {
		if (b == null) {
			quests = new HashSet<DailyQuest>();
			return;
		}
		quests = readQuestList(b, "quests");
		completedQuests = readQuestList(b, "completedQuests");
	}

	private void writeQuestsList(NBTTagCompound c, String key, Set<DailyQuest> quests) {
		NBTTagList list = new NBTTagList();
		if (quests != null) {
			for (DailyQuest quest : quests) {
				list.appendTag(quest.writeNBT());
			}
		}
		c.setTag(key, list);
	}

	private Set<DailyQuest> readQuestList(NBTTagCompound b, String key) {
		Set<DailyQuest> quests = new HashSet<DailyQuest>();
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

		DailyQuest playerQuest = (DailyQuest) quest.clone();
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
	 * private void setDefaultQuests() { quests = new ArrayList<DailyQuest>();
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

	@Override
	public Set<DailyQuest> getAcceptedQuests() {
		return quests;
	}

	@Override
	public Set<DailyQuest> getCompletedQuests() {
		return completedQuests;
	}

}