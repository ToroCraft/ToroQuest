package net.torocraft.toroquest.civilization.quests;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.Quest;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.Quests;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reject(QuestData data) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ItemStack> accept(QuestData data, List<ItemStack> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle(QuestData data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription(QuestData data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestData generateQuestFor(EntityPlayer player, Province province) {
		// TODO Auto-generated method stub
		return null;
	}
}
