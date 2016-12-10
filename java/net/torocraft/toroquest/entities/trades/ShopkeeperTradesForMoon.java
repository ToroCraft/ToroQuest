package net.torocraft.toroquest.entities.trades;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.torocraft.toroquest.civilization.ReputationLevel;

public class ShopkeeperTradesForMoon {
	public static MerchantRecipeList trades(EntityPlayer player, ReputationLevel rep) {
		MerchantRecipeList recipeList = new MerchantRecipeList();

		if (rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), level1Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(10)), level2Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level3Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(30)), level4Sword()));
			
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), level1Chestplate()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(10)), level2Chestplate()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level3Chestplate()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(30)), level4Chestplate()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(40)), level5Chestplate()));
		}

		if (rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {

		}

		if (rep.equals(ReputationLevel.HERO)) {

		}

		return recipeList;
	}

	private static ItemStack level1Sword() {
		ItemStack stack = new ItemStack(Items.IRON_SWORD);
		stack.setStackDisplayName("Moon Sword I");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(18), 1);
		return stack;
	}
	
	private static ItemStack level2Sword() {
		ItemStack stack = new ItemStack(Items.IRON_SWORD);
		stack.setStackDisplayName("Moon Sword II");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(18), 2);
		return stack;
	}
	
	private static ItemStack level3Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Moon Sword III");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(18), 2);
		return stack;
	}
	
	private static ItemStack level4Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Moon Sword IV");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(18), 4);
		return stack;
	}
	
	private static ItemStack level1Chestplate() {
		ItemStack stack = new ItemStack(Items.LEATHER_CHESTPLATE);
		stack.setStackDisplayName("Moon Chestplate I");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(7), 1);
		return stack;
	}
	
	private static ItemStack level2Chestplate() {
		ItemStack stack = new ItemStack(Items.LEATHER_CHESTPLATE);
		stack.setStackDisplayName("Moon Chestplate II");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(7), 4);
		return stack;
	}
	
	private static ItemStack level3Chestplate() {
		ItemStack stack = new ItemStack(Items.DIAMOND_CHESTPLATE);
		stack.setStackDisplayName("Moon Chestplate III");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(7), 1);
		return stack;
	}
	
	private static ItemStack level4Chestplate() {
		ItemStack stack = new ItemStack(Items.DIAMOND_CHESTPLATE);
		stack.setStackDisplayName("Moon Chestplate IV");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(7), 3);
		return stack;
	}
	
	private static ItemStack level5Chestplate() {
		ItemStack stack = new ItemStack(Items.DIAMOND_CHESTPLATE);
		stack.setStackDisplayName("Moon Chestplate V");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(7), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(70), 1);
		return stack;
	}
	
}