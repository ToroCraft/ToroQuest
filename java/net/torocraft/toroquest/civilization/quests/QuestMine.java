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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

/**
 * provide a special pick or shovel
 */

// TODO add an event handler to name the harvested blocks when using the given
// tool

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

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> in) {
		if (in == null) {
			return null;
		}

		// TODO check if they have the target amount of the correct block type
		// (blocks must be properly tagged)

		// TODO make sure they turned the tool back in

		return null;
	}

	@Override
	public void reject(QuestData data) {
		// TODO cost money or the tool to complete
	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {

		Block type = getBlockType(data);
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

		// TODO work in depth

		StringBuilder s = new StringBuilder();
		s.append("- Mine " + getTargetAmount(data) + " blocks of " + getBlockType(data).getLocalizedName() + "\n");
		s.append("- Use and return the provided tool\n");
		s.append("- Reward ").append(listItems(getRewardItems(data))).append("\n");
		s.append("- Recieve ").append(getRewardRep(data)).append(" reputation");
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

		int roll = rand.nextInt(20);
		setTargetAmount(data, 10 + roll);

		setBlockType(data, BLOCK_TYPES[rand.nextInt(BLOCK_TYPES.length)]);

		setRewardRep(data, 5 + (roll / 5));
		List<ItemStack> rewards = new ArrayList<ItemStack>(1);
		ItemStack emeralds = new ItemStack(Items.EMERALD, 4 + (roll / 10));
		setRewardItems(data, rewards);

		return data;
	}
	
	private Block getBlockType(QuestData data) {
		return BLOCK_TYPES[data.getiData().get("block_type")];
	}

	private void setBlockType(QuestData data, Block block) {
		for (int i = 0; i < BLOCK_TYPES.length; i++) {
			if (BLOCK_TYPES[i] == block) {
				data.getiData().put("block_type", i);
			}
		}
		throw new IllegalArgumentException(block.getUnlocalizedName());
	}

	private int getMaxDepth(QuestData data) {
		return data.getiData().get("max_depth");
	}

	/**
	 * setting the max depth to 0 or less will require the block to be on the
	 * top of bed rock
	 */
	private void setMaxDepth(QuestData data, int depth) {
		data.getiData().put("max_depth", depth);
	}
	
	private int getMinDepth(QuestData data) {
		return data.getiData().get("min_depth");
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
