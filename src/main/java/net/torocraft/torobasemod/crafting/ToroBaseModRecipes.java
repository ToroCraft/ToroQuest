package net.torocraft.torobasemod.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.torobasemod.ToroBaseMod;
import net.torocraft.torobasemod.item.ToroBaseModItems;
import net.torocraft.torobasemod.item.armor.ItemHeavyDiamondArmor;
import net.torocraft.torobasemod.item.armor.ItemKingArmor;
import net.torocraft.torobasemod.item.projectile.ItemTorchArrow;

public class ToroBaseModRecipes {
	
	public static final void init() {
		heavyDiamondArmor();
		kingArmor();
		torchArrow();
	}
	
	private static void torchArrow() {
		ItemStack stick = new ItemStack(Items.stick);
		ItemStack feather = new ItemStack(Items.feather);
		ItemStack coal = new ItemStack(Items.coal);
		ItemStack charcoal = new ItemStack(Items.coal, 1, 1);
		
		GameRegistry.addRecipe(new ItemStack(ItemTorchArrow.torchArrow), "c", "s", "f", 'c', coal, 's', stick, 'f', feather);
		GameRegistry.addRecipe(new ItemStack(ItemTorchArrow.torchArrow), "c", "s", "f", 'c', charcoal, 's', stick, 'f', feather);
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
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.helmetItem), "###", "# #", '#',  Items.diamond_helmet);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.chestplateItem), "# #", "###", "###", '#',  Items.diamond_chestplate);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.leggingsItem), "###", "# #", "# #", '#',  Items.diamond_leggings);
		GameRegistry.addRecipe(new ItemStack(ItemHeavyDiamondArmor.bootsItem), "# #", "# #", '#',  Items.diamond_boots);
	}
	
}
