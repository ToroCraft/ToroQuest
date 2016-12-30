package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

//TODO logic to listen to the village lord GUI and transform notes to replies

public class QuestCourier extends QuestBase implements Quest {
	public static QuestCourier INSTANCE;

	public static int ID;

	public static void init(int id) {
		INSTANCE = new QuestCourier();
		Quests.registerQuest(id, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		ID = id;
	}

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> in) {

		ItemStack note = getNoteFromItems(data, in);

		if (note == null) {
			return null;
		}

		note.setCount(0);
		in.remove(note);

		List<ItemStack> rewards = getRewardItems(data);
		if (rewards != null) {
			in.addAll(rewards);
		}

		return in;
	}

	protected ItemStack getNoteFromItems(QuestData data, List<ItemStack> in) {
		for (ItemStack s : in) {
			if (isReplyNoteForQuest(data, s)) {
				return s;
			}
		}
		return null;
	}

	protected boolean isReplyNoteForQuest(QuestData data, ItemStack item) {
		if (!item.hasTagCompound() || !item.hasTagCompound()) {
			return false;
		}

		String noteAddressedTo = item.getTagCompound().getString("toProvince");
		String noteQuestId = item.getTagCompound().getString("questId");

		if (noteAddressedTo == null || noteQuestId == null) {
			return false;
		}

		/*
		 * must be addressed to the province that created the quest
		 */
		if (!noteAddressedTo.equals(data.getProvinceId().toString())) {
			return false;
		}

		/*
		 * quest ID must match
		 */
		return noteQuestId.equals(data.getQuestId().toString());
	}

	@Override
	public List<ItemStack> reject(QuestData data, List<ItemStack> in) {
		return in;
	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		Province deliverToProvince = getDeliverToProvince(data);
		ItemStack note = new ItemStack(Items.PAPER);
		note.setStackDisplayName("Deliver to the Lord of " + deliverToProvince.name);
		note.setTagInfo("toProvince", new NBTTagString(deliverToProvince.id.toString()));
		note.setTagInfo("questId", new NBTTagString(data.getQuestId().toString()));
		in.add(note);
		return in;
	}

	@Override
	public String getTitle(QuestData data) {
		return "Courier";
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}
		Province deliverToProvince = getDeliverToProvince(data);
		StringBuilder s = new StringBuilder();
		s.append("- Deliver a note to " + deliverToProvince.name + " and return the reply\n");
		s.append("- Location: [" + deliverToProvince.chunkX * 16 + ", " + deliverToProvince.chunkZ * 16 + "] " + getDistance(data) + "m\n");
		s.append("- Reward ").append(listItems(getRewardItems(data))).append("\n");
		s.append("- Receive ").append(getRewardRep(data)).append(" reputation");
		return s.toString();
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province questProvince) {
		Province deliverToProvince = chooseRandomProvince(questProvince, player.world);
		if (deliverToProvince == null) {
			return null;
		}
		QuestData data = new QuestData();
		data.setCiv(questProvince.civilization);
		data.setPlayer(player);
		data.setProvinceId(questProvince.id);
		data.setQuestId(UUID.randomUUID());
		data.setQuestType(ID);
		data.setCompleted(false);
		setDeliverToProvinceId(data, deliverToProvince.id);
		setDistance(data, (int) Math.round(player.getPosition().getDistance(deliverToProvince.chunkX * 16, (int) player.posY, deliverToProvince.chunkZ * 16)));
		setRewardRep(data, 5 + (getDistance(data) / 50));

		List<ItemStack> rewards = new ArrayList<ItemStack>(1);
		ItemStack emeralds = new ItemStack(Items.EMERALD, 4 + (getDistance(data) / 80));
		setRewardItems(data, rewards);

		return data;
	}

	public static Province chooseRandomProvince(Province exclude, World world) {

		List<Province> provinces = getAllProvinces(world);
		if (provinces.size() < 2) {
			return null;
		}

		Collections.shuffle(provinces);

		for (Province p : provinces) {
			if (exclude == null || p.id != exclude.id) {
				return p;
			}
		}

		return null;
	}

	private void setDeliverToProvinceId(QuestData data, UUID id) {
		data.getsData().put("deliverTo", id.toString());
	}

	private UUID getDeliverToProvinceId(QuestData data) {
		return UUID.fromString(data.getsData().get("deliverTo"));
	}

	private Province getDeliverToProvince(QuestData data) {
		Province p = getProvinceById(data.getPlayer().world, data.getsData().get("deliverTo"));
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
