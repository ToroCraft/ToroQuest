package net.torocraft.toroquest.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.torocraft.toroquest.item.armor.ItemReinforcedDiamondArmor;
import net.torocraft.toroquest.item.armor.ItemRoyalArmor;
import net.torocraft.toroquest.item.armor.ItemSamuraiArmor;
import net.torocraft.toroquest.item.armor.ItemToroArmor;

public class ToroQuestItems {

	@SideOnly(Side.CLIENT)
	public static final void registerRenders() {
		ItemRoyalArmor.registerRenders();
		ItemToroArmor.registerRenders();
		ItemReinforcedDiamondArmor.registerRenders();
		ItemSamuraiArmor.registerRenders();
		ItemToroLeather.registerRenders();
		ItemObsidianSword.registerRenders();
		ItemFireSword.registerRenders();
		ItemBattleAxe.registerRenders();
		ItemPickaxeOfGreed.registerRenders();
		ItemSwordOfPain.registerRenders();
		ItemSpicyChicken.registerRenders();
	}

}
