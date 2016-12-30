package net.torocraft.toroquest.civilization.quests;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

public class QuestKillMobs extends QuestBase implements Quest {
	
	private static final EntityMob[] MOB_TYPES = {  };
	
	public static QuestKillMobs INSTANCE;
	
	public static int ID;

	public static void init(int id) {
		INSTANCE = new QuestKillMobs();
		Quests.registerQuest(id, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		ID = id;
	}

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reject(QuestData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle(QuestData data) {
		if (data == null) {
			return "";
		}
		DataWrapper q = new DataWrapper().setData(data);
		return "Kill " + q.getTargetAmount() + " " + q.getMobType() + " in " + getProvinceName(data.getPlayer(), data.getProvinceId());
	}

	@Override
	public String getDescription(QuestData data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String getProvinceName(EntityPlayer player, UUID provinceId) {
		Province province = getProvinceById(provinceId);
		if (province == null) {
			return "";
		}
		return province.name;
	}
	
	public static class DataWrapper {
		private QuestData data = new QuestData();
		private Province provinceFarmedIn;
		private Block farmedCrop;
		
		public QuestData getData() {
			return data;
		}

		public DataWrapper setData(QuestData data) {
			this.data = data;
			return this;
		}

		public Province getProvinceFarmedIn() {
			return provinceFarmedIn;
		}

		public void setProvinceFarmedIn(Province provinceFarmedIn) {
			this.provinceFarmedIn = provinceFarmedIn;
		}

		public Block getFarmedCrop() {
			return farmedCrop;
		}

		public void setFarmedCrop(Block farmedCrop) {
			this.farmedCrop = farmedCrop;
		}

		public Integer getMobType() {
			return i(data.getiData().get("type"));
		}

		public void setMobType(Integer mobType) {
			data.getiData().put("type", mobType);
		}

		public Integer getTargetAmount() {
			return i(data.getiData().get("target"));
		}

		public void setTargetAmount(Integer targetAmount) {
			data.getiData().put("target", targetAmount);
		}

		public Integer getCurrentAmount() {
			return i(data.getiData().get("amount"));
		}

		public void setCurrentAmount(Integer currentAmount) {
			data.getiData().put("amount", currentAmount);
		}

		public Integer getRewardRep() {
			return i(data.getiData().get("rep"));
		}

		public void setRewardRep(Integer rewardRep) {
			data.getiData().put("rep", rewardRep);
		}

		private Integer i(Object o) {
			try {
				return (Integer) o;
			} catch (Exception e) {
				return 0;
			}
		}

		private boolean isApplicable() {
			return isKillMobsQuest() && isInCorrectProvince() && isCorrectMob();
		}

		private boolean isKillMobsQuest() {
			return data.getQuestType() == ID;
		}

		private boolean isInCorrectProvince() {
			return data.getProvinceId().equals(getProvinceFarmedIn().id);
		}
		
		private boolean isCorrectMob() {
			return true;
		}

	}
}
