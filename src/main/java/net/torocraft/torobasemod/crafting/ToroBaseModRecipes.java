package net.torocraft.torobasemod.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.torobasemod.item.armor.ItemHeavyDiamodArmor;

public class ToroBaseModRecipes {
	
	public static final void init() {
		heavyDiamondArmor();
		
	}

	private static void heavyDiamondArmor() {
		
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.helmetItem), "###", "# #", '#',  Items.diamond_helmet);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.chestplateItem), "# #", "###", "###", '#',  Items.diamond_chestplate);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.leggingsItem), "###", "# #", "# #", '#',  Items.diamond_leggings);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamodArmor.bootsItem), "# #", "# #", '#',  Items.diamond_boots);
		
		/*
		GameRegistry.addShapelessRecipe(new ItemStack(ItemHeavyDiamodArmor.helmetItem), new Object[] {Items.diamond_helmet, Items.diamond_helmet});
		GameRegistry.addShapelessRecipe(new ItemStack(ItemHeavyDiamodArmor.chestplateItem), new Object[] {Items.diamond_chestplate, Items.diamond_chestplate});
		GameRegistry.addShapelessRecipe(new ItemStack(ItemHeavyDiamodArmor.leggingsItem), new Object[] {Items.diamond_leggings, Items.diamond_leggings});
		GameRegistry.addShapelessRecipe(new ItemStack(ItemHeavyDiamodArmor.bootsItem), new Object[] {Items.diamond_boots, Items.diamond_boots});
		*/
	}
	
}
