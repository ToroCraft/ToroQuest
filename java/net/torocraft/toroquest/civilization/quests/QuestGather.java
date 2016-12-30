package net.torocraft.toroquest.civilization.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

public class QuestGather extends QuestBase implements Quest {

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
		s.append("- Receive ").append(getRewardRep(data)).append(" reputation");
		return s.toString();
	}

	@Override
	public List<ItemStack> reject(QuestData data, List<ItemStack> in) {
		return in;
	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		return in;
	}

	@Override
	public List<ItemStack> complete(QuestData data, List<ItemStack> items) {
		Province province = loadProvince(data.getPlayer().world, data.getPlayer().getPosition());

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
}
