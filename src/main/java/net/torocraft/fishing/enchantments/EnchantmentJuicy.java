package net.torocraft.fishing.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentJuicy extends Enchantment {

	public static final String NAME = "juicy";
	
	public EnchantmentJuicy(Rarity rarityIn, EnumEnchantmentType typeIn,
			EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
		setName(NAME);
	}
}
