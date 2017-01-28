package net.torocraft.toroquest.potion;

import com.google.common.base.Predicate;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Potions {

	private static final Predicate<ItemStack> emerald = new PotionHelper.ItemPredicateInstance(Items.EMERALD);
	private static final Predicate<ItemStack> diamond = new PotionHelper.ItemPredicateInstance(Items.DIAMOND);
	private static final Predicate<ItemStack> redstone = new PotionHelper.ItemPredicateInstance(Items.REDSTONE);
	private static final Predicate<ItemStack> glowstone = new PotionHelper.ItemPredicateInstance(Items.GLOWSTONE_DUST);

	public static void initRecipes() {
		PotionHelper.registerPotionTypeConversion(PotionTypes.AWKWARD, emerald, TQPotionTypes.ROYALTY);
		PotionHelper.registerPotionTypeConversion(TQPotionTypes.ROYALTY, redstone, TQPotionTypes.ROYALTY_LONG);
		PotionHelper.registerPotionTypeConversion(TQPotionTypes.ROYALTY, glowstone, TQPotionTypes.ROYALTY_STRONG);

		PotionHelper.registerPotionTypeConversion(PotionTypes.AWKWARD, diamond, TQPotionTypes.LOYALTY);
		PotionHelper.registerPotionTypeConversion(TQPotionTypes.LOYALTY, redstone, TQPotionTypes.LOYALTY_LONG);
		PotionHelper.registerPotionTypeConversion(TQPotionTypes.LOYALTY, glowstone, TQPotionTypes.LOYALTY_STRONG);
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {

		event.getRegistry().registerAll(PotionRoyal.INSTANCE);

		initRecipes();
	}

}
