package net.torocraft.torobasemod.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.torobasemod.item.armor.ItemHeavyDiamodArmor;
import net.torocraft.torobasemod.item.armor.ItemKingArmor;

public class ToroBaseModRecipes {
	
	public static final void init() {
		heavyDiamondArmor();
		kingArmor();
	}
	
	private static void kingArmor() {
		ItemStack goldIngot = new ItemStack(Items.gold_ingot);
		ItemStack diamond = new ItemStack(Items.diamond);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.helmetItem), "ggg", "ddd", "d d", 'g', goldIngot, 'd', diamond);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.chestplateItem), "dgd", "ddd", "ddd", 'g', goldIngot, 'd', diamond);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.leggingsItem), "ddd", "dgd", "dgd", 'g', goldIngot, 'd', diamond);
		GameRegistry.addRecipe(new ItemStack(ItemKingArmor.bootsItem), "g g", "d d", "d d", 'g', goldIngot, 'd', diamond);
	}

	private static void heavyDiamondArmor() {
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.helmetItem), "###", "# #", '#',  Items.diamond_helmet);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.chestplateItem), "# #", "###", "###", '#',  Items.diamond_chestplate);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.leggingsItem), "###", "# #", "# #", '#',  Items.diamond_leggings);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.bootsItem), "# #", "# #", '#',  Items.diamond_boots);
	}
	
}
