package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.ReputationLevel;
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

		return "Gather Items";
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		s.append("- Gather: ").append(listItems(getRequiredItems(data))).append("\n");
		s.append("- Reward: ").append(listItems(getRewardItems(data))).append("\n");
		s.append("- Recieve ").append(getRewardRep(data)).append(" reputation");
		return s.toString();
	}

	private String listItems(List<ItemStack> items) {
		StringBuilder s = new StringBuilder();
		for (ItemStack stack : items) {
			s.append(" ").append(stack.func_190916_E()).append(" ").append(stack.getDisplayName());
		}
		return s.toString();
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
		int roll = rand.nextInt(100);
		int playerRep = PlayerCivilizationCapabilityImpl.get(player).getPlayerReputation(province.civilization);
		ReputationLevel level = ReputationLevel.fromReputation(playerRep);

		setRewardRep(data, 20 + roll);

		genItems(data, level, rand);

		Integer[] a = { Item.getIdFromItem(Items.DIAMOND) };

		return data;
	}

	protected void genItems(QuestData data, ReputationLevel level, Random rand) {
		List<ItemStack> items = new ArrayList<ItemStack>();

		// TODO items by rep level
		switch (level) {
		case HERO:

		case ALLY:
		case FRIEND:
		default:

		}

		pickRandomItems(rand, items);
		setRequiredItems(data, items);
	}

	private void pickRandomItems(Random rand, List<ItemStack> items) {
		items.add(new ItemStack(Items.FLINT_AND_STEEL, 1));
		items.add(new ItemStack(Blocks.OBSIDIAN, 10));
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
		Province province = loadProvice(data.getPlayer().worldObj, data.getPlayer().getPosition());

		if (province == null || !province.id.equals(data.getProvinceId())) {
			return null;
		}

		try {
			items = removeItems(getRequiredItems(data), items);
		} catch (InsufficientItems ex) {
			System.out.println("Insuifficient items: " + ex.getMessage());
			return null;
		}

		if (!removeQuestFromPlayer(data)) {
			return null;
		}

		PlayerCivilizationCapabilityImpl.get(data.getPlayer()).adjustPlayerReputation(data.getCiv(), getRewardRep(data));

		ItemStack emeralds = new ItemStack(Items.EMERALD, 20);
		items.add(emeralds);

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
			if (remainingRequired.func_190916_E() > 0) {
				throw new InsufficientItems(remainingRequired.func_190916_E() + " " + remainingRequired.getDisplayName());
			}
		}

		return givenItems;
	}

	private static void handleStackDecrement(ItemStack requiredItem, ItemStack givenItem) {
		if (!equals(requiredItem, givenItem)) {
			return;
		}

		if (requiredItem.func_190916_E() < 1 || givenItem.func_190916_E() < 1) {
			return;
		}
		int decrementBy = Math.min(requiredItem.func_190916_E(), givenItem.func_190916_E());
		requiredItem.func_190918_g(decrementBy);
		givenItem.func_190918_g(decrementBy);

		System.out.println("stack dec: " + decrementBy + " => G:" + givenItem.func_190916_E() + " R:" + requiredItem.func_190916_E());
	}

	private static boolean equals(ItemStack requiredItem, ItemStack givenItem) {
		ItemStack givenCopy = givenItem.copy();
		givenCopy.func_190920_e(requiredItem.func_190916_E());
		return ItemStack.areItemStacksEqual(givenCopy, requiredItem);
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

	protected boolean removeQuestFromPlayer(QuestData quest) {
		return PlayerCivilizationCapabilityImpl.get(quest.getPlayer()).removeQuest(quest);
	}

	public void setRewardItems(QuestData data, List<ItemStack> rewards) {
		setItemsToNbt(data, "rewards", rewards);
	}

	public void setRequiredItems(QuestData data, List<ItemStack> required) {
		setItemsToNbt(data, "required", required);
	}

	public List<ItemStack> getRequiredItems(QuestData data) {
		return getItemsFromNbt(data, "required");
	}

	public List<ItemStack> getRewardItems(QuestData data) {
		return getItemsFromNbt(data, "rewards");
	}

	public List<ItemStack> getItemsFromNbt(QuestData data, String name) {
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

	private List<ItemStack> getDefaultItems(String name) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Items.DIAMOND, 13));
		return items;
	}

	public void setItemsToNbt(QuestData data, String name, List<ItemStack> items) {
		NBTTagCompound c = getCustomNbtTag(data);
		NBTTagList list = new NBTTagList();
		for (ItemStack stack : items) {
			NBTTagCompound cStack = new NBTTagCompound();
			stack.writeToNBT(cStack);
			list.appendTag(cStack);
		}
		c.setTag(name, list);
	}

	protected NBTTagCompound getCustomNbtTag(QuestData data) {
		try {
			return (NBTTagCompound) data.getCustom();
		} catch (Exception e) {
			NBTTagCompound c = new NBTTagCompound();
			data.setCustom(c);
			return c;
		}
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
