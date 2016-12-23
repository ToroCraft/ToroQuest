package net.torocraft.toroquest.civilization.quests.util;

import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.torocraft.toroquest.civilization.CivilizationType;

public class QuestData {

	public UUID questId;
	public Integer questType;
	public UUID provinceId;
	public CivilizationType civ;
	public EntityPlayer player;
	public Map<String, Object> data;

	public void readNBT(NBTTagCompound c, EntityPlayer player) {
		this.player = player;
		questId = UUID.fromString(c.getString("id"));
		provinceId = UUID.fromString(c.getString("provinceId"));
		civ = e(c.getString("civ"));
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("id", questId.toString());
		c.setString("provinceId", provinceId.toString());
		c.setString("civ", s(civ));
		return c;
	}

	private String s(CivilizationType civ) {
		if (civ == null) {
			return "";
		}
		return civ.toString();
	}

	private CivilizationType e(String s) {
		try {
			return CivilizationType.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((questId == null) ? 0 : questId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestData other = (QuestData) obj;
		if (questId == null) {
			if (other.questId != null)
				return false;
		} else if (!questId.equals(other.questId))
			return false;
		return true;
	}

}
