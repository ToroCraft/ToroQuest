package net.torocraft.bouncermod.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.torocraft.bouncermod.BouncerMod;

public class BounceModBlocks {
	
	public static final String MODID = BouncerMod.MODID;
	public static Block rubberBlock;
	public static Block flubberBlock;
	public static Block launchBlock;
	public static Block rubberWoodBlock;
	
	public static Item rubberBlockItem;
	public static Item flubberBlockItem;
	public static Item launchBlockItem;
	public static Item rubberWoodBlockItem;

	public static final void init() {
		initRubber();
		initFlubber();
		initLauncher();
		initRubberWood();
	}
	
	public static void registerRenders() {
		ModelResourceLocation rubberBlockModel = new ModelResourceLocation(MODID + ":" + BlockRubber.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberBlockItem, 0, rubberBlockModel);
		
		ModelResourceLocation flubberBlockModel = new ModelResourceLocation(MODID + ":" + BlockFlubber.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(flubberBlockItem, 0, flubberBlockModel);
		
		ModelResourceLocation launchBlockModel = new ModelResourceLocation(MODID + ":" + BlockLaunch.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(launchBlockItem, 0, launchBlockModel);
		
		ModelResourceLocation rubberWoodModel = new ModelResourceLocation(MODID + ":" + BlockRubberWood.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberWoodBlockItem, 0, rubberWoodModel);
	}

	private static void initRubber() {
		rubberBlock = new BlockRubber();
		GameRegistry.registerBlock(rubberBlock, BlockRubber.NAME);
		rubberBlockItem = GameRegistry.findItem(MODID, BlockRubber.NAME);
		
	}

	private static void initFlubber() {
		flubberBlock = new BlockFlubber();
		GameRegistry.registerBlock(flubberBlock, BlockFlubber.NAME);
		flubberBlockItem = GameRegistry.findItem(MODID, BlockFlubber.NAME);
	}

	private static void initLauncher() {
		launchBlock = new BlockLaunch();
		GameRegistry.registerBlock(launchBlock, BlockLaunch.NAME);
		launchBlockItem = GameRegistry.findItem(MODID, BlockLaunch.NAME);
	}

	private static void initRubberWood() {
		rubberWoodBlock = new BlockRubberWood();
		GameRegistry.registerBlock(rubberWoodBlock, BlockRubberWood.NAME);
		rubberWoodBlockItem = GameRegistry.findItem(MODID, BlockRubberWood.NAME);
	}
}
