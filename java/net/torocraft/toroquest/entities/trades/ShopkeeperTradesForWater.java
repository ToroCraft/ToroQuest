package net.torocraft.toroquest.entities.trades;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.item.ItemBattleAxe;

public class ShopkeeperTradesForWater {
	public static MerchantRecipeList trades(EntityPlayer player, ReputationLevel rep) {
		MerchantRecipeList recipeList = new MerchantRecipeList();

		if (rep.equals(ReputationLevel.DRIFTER) || rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), level1Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(10)), level2Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level3Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(30)), level4Boots()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(40)), level5Boots()));
			
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), level1Helmet()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(10)), level2Helmet()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level3Helmet()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(30)), level4Helmet()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(40)), level5Helmet()));
		}
		
		if (rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, 3), new ItemStack(Items.EMERALD, rep.adjustPrice(5)), new ItemStack(ItemBattleAxe.INSTANCE)));
		}

		if (rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(25)), loChangsRod()));
		}

		if (rep.equals(ReputationLevel.HERO)) {

		}

		return recipeList;
	}

	private static ItemStack loChangsRod() {
		ItemStack stack = new ItemStack(Items.FISHING_ROD);
		stack.setStackDisplayName("Lo Chang's Rod");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(61), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(34), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(62), 4);
		return stack;
	}

	private static ItemStack level1Helmet() {
		ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
		stack.setStackDisplayName("Delphin Helmet");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(6), 1);
		return stack;
	}

	private static ItemStack level2Helmet() {
		ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
		stack.setStackDisplayName("Brizo Helmet");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(5), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(6), 4);
		return stack;
	}

	private static ItemStack level3Helmet() {
		ItemStack stack = new ItemStack(Items.DIAMOND_HELMET);
		stack.setStackDisplayName("Charybdis Helmet");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(6), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(5), 2);
		return stack;
	}

	private static ItemStack level4Helmet() {
		ItemStack stack = new ItemStack(Items.DIAMOND_HELMET);
		stack.setStackDisplayName("Oceanus Helmet");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(6), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(5), 4);
		return stack;
	}

	private static ItemStack level5Helmet() {
		ItemStack stack = new ItemStack(Items.DIAMOND_HELMET);
		stack.setStackDisplayName("Poseidon Helmet");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(6), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(5), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(70), 1);
		return stack;
	}
	
	private static ItemStack level1Boots() {
		ItemStack stack = new ItemStack(Items.LEATHER_BOOTS);
		stack.setStackDisplayName("Delphin Boots");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(8), 1);
		return stack;
	}

	private static ItemStack level2Boots() {
		ItemStack stack = new ItemStack(Items.LEATHER_BOOTS);
		stack.setStackDisplayName("Brizo Boots");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(8), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 1);
		return stack;
	}

	private static ItemStack level3Boots() {
		ItemStack stack = new ItemStack(Items.DIAMOND_BOOTS);
		stack.setStackDisplayName("Charybdis Boots");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(8), 2);
		return stack;
	}

	private static ItemStack level4Boots() {
		ItemStack stack = new ItemStack(Items.DIAMOND_BOOTS);
		stack.setStackDisplayName("Oceanus Boots");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(8), 4);
		return stack;
	}

	private static ItemStack level5Boots() {
		ItemStack stack = new ItemStack(Items.DIAMOND_BOOTS);
		stack.setStackDisplayName("Poseidon Boots");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(0), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(8), 4);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(70), 1);
		return stack;
	}
}