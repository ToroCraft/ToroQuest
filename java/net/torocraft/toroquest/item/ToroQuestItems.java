package net.torocraft.toroquest.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.item.armor.ItemBullArmor;
import net.torocraft.toroquest.item.armor.ItemHeavyDiamondArmor;
import net.torocraft.toroquest.item.armor.ItemKingArmor;
import net.torocraft.toroquest.item.armor.ItemSamuraiArmor;

public class ToroQuestItems {

	public static final void init() {
		initTools();
		initArmor();
	}

	private static void initTools() {
	}

	private static void initArmor() {
		ItemKingArmor.init();
		ItemBullArmor.init();
		ItemHeavyDiamondArmor.init();
		ItemSamuraiArmor.init();
	}
	
	@SideOnly(Side.CLIENT)
	public static final void registerRenders() {
		ItemKingArmor.registerRenders();
		ItemBullArmor.registerRenders();
		ItemHeavyDiamondArmor.registerRenders();
		ItemSamuraiArmor.registerRenders();
	}

}
