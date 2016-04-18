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
	}
	
}
