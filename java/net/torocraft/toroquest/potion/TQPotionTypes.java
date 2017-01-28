package net.torocraft.toroquest.potion;

import javax.annotation.Nullable;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TQPotionTypes {
	public static final PotionType ROYALTY;
	public static final PotionType ROYALTY_LONG;
	public static final PotionType ROYALTY_STRONG;

	public static final PotionType LOYALTY;
	public static final PotionType LOYALTY_LONG;
	public static final PotionType LOYALTY_STRONG;

	static {
		final String LONG_PREFIX = "long_";
		final String STRONG_PREFIX = "strong_";

		final int HELPFUL_DURATION_STANDARD = 3600;
		final int HELPFUL_DURATION_LONG = 9600;
		final int HELPFUL_DURATION_STRONG = 1800;

		ROYALTY = createPotionType(new PotionEffect(PotionRoyal.INSTANCE, HELPFUL_DURATION_STANDARD));
		ROYALTY_LONG = createPotionType(new PotionEffect(PotionRoyal.INSTANCE, HELPFUL_DURATION_LONG), LONG_PREFIX);
		ROYALTY_STRONG = createPotionType(new PotionEffect(PotionRoyal.INSTANCE, HELPFUL_DURATION_STRONG, 1), STRONG_PREFIX);

		LOYALTY = createPotionType(new PotionEffect(PotionLoyalty.INSTANCE, HELPFUL_DURATION_STANDARD));
		LOYALTY_LONG = createPotionType(new PotionEffect(PotionLoyalty.INSTANCE, HELPFUL_DURATION_LONG), LONG_PREFIX);
		LOYALTY_STRONG = createPotionType(new PotionEffect(PotionLoyalty.INSTANCE, HELPFUL_DURATION_STRONG, 1), STRONG_PREFIX);
	}

	private static PotionType createPotionType(PotionEffect potionEffect) {
		return createPotionType(potionEffect, null);
	}

	private static PotionType createPotionType(PotionEffect potionEffect, @Nullable String namePrefix) {
		final ResourceLocation potionName = potionEffect.getPotion().getRegistryName();

		final ResourceLocation potionTypeName;
		if (namePrefix != null) {
			potionTypeName = new ResourceLocation(potionName.getResourceDomain(), namePrefix + potionName.getResourcePath());
		} else {
			potionTypeName = potionName;
		}

		return new PotionType(potionName.toString(), potionEffect).setRegistryName(potionTypeName);
	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
			event.getRegistry().registerAll(ROYALTY, ROYALTY_LONG, ROYALTY_STRONG, LOYALTY, LOYALTY_LONG, LOYALTY_STRONG);
		}
	}
}
