package net.torocraft.toroquest.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.toroquest.item.armor.ItemHeavyDiamondArmor;
import net.torocraft.toroquest.item.armor.ItemKingArmor;
import net.torocraft.toroquest.item.armor.ItemSamuraiArmor;

public class ToroBaseModRecipes {

	public static final void init() {
		heavyDiamondArmor();
		kingArmor();
		samuraiArmor();
	}

	private static void kingArmor() {
		ItemStack goldIngot = new ItemStack(Items.GOLD_INGOT);
		ItemStack diamond = new ItemStack(Items.DIAMOND);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.helmetItem), "ggg", "ddd", "d d", 'g', goldIngot, 'd', diamond);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.chestplateItem), "dgd", "ddd", "ddd", 'g', goldIngot, 'd', diamond);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.leggingsItem), "ddd", "dgd", "dgd", 'g', goldIngot, 'd', diamond);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.bootsItem), "g g", "d d", "d d", 'g', goldIngot, 'd', diamond);
	}

	private static void samuraiArmor() {
		ItemStack emerald = new ItemStack(Items.EMERALD);
		ItemStack leather = new ItemStack(Items.LEATHER);
		GameRegistry.addRecipe(new ItemStack(ItemSamuraiArmor.helmetItem), "ggg", "ddd", "d d", 'g', emerald, 'd', leather);
		GameRegistry.addRecipe(new ItemStack(ItemSamuraiArmor.chestplateItem), "dgd", "ddd", "ddd", 'g', emerald, 'd', leather);
		GameRegistry.addRecipe(new ItemStack(ItemSamuraiArmor.leggingsItem), "ddd", "dgd", "dgd", 'g', emerald, 'd', leather);
		GameRegistry.addRecipe(new ItemStack(ItemSamuraiArmor.bootsItem), "dgd", "dgd", 'g', emerald, 'd', leather);
	}

	private static void heavyDiamondArmor() {
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.helmetItem), "###", "# #", '#', Items.DIAMOND_HELMET);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.chestplateItem), "# #", "###", "###", '#', Items.DIAMOND_CHESTPLATE);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.leggingsItem), "###", "# #", "# #", '#', Items.DIAMOND_LEGGINGS);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.bootsItem), "# #", "# #", '#', Items.DIAMOND_BOOTS);
	}

}
