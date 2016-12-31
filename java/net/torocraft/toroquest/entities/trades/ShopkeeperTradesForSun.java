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
import net.torocraft.toroquest.item.armor.ItemSamuraiArmor;

public class ShopkeeperTradesForSun {
	public static MerchantRecipeList trades(EntityPlayer player, ReputationLevel rep) {
		MerchantRecipeList recipeList = new MerchantRecipeList();

		if (rep.equals(ReputationLevel.DRIFTER) || rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(5)), level1Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(10)), level2Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level3Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(30)), level4Sword()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(40)), level5Sword()));
			
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(3)), level1BlastProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(6)), level2BlastProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(9)), level3BlastProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(15)), level4BlastProtection()));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, rep.adjustPrice(20)), level5BlastProtection()));
		}

		if (rep.equals(ReputationLevel.FRIEND) || rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(Items.DIAMOND, 3), new ItemStack(Items.EMERALD, rep.adjustPrice(5)), new ItemStack(ItemBattleAxe.INSTANCE)));
		}
		
		if (rep.equals(ReputationLevel.ALLY) || rep.equals(ReputationLevel.HERO)) {
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 5), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemSamuraiArmor.helmetItem)));
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 7), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemSamuraiArmor.leggingsItem)));
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 4), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemSamuraiArmor.bootsItem)));
			recipeList.add(new MerchantRecipe(new ItemStack(ItemToroLeather.INSTANCE, 8), new ItemStack(Items.EMERALD, 5), new ItemStack(ItemSamuraiArmor.chestplateItem)));
		}

		if (rep.equals(ReputationLevel.HERO)) {

		}

		return recipeList;
	}
	
	private static ItemStack level1Sword() {
		ItemStack stack = new ItemStack(Items.IRON_SWORD);
		stack.setStackDisplayName("Svarog Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 1);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 1);
		return stack;
	}
	
	private static ItemStack level2Sword() {
		ItemStack stack = new ItemStack(Items.IRON_SWORD);
		stack.setStackDisplayName("Sol Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 3);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 3);
		return stack;
	}
	
	private static ItemStack level3Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Helios Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 2);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 2);
		return stack;
	}
	
	private static ItemStack level4Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Amaterasu Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 3);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 3);
		return stack;
	}
	
	private static ItemStack level5Sword() {
		ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
		stack.setStackDisplayName("Ra Sword");
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(17), 5);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(16), 5);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(70), 1);
		return stack;
	}

	private static ItemStack level1BlastProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(3), 1);
		return stack;
	}

	private static ItemStack level2BlastProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(3), 2);
		return stack;
	}

	private static ItemStack level3BlastProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(3), 3);
		return stack;
	}

	private static ItemStack level4BlastProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(3), 4);
		return stack;
	}

	private static ItemStack level5BlastProtection() {
		ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
		stack.addEnchantment(Enchantment.REGISTRY.getObjectById(3), 5);
		return stack;
	}
	
}