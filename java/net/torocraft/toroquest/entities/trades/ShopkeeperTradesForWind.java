package net.torocraft.toroquest.entities.trades;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.item.armor.ItemKingArmor;

public class ShopkeeperTradesForWind {
	public static MerchantRecipeList trades(EntityPlayer player, ReputationLevel rep) {
		MerchantRecipeList recipeList = new MerchantRecipeList();

		if (rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 15), level1Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 30), level2Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 15), level1Bow()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 30), level2Bow()));

			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 30), level1Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 30), level2Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 30), level3Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 30), level4Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 30), level5Boots()));
		}

		if (rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.STICK), ItemKingArmor.helmetItem));

		}

		if (rep.equals(ReputationLevel.HERO)) {
			/*
			 * only the best here
			 */
		}

		return recipeList;
	}

	private static ItemStack level1Boots() {
		ItemStack stack = new ItemStack(Items.LEATHER_BOOTS);
		stack.setStackDisplayName("Wind Boots I");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(2), 1);
		return stack;
	}

	private static ItemStack level2Boots() {
		ItemStack stack = new ItemStack(Items.LEATHER_BOOTS);
		stack.setStackDisplayName("Wind Boots II");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(2), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 1);
		return stack;
	}

	private static ItemStack level3Boots() {
		ItemStack stack = new ItemStack(Items.DIAMOND_BOOTS);
		stack.setStackDisplayName("Wind Boots III");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(2), 2);
		return stack;
	}

	private static ItemStack level4Boots() {
		ItemStack stack = new ItemStack(Items.DIAMOND_BOOTS);
		stack.setStackDisplayName("Wind Boots IIII");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(2), 4);
		return stack;
	}

	private static ItemStack level5Boots() {
		ItemStack stack = new ItemStack(Items.DIAMOND_BOOTS);
		stack.setStackDisplayName("Wind Boots V");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(2), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(70), 1);
		return stack;
	}

	private static ItemStack level1Bow() {
		ItemStack stack = new ItemStack(Items.BOW);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(49), 1);
		return stack;
	}

	private static ItemStack level2Bow() {
		ItemStack stack = new ItemStack(Items.BOW);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(49), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(48), 1);
		return stack;
	}

	protected static ItemStack level2Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(19), 2);
		return stack;
	}

	protected static ItemStack level1Sword() {
		ItemStack stack = new ItemStack(Items.IRON_SWORD);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(19), 1);
		return stack;
	}
}