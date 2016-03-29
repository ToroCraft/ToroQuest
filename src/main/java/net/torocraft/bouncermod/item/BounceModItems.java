package net.torocraft.bouncermod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.bouncermod.BouncerMod;
import net.minecraftforge.common.util.EnumHelper;

public class BounceModItems {
	
	private static final String MODID = BouncerMod.MODID;
	public static Item rubberSapItem;
	public static Item rawRubberItem;

	public static Item rubberSwordItem;
	public static Item flubberSwordItem;

	public static ToolMaterial RUBBER = EnumHelper.addToolMaterial("RUBBER", 0, 1561, .75F, 0.0F, 15);

	public static final void init() {
		initCraftingMaterials();
		initTools();
	}

	private static void initCraftingMaterials() {
		initRubberSap();
		initRawRubber();
	}
	
	private static void initRubberSap() {
		rubberSapItem = new Item().setUnlocalizedName("rubberSapItem").setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.registerItem(rubberSapItem, "rubberSap");
		ModelResourceLocation rubberSapModel = new ModelResourceLocation(MODID + ":rubberSap", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberSapItem, 0, rubberSapModel);		
	}

	private static void initRawRubber() {
		rawRubberItem = new Item().setUnlocalizedName("rawRubberItem").setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.registerItem(rawRubberItem, "rawRubber");
		ModelResourceLocation rawRubberModel = new ModelResourceLocation(MODID + ":rawRubber", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rawRubberItem, 0, rawRubberModel);		
	}
	
	private static void initTools() {
		initRubberSword();
		initFlubberSword();
	}

	private static void initRubberSword() {
		GameRegistry.registerItem(rubberSwordItem = new ItemRubberSword("rubberSword", RUBBER), "rubberSword");	
		ModelResourceLocation rubberSwordModel = new ModelResourceLocation(MODID + ":rubberSword", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberSwordItem, 0, rubberSwordModel);
	}
	
	private static void initFlubberSword() {
		GameRegistry.registerItem(flubberSwordItem = new ItemFlubberSword("flubberSword", RUBBER), "flubberSword");	
		ModelResourceLocation flubberSwordModel = new ModelResourceLocation(MODID + ":flubberSword", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(flubberSwordItem, 0, flubberSwordModel);
	}
}
