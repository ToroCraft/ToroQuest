package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;

public abstract class QuestBase implements Quest {

	protected static ItemStack createMetaBlockStack(Block block, int meta, int amount) {
		ItemStack s = new ItemStack(block, amount);
		s.setItemDamage(meta);
		return s;
	}

	protected static Province loadProvince(World world, BlockPos pos) {
		return CivilizationUtil.getProvinceAt(world, pos.getX() / 16, pos.getZ() / 16);
	}

	protected static String listItems__OLD(List<ItemStack> items) {
		StringBuilder s = new StringBuilder();
		for (ItemStack stack : items) {
			s.append(" ").append(stack.getCount()).append(" ").append(stack.getDisplayName());
		}
		return s.toString();
	}

	protected static String listItems(List<ItemStack> rewardItems) {
		if (rewardItems == null || rewardItems.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();

		sb.append("L:");

		boolean first = true;
		for (ItemStack item : rewardItems) {
			if (first) {
				first = false;
			} else {
				sb.append(";");
			}
			sb.append(item.getItem().getUnlocalizedName());
			sb.append(",");
			sb.append(item.getCount());
		}
		return sb.toString();
	}

	protected static List<ItemStack> removeItems(List<ItemStack> requiredIn, List<ItemStack> itemsIn) throws InsufficientItems {
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

	protected static void handleStackDecrement(ItemStack requiredItem, ItemStack givenItem) {
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

	protected static boolean equals(ItemStack requiredItem, ItemStack givenItem) {
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

	protected static void setRewardItems(QuestData data, List<ItemStack> rewards) {
		setItemsToNbt(data, "rewards", rewards);
	}

	protected static void setRequiredItems(QuestData data, List<ItemStack> required) {
		setItemsToNbt(data, "required", required);
	}

	protected static List<ItemStack> getRequiredItems(QuestData data) {
		return getItemsFromNbt(data, "required");
	}

	protected static List<ItemStack> getRewardItems(QuestData data) {
		return getItemsFromNbt(data, "rewards");
	}

	protected static List<ItemStack> getItemsFromNbt(QuestData data, String name) {
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

	protected static List<ItemStack> getDefaultItems(String name) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(new ItemStack(Items.DIAMOND, 13));
		return items;
	}

	protected static void setItemsToNbt(QuestData data, String name, List<ItemStack> items) {
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

	protected static Integer getRewardRep(QuestData data) {
		return i(data.getiData().get("rep"));
	}

	protected static void setRewardRep(QuestData data, Integer rewardRep) {
		data.getiData().put("rep", rewardRep);
	}

	protected static Integer i(Object o) {
		try {
			return (Integer) o;
		} catch (Exception e) {
			return 0;
		}
	}

	protected static boolean isLiquid(IBlockState blockState) {
		return blockState.getBlock() == Blocks.WATER || blockState.getBlock() == Blocks.LAVA;
	}

	protected static boolean isGroundBlock(IBlockState blockState) {
		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() == Blocks.LOG || blockState.getBlock() instanceof BlockBush) {
			return false;
		}
		return blockState.isOpaqueCube();
	}

	protected static boolean cantBuildOver(IBlockState blockState) {
		if (blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2 || blockState.getBlock() instanceof BlockBush) {
			return false;
		}
		return blockState.isOpaqueCube();
	}

	protected static String getDirections(BlockPos from, BlockPos to) {
		if (from == null || to == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("D:");

		long distance = Math.round(from.getDistance(to.getX(), to.getY(), to.getZ()));
		int x = to.getX();
		int z = to.getY();

		sb.append(distance);
		sb.append(";").append(x);
		sb.append(";").append(z);
		return sb.toString();
	}

	/**
	 * @return the province that gave the quest
	 */
	protected static Province getQuestProvince(QuestData data) {
		return getProvinceById(data.getPlayer().world, data.getProvinceId());
	}

	protected static BlockPos getProvincePosition(Province province) {
		return new BlockPos(province.chunkX * 16, 80, province.chunkZ * 16);
	}

	protected static List<Province> getAllProvinces(World world) {
		return CivilizationsWorldSaveData.get(world).getProvinces();
	}

	protected static Province getProvinceById(World world, String id) {
		return getProvinceById(world, UUID.fromString(id));
	}

	protected static Province getProvinceById(World world, UUID id) {
		for (Province p : getAllProvinces(world)) {
			if (p.id.equals(id)) {
				return p;
			}
		}
		return null;
	}

	protected String getProvinceName(EntityPlayer player, UUID provinceId) {
		Province province = getProvinceById(player.world, provinceId);
		if (province == null) {
			return "";
		}
		return province.name;
	}

	protected static QuestData getQuestById(EntityPlayer player, String questId) {
		return getQuestById(player, UUID.fromString(questId));
	}

	protected static QuestData getQuestById(EntityPlayer player, UUID questId) {
		Set<QuestData> quests = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuests();
		if (quests == null) {
			return null;
		}
		for (QuestData data : quests) {
			if (data.getQuestId().equals(questId)) {
				return data;
			}
		}
		return null;
	}

	public static List<ItemStack> removeEmptyItemStacks(List<ItemStack> givenItems) {
		List<ItemStack> itemsToReturn = new ArrayList<ItemStack>();
		for (ItemStack item : givenItems) {
			if (!item.isEmpty()) {
				itemsToReturn.add(item);
			}
		}
		return itemsToReturn;
	}

	protected static void addRewardItems(QuestData data, List<ItemStack> givenItems) {
		if (getRewardItems(data) == null || givenItems == null) {
			return;
		}
		givenItems.addAll(getRewardItems(data));
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

}
