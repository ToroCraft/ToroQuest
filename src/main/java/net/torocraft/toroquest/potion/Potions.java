package net.torocraft.toroquest.potion;

import com.google.common.base.Predicate;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Potions {

	public static void initRecipes() {
		PotionHelper.addMix(PotionTypes.AWKWARD, Ingredient.fromItem(Items.EMERALD), TQPotionTypes.ROYALTY);
		PotionHelper.addMix(TQPotionTypes.ROYALTY, Ingredient.fromItem(Items.REDSTONE), TQPotionTypes.ROYALTY_LONG);
		PotionHelper.addMix(TQPotionTypes.ROYALTY, Ingredient.fromItem(Items.GLOWSTONE_DUST), TQPotionTypes.ROYALTY_STRONG);

		PotionHelper.addMix(PotionTypes.AWKWARD, Ingredient.fromItem(Items.DIAMOND), TQPotionTypes.LOYALTY);
		PotionHelper.addMix(TQPotionTypes.LOYALTY, Ingredient.fromItem(Items.REDSTONE), TQPotionTypes.LOYALTY_LONG);
		PotionHelper.addMix(TQPotionTypes.LOYALTY, Ingredient.fromItem(Items.GLOWSTONE_DUST), TQPotionTypes.LOYALTY_STRONG);
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {

		event.getRegistry().registerAll(PotionRoyal.INSTANCE);

		initRecipes();
	}

}
