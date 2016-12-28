package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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

	private static QuestData baseQuest(Province province, EntityPlayer player) {
		QuestData data = new QuestData();
		data.setCiv(province.civilization);
		data.setPlayer(player);
		data.setProvinceId(province.id);
		data.setQuestId(UUID.randomUUID());
		data.setQuestType(ID);
		data.setCompleted(false);
		return data;
	}

	private static QuestData quest1(Province province, EntityPlayer player) {
		QuestData data = baseQuest(province, player);
		List<ItemStack> required = new ArrayList<ItemStack>();
		required.add(new ItemStack(Blocks.GRAVEL, 64));
		required.add(new ItemStack(Blocks.COBBLESTONE, 64));
		required.add(new ItemStack(Blocks.CLAY, 16));
		QuestGather.setRequiredItems(data, required);
		List<ItemStack> reward = new ArrayList<ItemStack>();
		reward.add(new ItemStack(Items.EMERALD, 4));
		QuestGather.setRewardItems(data, reward);
		setRewardRep(data, 10);
		return data;
	}

	private static QuestData questDarkOak(Province province, EntityPlayer player) {
		Random rand = player.world.rand;
		int roll = rand.nextInt(32);
		QuestData data = baseQuest(province, player);
		List<ItemStack> required = new ArrayList<ItemStack>();
		required.add(createMetaBlockStack(Blocks.LOG2, 1, 32 + roll));
		required.add(createMetaBlockStack(Blocks.LEAVES2, 1, 32 + roll));
		QuestGather.setRequiredItems(data, required);
		List<ItemStack> reward = new ArrayList<ItemStack>();
		reward.add(new ItemStack(Items.EMERALD, 5 + Math.round(roll / 10)));
		QuestGather.setRewardItems(data, reward);
		setRewardRep(data, 10);
		return data;
	}

	private static QuestData quest2(Province province, EntityPlayer player) {
		Random rand = player.world.rand;
		QuestData data = baseQuest(province, player);
		List<ItemStack> required = new ArrayList<ItemStack>();
		required.add(new ItemStack(Items.FLINT_AND_STEEL, 1));
		required.add(new ItemStack(Blocks.OBSIDIAN, 10));

		QuestGather.setRequiredItems(data, required);
		List<ItemStack> reward = new ArrayList<ItemStack>();
		reward.add(new ItemStack(Items.EMERALD, 3 + rand.nextInt(2)));
		QuestGather.setRewardItems(data, reward);
		setRewardRep(data, 10);
		return data;
	}

	private static ItemStack createMetaBlockStack(Block block, int meta, int amount) {
		ItemStack s = new ItemStack(block, amount);
		s.setItemDamage(meta);
		return s;
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {
		Random rand = player.getEntityWorld().rand;

		switch (rand.nextInt(3)) {
		case 0:
			return quest1(province, player);
		case 1:
			return quest2(province, player);
		case 2:
			return questDarkOak(province, player);
		default:
			return quest1(province, player);
		}
	}


	protected Province loadProvice(World world, BlockPos pos) {
		return CivilizationUtil.getProvinceAt(world, pos.getX() / 16, pos.getZ() / 16);
	}

	@Override
	public String getTitle(QuestData data) {
		if (data == null) {
			return "";
		}

		return "Gather Items";
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		s.append("- Gather ").append(listItems(getRequiredItems(data))).append("\n");
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
	public void reject(QuestData data) {

	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		return in;
	}

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> items) {
		Province province = loadProvice(data.getPlayer().world, data.getPlayer().getPosition());

		if (province == null || !province.id.equals(data.getProvinceId())) {
			return null;
		}

		try {
			items = removeItems(getRequiredItems(data), items);
		} catch (InsufficientItems ex) {
			data.getPlayer().sendMessage(new TextComponentString("Missing " + ex.getMessage()));
			return null;
		}

		PlayerCivilizationCapabilityImpl.get(data.getPlayer()).adjustReputation(data.getCiv(), getRewardRep(data));
		items.addAll(getRewardItems(data));
		return items;
	}

	public static List<ItemStack> removeItems(List<ItemStack> requiredIn, List<ItemStack> itemsIn) throws InsufficientItems {
		List<ItemStack> givenItems = copyItems(itemsIn);
		List<ItemStack> requiredItems = copyItems(requiredIn);

		for (ItemStack givenItem : givenItems) {
			for (ItemStack requiredItem : requiredItems) {
				handleStackDecrement(requiredItem, givenItem);
			}
		}

		for (ItemStack remainingRequired : requiredItems) {
			if (remainingRequired.getCount() > 0) {
				throw new InsufficientItems(remainingRequired.getCount() + " " + remainingRequired.getDisplayName());
			}
		}

		return givenItems;
	}

	private static void handleStackDecrement(ItemStack requiredItem, ItemStack givenItem) {
		if (!equals(requiredItem, givenItem)) {
			return;
		}

		if (requiredItem.getCount() < 1 || givenItem.getCount() < 1) {
			return;
		}
		int decrementBy = Math.min(requiredItem.getCount(), givenItem.getCount());
		requiredItem.shrink(decrementBy);
		givenItem.shrink(decrementBy);
	}

	private static boolean equals(ItemStack requiredItem, ItemStack givenItem) {
		return requiredItem.getItem() == givenItem.getItem();
	}

	public static class InsufficientItems extends Exception {
		public InsufficientItems(String message) {
			super(message);
		}
	}

	protected static List<ItemStack> copyItems(List<ItemStack> itemsIn) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack stack : itemsIn) {
			items.add(stack.copy());
		}
		return items;
	}

	public static void setRewardItems(QuestData data, List<ItemStack> rewards) {
		setItemsToNbt(data, "rewards", rewards);
	}

	public static void setRequiredItems(QuestData data, List<ItemStack> required) {
		setItemsToNbt(data, "required", required);
	}

	public static List<ItemStack> getRequiredItems(QuestData data) {
		return getItemsFromNbt(data, "required");
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

	private static Integer i(Object o) {
		try {
			return (Integer) o;
		} catch (Exception e) {
			return 0;
		}
	}
}
