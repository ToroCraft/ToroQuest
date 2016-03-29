package net.torocraft.cloudmod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod (modid = CloudMod.MODID, name = CloudMod.MODNAME, version = CloudMod.VERSION)
public class CloudMod {
	
	public static final String MODID = "cloudmod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "CloudMod";
	
	
	public static Block cloudBlock;
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		cloudBlock = new CloudBlock();
		
		GameRegistry.registerBlock(cloudBlock, CloudBlock.NAME);
		
		Item cloudBlockItem = GameRegistry.findItem(MODID, CloudBlock.NAME);
		
		ModelResourceLocation cloudBlockModel = new ModelResourceLocation("cloudmod:cloudBlock", "inventory");

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(cloudBlockItem, 0, cloudBlockModel);
	}
	
	
	
	
	
}
