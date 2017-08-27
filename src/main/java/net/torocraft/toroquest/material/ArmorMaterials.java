package net.torocraft.toroquest.material;

import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.torocraft.toroquest.ToroQuest;

public class ArmorMaterials {
	
	private static final String MODID = ToroQuest.MODID;
	
	public static ArmorMaterial ROYAL = EnumHelper.addArmorMaterial("ROYAL", MODID + ":royal_armor", 36, new int[] { 3, 6, 8, 3 }, 25, null, 2);
	public static ArmorMaterial REINFORCED_DIAMOND = EnumHelper.addArmorMaterial("REINFORCED_DIAMOND", MODID + ":reinforced_diamond_armor", 30, new int[] { 3, 6, 8, 3 }, 5, null, 2);
	public static ArmorMaterial TORO = EnumHelper.addArmorMaterial("TORO", MODID + ":toro_armor", 10, new int[] { 2, 5, 4, 1 }, 25, null, 0);
	public static ArmorMaterial SAMURAI = EnumHelper.addArmorMaterial("SAMURAI", MODID + ":samurai_armor", 10, new int[] { 3, 6, 8, 3 }, 25, null, 0);

}
