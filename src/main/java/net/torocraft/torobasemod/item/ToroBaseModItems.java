package net.torocraft.torobasemod.item;

import net.torocraft.torobasemod.item.armor.ItemBullArmor;
import net.torocraft.torobasemod.item.armor.ItemHeavyDiamondArmor;
import net.torocraft.torobasemod.item.armor.ItemKingArmor;
import net.torocraft.torobasemod.item.projectile.ItemTorchArrow;

public class ToroBaseModItems {

	public static final void init() {
		initTools();
		initArmor();
	}

	private static void initTools() {
		ItemTorchArrow.init();
	}

	private static void initArmor() {
		ItemKingArmor.init();
		ItemBullArmor.init();
		ItemHeavyDiamondArmor.init();
	}
	
	public static final void registerRenders() {
		ItemKingArmor.registerRenders();
		ItemBullArmor.registerRenders();
		ItemHeavyDiamondArmor.registerRenders();
	}

}
