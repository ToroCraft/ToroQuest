package net.torocraft.bouncermod.material;

import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.torocraft.bouncermod.BouncerMod;

public class ArmorMaterials {
	
	private static final String MODID = BouncerMod.MODID;
	
	public static ArmorMaterial RUBBER = EnumHelper.addArmorMaterial("RUBBER", MODID + ":rubberArmor", 15, new int[]{2, 5, 4, 1}, 12, null);

}
