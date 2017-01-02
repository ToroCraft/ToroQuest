package net.torocraft.toroquest.civilization.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.QuestData;

public abstract class PlayerCivilization {

	protected Map<CivilizationType, Integer> reputations = new HashMap<CivilizationType, Integer>();
	protected Set<QuestData> quests = new HashSet<QuestData>();
	protected Set<QuestData> nextQuests = new HashSet<QuestData>();
	protected Map<UUID, Integer> completedQuestsByProvince = new HashMap<UUID, Integer>();
	protected Integer completedQuests = 0;
	protected Province inCiv;

	public abstract EntityPlayer getPlayer();

	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setTag("reputations", buildNBTReputationList());
		c.setTag("quests", buildQuestCompound(quests));
		c.setTag("nextQuests", buildQuestCompound(nextQuests));
		c.setInteger("completedQuests", completedQuests);
		c.setTag("completedQuestsByProvince", buildCompletedQuestsByProvince());

		if (inCiv != null) {
			c.setTag("inCiv", inCiv.writeNBT());
		} else {
			c.removeTag("inCiv");
		}
		return c;
	}

	protected NBTTagList buildNBTReputationList() {
		NBTTagList repList = new NBTTagList();
		for (Entry<CivilizationType, Integer> rep : reputations.entrySet()) {
			if (rep.getValue() == null || rep.getKey() == null) {
				continue;
			}
			repList.appendTag(buildNBTReputationListItem(rep.getKey(), rep.getValue()));
		}
		return repList;
	}

	private NBTTagList buildQuestCompound(Set<QuestData> quests) {
		NBTTagList repList = new NBTTagList();
		for (QuestData data : quests) {
			if (data == null) {
				continue;
			}
			if (!data.isValid()) {
				continue;
			}
			repList.appendTag(data.writeNBT());
		}
		return repList;
	}

	private NBTTagCompound buildCompletedQuestsByProvince() {
		NBTTagCompound c = new NBTTagCompound();
		for (Entry<UUID, Integer> e : completedQuestsByProvince.entrySet()) {
			if (e.getKey() != null && e.getValue() != null) {
				c.setInteger(e.getKey().toString(), e.getValue());
			}
		}
		return c;
	}

	private static String s(CivilizationType civ) {
		try {
			return civ.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static NBTTagCompound buildNBTReputationListItem(CivilizationType civ, int rep) {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("civ", s(civ));
		c.setInteger("amount", rep);
		return c;
	}

	public void readNBT(NBTBase nbt) {
		if (nbt == null || !(nbt instanceof NBTTagCompound)) {
			reputations = new HashMap<CivilizationType, Integer>();
			inCiv = null;
			return;
		}

		NBTTagCompound b = (NBTTagCompound) nbt;
		reputations = readNBTReputationList(b.getTag("reputations"));
		quests = readQuests(b.getTag("quests"));
		nextQuests = readQuests(b.getTag("nextQuests"));
		completedQuests = b.getInteger("completedQuests");
		completedQuestsByProvince = readCompletedQuestsByProvince(b.getCompoundTag("completedQuestsByProvince"));

		NBTBase civTag = b.getTag("inCiv");
		if (civTag != null && civTag instanceof NBTTagCompound) {
			inCiv = new Province();
			inCiv.readNBT((NBTTagCompound) civTag);
		} else {
			inCiv = null;
		}
	}

	private Set<QuestData> readQuests(NBTBase tag) {
		Set<QuestData> quests = new HashSet<QuestData>();
		if (tag == null || !(tag instanceof NBTTagList)) {
			return quests;
		}
		NBTTagList list = (NBTTagList) tag;
		for (int i = 0; i < list.tagCount(); i++) {
			QuestData d = new QuestData();
			d.readNBT(list.getCompoundTagAt(i), getPlayer());
			if (!d.isValid()) {
				continue;
			}
			quests.add(d);
		}
		return quests;
	}

	private Map<CivilizationType, Integer> readNBTReputationList(NBTBase tag) {
		Map<CivilizationType, Integer> reputations = new HashMap<CivilizationType, Integer>();
		if (tag == null || !(tag instanceof NBTTagList)) {
			return reputations;
		}
		NBTTagList list = (NBTTagList) tag;
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound c = list.getCompoundTagAt(i);
			reputations.put(e(c.getString("civ")), c.getInteger("amount"));
		}
		return reputations;
	}

	private Map<UUID, Integer> readCompletedQuestsByProvince(NBTTagCompound tag) {
		Map<UUID, Integer> m = new HashMap<UUID, Integer>();
		for (String provinceId : tag.getKeySet()) {
			try {
				m.put(UUID.fromString(provinceId), tag.getInteger(provinceId));
			} catch (Exception e) {
			}
		}
		return m;
	}

	private CivilizationType e(String s) {
		try {
			return CivilizationType.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	public static class PlayerCivilizationStorage implements IStorage<PlayerCivilizationCapability> {

		@Override
		public NBTBase writeNBT(Capability<PlayerCivilizationCapability> capability, PlayerCivilizationCapability instance, EnumFacing side) {
			return instance.writeNBT();
		}

		@Override
		public void readNBT(Capability<PlayerCivilizationCapability> capability, PlayerCivilizationCapability instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT(nbt);
		}

	}

}
