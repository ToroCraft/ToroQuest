package net.torocraft.bouncermod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.bouncermod.BouncerMod;
import net.torocraft.bouncermod.item.armor.ItemRubberArmor;
import net.torocraft.bouncermod.item.crafting.ItemRawRubber;
import net.torocraft.bouncermod.item.crafting.ItemRubberSap;
import net.torocraft.bouncermod.item.tool.ItemFlubberSword;
import net.torocraft.bouncermod.item.tool.ItemRubberSword;

public class BounceModItems {

	private static final String MODID = BouncerMod.MODID;
	public static Item rubberSapItem;
	public static Item rawRubberItem;

	public static Item rubberSwordItem;
	public static Item flubberSwordItem;

	public static Item rubberHelmetItem;
	public static Item rubberChestplateItem;
	public static Item rubberLeggingsItem;
	public static Item rubberBootsItem;

	public static final void init() {
		initCraftingMaterials();
		initTools();
		initArmor();
	}

	public static void registerRenders() {
		ModelResourceLocation rubberSapModel = new ModelResourceLocation(MODID + ":" + ItemRubberSap.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberSapItem, 0, rubberSapModel);

		ModelResourceLocation rawRubberModel = new ModelResourceLocation(MODID + ":rawRubber", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rawRubberItem, 0, rawRubberModel);

		ModelResourceLocation rubberSwordModel = new ModelResourceLocation(MODID + ":" + ItemRubberSword.NAME,
				"inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberSwordItem, 0, rubberSwordModel);

		ModelResourceLocation flubberSwordModel = new ModelResourceLocation(MODID + ":" + ItemFlubberSword.NAME,
				"inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(flubberSwordItem, 0, flubberSwordModel);

		ModelResourceLocation rubberHelmetModel = new ModelResourceLocation(MODID + ":" + "rubber_helmet", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberHelmetItem, 0, rubberHelmetModel);

		ModelResourceLocation rubberChestplateModel = new ModelResourceLocation(MODID + ":" + "rubber_chestplate",
				"inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberChestplateItem, 0,
				rubberChestplateModel);

		ModelResourceLocation rubberLeggingsModel = new ModelResourceLocation(MODID + ":" + "rubber_leggings",
				"inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberLeggingsItem, 0,
				rubberLeggingsModel);

		ModelResourceLocation rubberBootModel = new ModelResourceLocation(MODID + ":" + "rubber_boots", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberBootsItem, 0, rubberBootModel);
	}

	private static void initCraftingMaterials() {
		initRubberSap();
		initRawRubber();
	}

	private static void initRubberSap() {
		rubberSapItem = new ItemRubberSap();
		GameRegistry.registerItem(rubberSapItem, ItemRubberSap.NAME);

	}

	private static void initRawRubber() {
		rawRubberItem = new ItemRawRubber();
		GameRegistry.registerItem(rawRubberItem, "rawRubber");

	}

	private static void initTools() {
		initRubberSword();
		initFlubberSword();
	}

	private static void initRubberSword() {
		GameRegistry.registerItem(rubberSwordItem = new ItemRubberSword(), ItemRubberSword.NAME);
	}

	private static void initFlubberSword() {
		GameRegistry.registerItem(flubberSwordItem = new ItemFlubberSword(), ItemFlubberSword.NAME);
	}

	private static void initArmor() {
		initRubberArmor();
	}

	private static void initRubberArmor() {
		GameRegistry.registerItem(rubberHelmetItem = new ItemRubberArmor("rubber_helmet", 1, EntityEquipmentSlot.HEAD),
				"rubber_helmet");
		GameRegistry.registerItem(
				rubberChestplateItem = new ItemRubberArmor("rubber_chestplate", 1, EntityEquipmentSlot.CHEST),
				"rubber_chestplate");
		GameRegistry.registerItem(
				rubberLeggingsItem = new ItemRubberArmor("rubber_leggings", 2, EntityEquipmentSlot.LEGS),
				"rubber_leggings");
		GameRegistry.registerItem(rubberBootsItem = new ItemRubberArmor("rubber_boots", 1, EntityEquipmentSlot.FEET),
				"rubber_boots");
	}
}
