package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

/**
 * provide a special pick or shovel
 */

public class QuestBountyHunter extends QuestBase {
	public static QuestBountyHunter INSTANCE;

	public static int ID;

	public static void init(int id) {
		INSTANCE = new QuestBountyHunter();
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
	public List<ItemStack> reject(QuestData data, List<ItemStack> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle(QuestData data) {
		return "quests.bounty_hunter.title";
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}
		Province deliverToProvince = getLocation(data);
		StringBuilder s = new StringBuilder();
		s.append("quests.bounty_hunter.description");
		s.append("|").append(deliverToProvince.name);
		s.append("|").append("[" + deliverToProvince.chunkX * 16 + ", " + deliverToProvince.chunkZ * 16 + "]");
		s.append("|").append(getDistance(data));
		s.append("|").append(listItems(getRewardItems(data)));
		s.append("|").append(getRewardRep(data));
		return s.toString();
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {
		Province locationProvince = chooseRandomProvince(province, player.world, true);
		if (locationProvince == null) {
			return null;
		}
		QuestData data = new QuestData();
		data.setCiv(province.civilization);
		data.setPlayer(player);
		data.setProvinceId(province.id);
		data.setQuestId(UUID.randomUUID());
		data.setQuestType(ID);
		data.setCompleted(false);
		setLocationId(data, locationProvince.id);
		setDistance(data, (int) Math.round(player.getPosition().getDistance(locationProvince.chunkX * 16, (int) player.posY, locationProvince.chunkZ * 16)));
		setRewardRep(data, 5 + (getDistance(data) / 50));

		List<ItemStack> rewards = new ArrayList<ItemStack>(1);
		ItemStack emeralds = new ItemStack(Items.EMERALD, 8 + (getDistance(data) / 200));
		rewards.add(emeralds);
		setRewardItems(data, rewards);

		return data;
	}

	private void setLocationId(QuestData data, UUID id) {
		data.getsData().put("location", id.toString());
	}

	private UUID getLocationId(QuestData data) {
		return UUID.fromString(data.getsData().get("location"));
	}

	private Province getLocation(QuestData data) {
		Province p = getProvinceById(data.getPlayer().world, data.getsData().get("location"));
		if (p == null) {
			throw new UnsupportedOperationException("Deliever to provice ID[" + data.getsData().get("deliverTo") + "] was not found");
		}
		return p;
	}

	public static Integer getDistance(QuestData data) {
		return i(data.getiData().get("distance"));
	}

	public static void setDistance(QuestData data, Integer distance) {
		data.getiData().put("distance", distance);
	}
}
