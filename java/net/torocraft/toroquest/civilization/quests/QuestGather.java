package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

public class QuestGather implements Quest {

	public static QuestGather INSTANCE;

	public static int ID;

	public static void init(int id) {
		INSTANCE = new QuestGather();
		Quests.registerQuest(id, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		ID = id;
	}

	protected Province loadProvice(World world, BlockPos pos) {
		return CivilizationUtil.getProvinceAt(world, pos.getX() / 16, pos.getZ() / 16);
	}

	@Override
	public String getTitle(QuestData data) {
		if (data == null) {
			return "";
		}

		return "Gather " + getTargetAmount(data) + "  Items";
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}
		return "Gahter Quest:  On completion you will be reward with " + getRewardRep(data) + " reputation points";
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {

		Random rand = player.getEntityWorld().rand;

		QuestData data = new QuestData();

		data.setCiv(province.civilization);
		data.setPlayer(player);
		data.setProvinceId(province.id);
		data.setQuestId(UUID.randomUUID());
		data.setQuestType(ID);
		data.setCompleted(false);
		// TODO factor in current rep for amount and reward, maybe also the
		// amount of crops in the province

		int roll = rand.nextInt(30) + 10;

		Integer[] a = { Item.getIdFromItem(Items.DIAMOND) };

		// q.setItemIds(a);
		// q.setRewardRep(roll / 20 + 10);
		// q.setTargetAmount(roll);

		return data;
	}

	@Override
	public void reject(QuestData data) {

	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		ItemStack emeralds = new ItemStack(Items.EMERALD, 10);
		emeralds.setStackDisplayName("Starter Funds");
		in.add(emeralds);
		return in;
	}

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> items) {
		if (!removeQuestFromPlayer(data)) {
			return null;
		}

		Province province = loadProvice(data.getPlayer().worldObj, data.getPlayer().getPosition());

		if (province == null || !province.id.equals(data.getProvinceId())) {
			return null;
		}

		if (!hasCorrectItems(data, items)) {
			return null;
		}

		items = removeQuestItems(data, items);

		PlayerCivilizationCapabilityImpl.get(data.getPlayer()).adjustPlayerReputation(data.getCiv(), getRewardRep(data));

		ItemStack emeralds = new ItemStack(Items.EMERALD, 20);
		items.add(emeralds);

		return items;
	}

	private List<ItemStack> removeQuestItems(QuestData data, List<ItemStack> items) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean hasCorrectItems(QuestData data, List<ItemStack> items) {

		for (ItemStack requiredStack : getRequiredItems(data)) {
			for (ItemStack turnInStack : items) {
				continue;
			}
		}

		// TODO Auto-generated method stub
		return false;
	}

	public static class ItemCount {
		public int target = 0;
		public int count = 0;
	}

	public static class ItemCounter {


		public Map<Item, ItemCount> counts;

		public boolean hasEnough(List<ItemStack> required, List<ItemStack> items) {
			resetCounts(required);

			for (ItemStack stack : items) {
				if (counts.get(stack.getItem()) != null) {
					ItemCount count = counts.get(stack.getItem());
					count.target += stack.func_190916_E();
				}

			}

			return false;
		}

		protected void resetCounts(List<ItemStack> required) {
			counts = new HashMap<Item, ItemCount>();
			for (ItemStack stack : required) {
				if (counts.get(stack.getItem()) == null) {
					counts.put(stack.getItem(), new ItemCount());
				}
				ItemCount count = counts.get(stack.getItem());
				count.target += stack.func_190916_E();
			}
		}

	}

	protected boolean removeQuestFromPlayer(QuestData quest) {
		return PlayerCivilizationCapabilityImpl.get(quest.getPlayer()).removeQuest(quest);
	}

	public List<ItemStack> getRequiredItems(QuestData data) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Items.DIAMOND, 10));

		// TODO return i(data.getiData().get("type"));
		// Integer[] a = { Item.getIdFromItem(Items.DIAMOND) };
		return items;
	}

	public void setRequiredItems(QuestData data, List<ItemStack> items) {
		// data.getiData().put("type", cropType);
		// TODO
	}

	public Integer getTargetAmount(QuestData data) {
		return i(data.getiData().get("target"));
	}

	public void setTargetAmount(QuestData data, Integer targetAmount) {
		data.getiData().put("target", targetAmount);
	}

	public Integer getRewardRep(QuestData data) {
		return i(data.getiData().get("rep"));
	}

	public void setRewardRep(QuestData data, Integer rewardRep) {
		data.getiData().put("rep", rewardRep);
	}

	private Integer i(Object o) {
		try {
			return (Integer) o;
		} catch (Exception e) {
			return 0;
		}
	}
}
