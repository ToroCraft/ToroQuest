package net.torocraft.toroquest.entities.trades;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.torocraft.toroquest.civilization.ReputationLevel;
import net.torocraft.toroquest.item.ItemFireSword;
import net.torocraft.toroquest.item.ItemSpicyChicken;

public class ShopkeeperTradesForFire {
	public static MerchantRecipeList trades(EntityPlayer player, ReputationLevel rep) {
		MerchantRecipeList recipeList = new MerchantRecipeList();

		if (rep.equals(ReputationLevel.DRIFTER) || rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), level1Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(10)), level2Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level3Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(30)), level4Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(40)), level5Sword()));
			
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(7)), level1Bow()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(15)), level2Bow()));
			
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(3)), level1FireProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(6)), level2FireProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(9)), level3FireProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(15)), level4FireProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level5FireProtection()));
		}
		
		if (rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(4)), new ItemStack(Items.CHICKEN, 1), new ItemStack(ItemSpicyChicken.INSTANCE)));
		}

		if (rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), new ItemStack(Blocks.MAGMA, 3), new ItemStack(ItemFireSword.INSTANCE)));
		}

		if (rep.equals(ReputationLevel.HERO)) {

		}

		return recipeList;
	}
	
	private static ItemStack level1Sword() {
		ItemStack stack = new ItemStack(Items.IRON_SWORD);
		stack.setStackDisplayName("Logi Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(20), 1);
		return stack;
	}
	
	private static ItemStack level2Sword() {
		ItemStack stack = new ItemStack(Items.IRON_SWORD);
		stack.setStackDisplayName("Vulcan Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(20), 2);
		return stack;
	}
	
	private static ItemStack level3Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Agni Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(20), 1);
		return stack;
	}
	
	private static ItemStack level4Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Hephaestus Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(20), 2);
		return stack;
	}
	
	private static ItemStack level5Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Apollo Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(20), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(70), 1);
		return stack;
	}
	
	private static ItemStack level1Bow() {
		ItemStack stack = new ItemStack(Items.BOW);
		stack.setStackDisplayName("Agni Bow");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(50), 1);
		return stack;
	}
	
	private static ItemStack level2Bow() {
		ItemStack stack = new ItemStack(Items.BOW);
		stack.setStackDisplayName("Apollo Bow");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(48), 5);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(50), 1);
		return stack;
	}
	
	private static ItemStack level1FireProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.setStackDisplayName("Fire Protection I");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(1), 1);
		return stack;
	}
	
	private static ItemStack level2FireProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.setStackDisplayName("Fire Protection II");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(1), 2);
		return stack;
	}
	
	private static ItemStack level3FireProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.setStackDisplayName("Fire Protection III");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(1), 3);
		return stack;
	}
	
	private static ItemStack level4FireProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.setStackDisplayName("Fire Protection IV");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(1), 4);
		return stack;
	}
	
	private static ItemStack level5FireProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.setStackDisplayName("Fire Protection V");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(1), 5);
		return stack;
	}
}