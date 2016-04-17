package net.torocraft.torobasemod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.torobasemod.ToroBaseMod;
import net.torocraft.torobasemod.item.armor.ItemBullArmor;
import net.torocraft.torobasemod.item.armor.ItemHeavyDiamodArmor;
import net.torocraft.torobasemod.item.armor.ItemKingArmor;

public class ToroBaseModItems {

	public static final void init() {
		initTools();
		initArmor();
	}

	private static void initTools() {

	}

	private static void initArmor() {
		ItemKingArmor.init();
		ItemBullArmor.init();
		ItemHeavyDiamodArmor.init();
	}

}
