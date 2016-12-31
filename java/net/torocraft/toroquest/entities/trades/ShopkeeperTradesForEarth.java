package net.torocraft.toroquest.entities.trades;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.item.ItemBattleAxe;
import net.torocraft.toroquest.item.ItemToroLeather;
import net.torocraft.toroquest.item.armor.ItemToroArmor;

public class ShopkeeperTradesForEarth {
	public static MerchantRecipeList trades(EntityPlayer player, ReputationLevel rep) {
		MerchantRecipeList recipeList = new MerchantRecipeList();

		if (rep.equals(ReputationLevel.DRIFTER) || rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), level1Pick()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(10)), level2Pick()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level3Pick()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(30)), level4Pick()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(40)), level5Pick()));
			
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(9)), silkTouch()));
		}

		if (rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, 3), new ItemStack(Items.EMERALD, rep.adjustPrice(5)), new ItemStack(ItemBattleAxe.INSTANCE)));
		}
		
		if (rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 5), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemToroArmor.helmetItem)));
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 7), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemToroArmor.leggingsItem)));
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 4), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemToroArmor.bootsItem)));
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 8), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemToroArmor.chestplateItem)));
		}

		if (rep.equals(ReputationLevel.HERO)) {

		}

		return recipeList;
	}

	private static ItemStack level1Pick() {
		ItemStack stack = new ItemStack(Items.IRON_PICKAXE);
		stack.setStackDisplayName("Zeme Pickaxe");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(32), 1);
		return stack;
	}
	
	private static ItemStack level2Pick() {
		ItemStack stack = new ItemStack(Items.IRON_PICKAXE);
		stack.setStackDisplayName("Demeter Pickaxe");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(32), 3);
		return stack;
	}
	
	private static ItemStack level3Pick() {
		ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
		stack.setStackDisplayName("Terra Pickaxe");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(32), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(34), 1);
		return stack;
	}
	
	private static ItemStack level4Pick() {
		ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
		stack.setStackDisplayName("Gaea Pickaxe");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(32), 3);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(34), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(35), 1);
		return stack;
	}
	
	private static ItemStack level5Pick() {
		ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
		stack.setStackDisplayName("Paul's Prick");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(32), 5);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(34), 3);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(35), 3);
		return stack;
	}
	
	private static ItemStack silkTouch() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(33), 1);
		return stack;
	}
}