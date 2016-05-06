package net.torocraft.dailies;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.torocraft.dailies.quests.DailyQuest;
import net.torocraft.dailies.quests.IDailyQuest;

public class DailiesWorldData extends WorldSavedData {

	public static final String MODNAME = "DailiesMod";
	
	private Set<IDailyQuest> dailyQuests;
	
	public DailiesWorldData() {
		super(MODNAME);
	}
	
	public DailiesWorldData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		dailyQuests = readQuestList(nbt, "dailies");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		writeQuestsList(nbt, "dailes", dailyQuests);
	}
	
	public Set<IDailyQuest> getDailyQuests() {
		return dailyQuests;
	}
	
	public void setDailyQuests(Set<IDailyQuest> dailies) {
		this.dailyQuests = dailies;
		markDirty();
	}
	
	private void writeQuestsList(NBTTagCompound c, String key, Set<IDailyQuest> quests) {
		NBTTagList list = new NBTTagList();
		for (IDailyQuest quest : quests) {
			list.appendTag(quest.writeNBT());
		}
		c.setTag(key, list);
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

	public static DailiesWorldData get(World world) {
		DailiesWorldData data = (DailiesWorldData)world.loadItemData(DailiesWorldData.class, MODNAME);
		if(data == null) {
			data = new DailiesWorldData();
			world.setItemData(MODNAME, data);
		}
		return data;
	}

}
