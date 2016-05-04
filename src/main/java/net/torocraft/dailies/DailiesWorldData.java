package net.torocraft.dailies;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.torocraft.dailies.quests.DailyQuest;

public class DailiesWorldData extends WorldSavedData {

	public static final String MODNAME = "DailiesMod";
	
	private List<DailyQuest> dailies;
	
	public DailiesWorldData() {
		super(MODNAME);
	}
	
	public DailiesWorldData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		dailies = new ArrayList<DailyQuest>();
		NBTTagList list = (NBTTagList)nbt.getTag("dailies");
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			DailyQuest daily = new DailyQuest();
			daily.readNBT(tag);
			dailies.add(daily);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < dailies.size(); i++) {
			NBTTagCompound tag = dailies.get(i).writeNBT();
			nbt.setTag(dailies.get(i).getName(), tag);
			list.appendTag(tag);
		}
		nbt.setTag("dailies", list);
	}
	
	public List<DailyQuest> getDailies() {
		return dailies;
	}
	
	public void setDailies(List<DailyQuest> dailies) {
		this.dailies = dailies;
		markDirty();
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
