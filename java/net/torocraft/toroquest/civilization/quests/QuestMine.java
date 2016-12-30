package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

/**
 * provide a special pick or shovel
 */

public class QuestMine extends QuestBase {
	public static QuestMine INSTANCE;
	
	private static final Block[] BLOCK_TYPES = { Blocks.GRAVEL, Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.OBSIDIAN, Blocks.REDSTONE_ORE };


	public static int ID;

	public static void init(int id) {
		INSTANCE = new QuestMine();
		Quests.registerQuest(id, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		ID = id;
	}

	@SubscribeEvent
	public void onMine(HarvestDropsEvent event) {
		if (event.getHarvester() == null) {
			return;
		}

		EntityPlayer player = event.getHarvester();
		Province inProvince = loadProvince(event.getHarvester().world, event.getPos());

		if (inProvince == null || inProvince.civilization == null) {
			return;
		}

		ItemStack tool = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

		if (!tool.hasTagCompound()) {
			return;
		}

		String questId = tool.getTagCompound().getString("mine_quest");
		String provinceId = tool.getTagCompound().getString("province");

		if (questId == null || provinceId == null || !provinceId.equals(inProvince.id.toString())) {
			return;
		}

		QuestData data = getQuestById(player, questId);

		if (data == null) {
			return;
		}

		if (notCurrentDepth(event, data)) {
			return;
		}

		if (event.getState().getBlock() != BLOCK_TYPES[getBlockType(data)]) {
			return;
		}

		event.setDropChance(1f);
		for (ItemStack drop : event.getDrops()) {
			drop.setTagInfo("mine_quest", new NBTTagString(questId));
			drop.setTagInfo("province", new NBTTagString(provinceId));
			drop.setStackDisplayName(drop.getDisplayName() + " for " + inProvince.name);
		}
	}

	protected boolean notCurrentDepth(HarvestDropsEvent event, QuestData data) {
		int max = getMaxDepth(data);
		int min = getMinDepth(data);
		int y = event.getPos().getY();

		if (min > 0 && y < min) {
			return true;
		}

		if (max > 0 && y > max) {
			return true;
		}

		return false;
	}

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> in) {
		if (in == null) {
			return null;
		}

		List<ItemStack> givenItems = copyItems(in);

		int requiredLeft = getTargetAmount(data);
		boolean toolIncluded = false;

		for (ItemStack item : givenItems) {
			if (isForThisQuest(data, item)) {
				if (item.getItem() instanceof ItemTool) {
					toolIncluded = true;
					item.setCount(0);
					System.out.println("found tool for this quest");
				} else {
					System.out.println("was for this quest: sub " + item.getCount());
					requiredLeft -= item.getCount();
					item.setCount(0);
				}
			} else {
				System.out.println("was NOT for this quest: sub " + item.getTagCompound());
			}
		}

		if (requiredLeft > 0) {
			data.getPlayer().sendMessage(new TextComponentString("You are " + requiredLeft + " short"));
			return null;
		}

		if (!toolIncluded) {
			data.getPlayer().sendMessage(new TextComponentString("You must turn in the tool that you were given"));
			return null;
		}

		givenItems = removeEmptyItemStacks(givenItems);
		addRewardItems(data, givenItems);
		return givenItems;
	}

	protected boolean isForThisQuest(QuestData data, ItemStack item) {
		if (!item.hasTagCompound()) {
			return false;
		}
		String wasMinedForQuestId = item.getTagCompound().getString("mine_quest");
		return data.getQuestId().toString().equals(wasMinedForQuestId);
	}

	@Override
	public void reject(QuestData data) {
		// TODO cost money or the tool to complete
	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {

		Block type = BLOCK_TYPES[getBlockType(data)];
		Province province = getQuestProvince(data);

		ItemStack tool;

		if (type == Blocks.GRAVEL) {
			tool = new ItemStack(Items.DIAMOND_SHOVEL);
		} else {
			tool = new ItemStack(Items.DIAMOND_PICKAXE);
		}

		tool.setStackDisplayName(tool.getDisplayName() + " of " + province.name);
		tool.addEnchantment(Enchantment.getEnchantmentByID(33), 1);
		tool.setTagInfo("mine_quest", new NBTTagString(data.getQuestId().toString()));
		tool.setTagInfo("province", new NBTTagString(province.id.toString()));

		in.add(tool);

		return in;
	}

	@Override
	public String getTitle(QuestData data) {
		return "Mine Quest";
	}

	@Override
	public String getDescription(QuestData data) {
		if (data == null) {
			return "";
		}

		StringBuilder s = new StringBuilder();
		s.append("- Mine " + getTargetAmount(data) + " blocks of " + BLOCK_TYPES[getBlockType(data)].getLocalizedName() + "\n");
		if (getMaxDepth(data) > 0 && getMinDepth(data) > 0) {
			s.append("- Between level " + getMinDepth(data) + " and " + getMaxDepth(data)).append("\n");
		} else if (getMaxDepth(data) > 0) {
			s.append("- Below level " + getMaxDepth(data)).append("\n");
		} else if (getMinDepth(data) > 0) {
			s.append("- Above level ").append(getMinDepth(data)).append("\n");
		}
		s.append("- Use and return the provided tool\n");
		s.append("- Reward ").append(listItems(getRewardItems(data))).append("\n");
		s.append("- Receive ").append(getRewardRep(data)).append(" reputation");
		return s.toString();
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province questProvince) {
		Random rand = player.world.rand;
		QuestData data = new QuestData();
		data.setCiv(questProvince.civilization);
		data.setPlayer(player);
		data.setProvinceId(questProvince.id);
		data.setQuestId(UUID.randomUUID());
		data.setQuestType(ID);
		data.setCompleted(false);

		setMaxDepth(data, 30);
		setMinDepth(data, 0);

		int roll = rand.nextInt(20);
		setTargetAmount(data, 10 + roll);

		setBlockType(data, rand.nextInt(BLOCK_TYPES.length));

		setRewardRep(data, 5 + (roll / 5));
		List<ItemStack> rewards = new ArrayList<ItemStack>(1);
		ItemStack emeralds = new ItemStack(Items.EMERALD, 4 + (roll / 10));
		rewards.add(emeralds);
		setRewardItems(data, rewards);

		return data;
	}
	
	private int getBlockType(QuestData data) {
		return coalesce(data.getiData().get("block_type"), 0);
	}

	private int coalesce(Integer integer, int i) {
		if (integer == null) {
			return i;
		}
		return integer;
	}

	private void setBlockType(QuestData data, int blockType) {
		data.getiData().put("block_type", blockType);

		/*
		 * for (int i = 0; i < BLOCK_TYPES.length; i++) { if (BLOCK_TYPES[i] ==
		 * block) { data.getiData().put("block_type", i); } } throw new
		 * IllegalArgumentException(block.getUnlocalizedName());
		 */
	}

	private int getMaxDepth(QuestData data) {
		return coalesce(data.getiData().get("max_depth"), 0);
	}

	private void setMaxDepth(QuestData data, int depth) {
		data.getiData().put("max_depth", depth);
	}
	
	private int getMinDepth(QuestData data) {
		return coalesce(data.getiData().get("min_depth"), 0);
	}

	private void setMinDepth(QuestData data, int depth) {
		data.getiData().put("min_depth", depth);
	}

	private int getTargetAmount(QuestData data) {
		return data.getiData().get("target_amount");
	}

	private void setTargetAmount(QuestData data, int amount) {
		data.getiData().put("target_amount", amount);
	}


}
