package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

//TODO logic to listen to the village lord GUI and transform notes to replies

public class QuestCourier implements Quest {
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
	public void reject(QuestData data) {

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
		s.append("- Recieve ").append(getRewardRep(data)).append(" reputation");
		return s.toString();
	}

	private String listItems(List<ItemStack> items) {
		StringBuilder s = new StringBuilder();
		for (ItemStack stack : items) {
			s.append(" ").append(stack.getCount()).append(" ").append(stack.getDisplayName());
		}
		return s.toString();
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province questProvince) {
		Province deliverToProvince = getDeliverToProvince(questProvince, player);
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

	private Province getDeliverToProvince(Province questProvince, EntityPlayer player) {

		List<Province> provinces = getAllProvinces(player);
		if (provinces.size() < 2) {
			return null;
		}

		Collections.shuffle(provinces);

		for (Province p : provinces) {
			if (p.id != questProvince.id) {
				return p;
			}
		}

		return null;
	}

	protected List<Province> getAllProvinces(EntityPlayer player) {
		return CivilizationsWorldSaveData.get(player.world).getProvinces();
	}

	private void setDeliverToProvinceId(QuestData data, UUID id) {
		data.getsData().put("deliverTo", id.toString());
	}

	private UUID getDeliverToProvinceId(QuestData data) {
		return UUID.fromString(data.getsData().get("deliverTo"));
	}

	private Province getDeliverToProvince(QuestData data) {
		UUID id = UUID.fromString(data.getsData().get("deliverTo"));
		for (Province p : getAllProvinces(data.getPlayer())) {
			if (p.id.equals(id)) {
				return p;
			}
		}
		throw new UnsupportedOperationException("Deliever to provice ID[" + id + "] was not found");
	}

	public static void setRewardItems(QuestData data, List<ItemStack> rewards) {
		setItemsToNbt(data, "rewards", rewards);
	}

	public static List<ItemStack> getRewardItems(QuestData data) {
		return getItemsFromNbt(data, "rewards");
	}

	public static List<ItemStack> getItemsFromNbt(QuestData data, String name) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		NBTTagCompound c = getCustomNbtTag(data);
		try {
			NBTTagList list = (NBTTagList) c.getTag(name);
			for (int i = 0; i < list.tagCount(); i++) {
				items.add(new ItemStack(list.getCompoundTagAt(i)));
			}
			return items;
		} catch (Exception e) {
			return getDefaultItems(name);
		}
	}

	private static List<ItemStack> getDefaultItems(String name) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Items.DIAMOND, 13));
		return items;
	}

	public static void setItemsToNbt(QuestData data, String name, List<ItemStack> items) {
		NBTTagCompound c = getCustomNbtTag(data);
		NBTTagList list = new NBTTagList();
		for (ItemStack stack : items) {
			NBTTagCompound cStack = new NBTTagCompound();
			stack.writeToNBT(cStack);
			list.appendTag(cStack);
		}
		c.setTag(name, list);
	}

	protected static NBTTagCompound getCustomNbtTag(QuestData data) {
		try {
			return (NBTTagCompound) data.getCustom();
		} catch (Exception e) {
			NBTTagCompound c = new NBTTagCompound();
			data.setCustom(c);
			return c;
		}
	}

	public static Integer getRewardRep(QuestData data) {
		return i(data.getiData().get("rep"));
	}

	public static void setRewardRep(QuestData data, Integer rewardRep) {
		data.getiData().put("rep", rewardRep);
	}

	public static Integer getDistance(QuestData data) {
		return i(data.getiData().get("distance"));
	}

	public static void setDistance(QuestData data, Integer distance) {
		data.getiData().put("distance", distance);
	}

	private static Integer i(Object o) {
		try {
			return (Integer) o;
		} catch (Exception e) {
			return 0;
		}
	}

}
