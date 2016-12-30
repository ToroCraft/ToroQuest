package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapability;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

public class QuestKillMobs extends QuestBase implements Quest {
	
	private static final String[] MOB_TYPES = { "zombie", "skeleton", "creeper", "spider" };
	
	public static QuestKillMobs INSTANCE;
	
	public static int ID;

	public static void init(int id) {
		INSTANCE = new QuestKillMobs();
		Quests.registerQuest(id, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		ID = id;
	}
	
	@SubscribeEvent
	public void onKill(LivingDeathEvent event) {
		EntityPlayer player = null;
		EntityLivingBase victim = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (source.getEntity() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getEntity();
		}

		if (player == null) {
			return;
		}
		
		Province province = PlayerCivilizationCapabilityImpl.get(player).getInCivilization();

		if (province == null || province.civilization == null) {
			return;
		}
		
		handleKillMobsQuest(player, province, victim);
	}
		
	private void handleKillMobsQuest(EntityPlayer player, Province provinceHuntedIn, EntityLivingBase victim) {
		Set<QuestData> quests = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuests();
				
		DataWrapper quest = new DataWrapper();
		for (QuestData data : quests) {			
			quest.setData(data);
			quest.huntedMob = EntityList.getEntityString(victim);
			quest.provinceHuntedIn = provinceHuntedIn;
			if (perform(quest)) {
				return;
			}
		}
	}

	private boolean perform(DataWrapper quest) {
		if (quest.getData().getPlayer().world.isRemote) {
			return false;
		}
		
		if (!quest.isApplicable()) {
			return false;
		}
		
		quest.setCurrentAmount(quest.getCurrentAmount() + 1);
		
		if (quest.getCurrentAmount() >= quest.getTargetAmount()) {
			quest.data.setCompleted(true);
		}
		
		return false;
	}

	@Override
	public List<ItemStack> complete(QuestData quest, List<ItemStack> items) {
		if (!quest.getCompleted()) {
			return null;
		}

		Province province = loadProvince(quest.getPlayer().world, quest.getPlayer().getPosition());

		if (province == null || province.id == null || !province.id.equals(quest.getProvinceId())) {
			return null;
		}

		PlayerCivilizationCapability playerCiv = PlayerCivilizationCapabilityImpl.get(quest.getPlayer());

		playerCiv.adjustReputation(quest.getCiv(), new DataWrapper().setData(quest).getRewardRep());

		if (playerCiv.getReputation(province.civilization) > 100 && quest.getPlayer().world.rand.nextInt(10) > 8) {
			ItemStack sword = new ItemStack(Items.GOLDEN_SWORD);
			sword.setStackDisplayName("Golden Sword of " + province.name);
			items.add(sword);
		}

		List<ItemStack> rewards = getRewardItems(quest);
		if (rewards != null) {
			items.addAll(rewards);
		}

		return items;
	}

	@Override
	public void reject(QuestData data) {
	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		return in;
	}

	@Override
	public String getTitle(QuestData data) {
		if (data == null) {
			return "";
		}
		DataWrapper q = new DataWrapper().setData(data);
		return "Kill " + q.getTargetAmount() + " " + mobName(q.getMobType(), data.getPlayer()) + " in " + getProvinceName(data.getPlayer(), data.getProvinceId());
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}
		DataWrapper q = new DataWrapper().setData(data);
		StringBuilder s = new StringBuilder();
		s.append("- Kill ").append(q.getTargetAmount()).append(" ").append(mobName(q.getMobType(), data.getPlayer())).append(" in ").append(getProvinceName(data.getPlayer(), data.getProvinceId())).append("\n");
		s.append("- You have killed ").append(q.getCurrentAmount()).append(" currently.\n");
		s.append("- Reward: " + listItems(getRewardItems(q.data)));
		return s.toString();
	}

	private String mobName(Integer mobType, EntityPlayer player) {
		Entity mob = EntityList.createEntityByID(mobType, player.world);
		return mob.getName();
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {
		
		Random rand = player.getEntityWorld().rand;
		
		DataWrapper q = new DataWrapper();
		q.data.setCiv(province.civilization);
		q.data.setPlayer(player);
		q.data.setProvinceId(province.id);
		q.data.setQuestId(UUID.randomUUID());
		q.data.setQuestType(ID);
		q.data.setCompleted(false);
		
		int roll = rand.nextInt(10);
		
		q.setMobType(rand.nextInt(MOB_TYPES.length));
		q.setCurrentAmount(0);
		q.setRewardRep(0);
		q.setTargetAmount(20 + roll);
		
		ItemStack emeralds = new ItemStack(Items.EMERALD, q.getTargetAmount() / 6);
		List<ItemStack> rewardItems = new ArrayList<ItemStack>();
		rewardItems.add(emeralds);
		setRewardItems(q.data, rewardItems);
		
		return null;
	}
	
	public static class DataWrapper {
		private QuestData data = new QuestData();
		private Province provinceHuntedIn;
		private String huntedMob;
		
		public QuestData getData() {
			EntityList.init();
			return data;
		}

		public DataWrapper setData(QuestData data) {
			this.data = data;
			return this;
		}

		public Province getProvinceHuntedIn() {
			return provinceHuntedIn;
		}

		public void setProvinceHuntedIn(Province provinceHuntedIn) {
			this.provinceHuntedIn = provinceHuntedIn;
		}

		public String getHuntedMob() {
			return huntedMob;
		}

		public void setHuntedMob(String huntedMob) {
			this.huntedMob = huntedMob;
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
			return data.getProvinceId().equals(getProvinceHuntedIn().id);
		}
		
		private boolean isCorrectMob() {
			return MOB_TYPES[getMobType()] == getHuntedMob();
		}

	}
}
