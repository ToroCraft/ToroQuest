package net.torocraft.toroquest.civilization.quests;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

public class QuestFarm implements Quest {

	private static final Block[] CROP_TYPES = { Blocks.CARROTS, Blocks.POTATOES, Blocks.WHEAT, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM,
			Blocks.BEETROOTS };

	public static QuestFarm INSTANCE;

	public static int ID;

	public static void init(int id) {
		INSTANCE = new QuestFarm();
		Quests.registerQuest(id, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		ID = id;
	}

	public static class Data extends QuestData {
		private QuestData data = new QuestData();
		private Province provinceFarmedIn;
		private Block farmedCrop;

		public QuestData getData() {
			return data;
		}

		public Data setData(QuestData data) {
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

		public Integer getCropType() {
			return i(data.getiData().get("type"));
		}

		public void setCropType(Integer cropType) {
			data.getiData().put("type", cropType);
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
			return isFarmQuest() && isInCorrectProvince() && isCorrectCrop();
		}

		private boolean isFarmQuest() {
			return getQuestType() == ID;
		}

		private boolean isInCorrectProvince() {
			return getProvinceId().equals(provinceFarmedIn.id);
		}

		private boolean isCorrectCrop() {
			return CROP_TYPES[getCropType()] == getFarmedCrop();
		}

	}

	@SubscribeEvent
	public void onFarm(PlaceEvent event) {
		if (event.getPlayer() == null) {
			return;
		}

		Province provinceFarmedIn = loadProvice(event.getPlayer().worldObj, event.getBlockSnapshot().getPos());

		if (provinceFarmedIn == null || provinceFarmedIn.civilization == null) {
			return;
		}

		handleFarmQuest(event.getPlayer(), provinceFarmedIn, event.getPlacedBlock().getBlock());
	}

	protected Province loadProvice(World world, BlockPos pos) {
		return CivilizationUtil.getProvinceAt(world, pos.getX() / 16, pos.getZ() / 16);
	}

	private void handleFarmQuest(EntityPlayer player, Province provinceFarmedIn, Block crop) {
		Set<QuestData> quests = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuests();
		Data quest = new Data();
		for (QuestData data : quests) {
			quest.setData(data);
			quest.farmedCrop = crop;
			quest.provinceFarmedIn = provinceFarmedIn;
			if (perform(quest, crop)) {
				return;
			}
		}
	}

	public boolean perform(Data quest, Block farmedCrop) {
		if (!quest.isApplicable()) {
			return false;
		}
		quest.setCurrentAmount(quest.getCurrentAmount() + 1);
		if (quest.getCurrentAmount() >= quest.getTargetAmount()) {
			quest.setCompleted(true);
			complete(quest);
		}
		return true;
	}

	@Override
	public void reward(QuestData data) {
		PlayerCivilizationCapabilityImpl.get(data.getPlayer()).adjustPlayerReputation(data.getCiv(), new Data().setData(data).getRewardRep());
	}

	@Override
	public void complete(QuestData quest) {
		if (PlayerCivilizationCapabilityImpl.get(quest.getPlayer()).removeQuest(quest)) {
			reward(quest);
		}
	}

	@Override
	public String getTitle(QuestData data) {
		return "Farming";
	}

	@Override
	public String getDescription(QuestData data) {
		return "";
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {

		Random rand = player.getEntityWorld().rand;

		Data q = new Data();
		q.setCiv(province.civilization);
		q.setPlayer(player);
		q.setProvinceId(province.id);
		q.setQuestId(UUID.randomUUID());
		q.setQuestType(ID);
		q.setCompleted(false);
		// TODO factor in current rep for amount and reward, maybe also the
		// amount of crops in the province

		int roll = rand.nextInt(100);

		q.setCropType(rand.nextInt(CROP_TYPES.length));
		q.setCurrentAmount(0);
		q.setRewardRep(roll / 20);
		q.setTargetAmount(roll);

		return q;
	}

}
