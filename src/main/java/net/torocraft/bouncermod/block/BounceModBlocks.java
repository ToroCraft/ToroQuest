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

	public static final void init() {
		initRubber();
		initFlubber();
		initLauncher();
	}

	private static void initRubber() {
		rubberBlock = new BlockRubber();
		GameRegistry.registerBlock(rubberBlock, BlockRubber.NAME);
		Item rubberBlockItem = GameRegistry.findItem(MODID, BlockRubber.NAME);
		ModelResourceLocation rubberBlockModel = new ModelResourceLocation(MODID + ":" + BlockRubber.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(rubberBlockItem, 0, rubberBlockModel);
	}

	private static void initFlubber() {
		flubberBlock = new BlockFlubber();
		GameRegistry.registerBlock(flubberBlock, BlockFlubber.NAME);
		Item flubberBlockItem = GameRegistry.findItem(MODID, BlockFlubber.NAME);
		ModelResourceLocation flubberBlockModel = new ModelResourceLocation(MODID + ":" + BlockFlubber.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(flubberBlockItem, 0, flubberBlockModel);
	}

	private static void initLauncher() {
		launchBlock = new BlockLaunch();
		GameRegistry.registerBlock(launchBlock, BlockLaunch.NAME);
		Item launchBlockItem = GameRegistry.findItem(MODID, BlockLaunch.NAME);
		ModelResourceLocation launchBlockModel = new ModelResourceLocation(MODID + ":" + BlockLaunch.NAME, "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(launchBlockItem, 0, launchBlockModel);
	}
}
