package net.torocraft.cloudmod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod (modid = CloudMod.MODID, name = CloudMod.MODNAME, version = CloudMod.VERSION)
public class CloudMod {
	
	public static final String MODID = "cloudmod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "CloudMod";
	
	
	public static Block cloudBlock;
	public static Item cloudBlockItem;
	
	@EventHandler
	public static void init(FMLInitializationEvent e) {
		cloudBlock = new CloudBlock();
		
		GameRegistry.registerBlock(cloudBlock, CloudBlock.NAME);
		
		cloudBlockItem = GameRegistry.findItem(MODID, CloudBlock.NAME);
		
		if(e.getSide() == Side.CLIENT){
			registerRenders();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		ModelResourceLocation cloudBlockModel = new ModelResourceLocation("cloudmod:cloudBlock", "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(cloudBlockItem, 0, cloudBlockModel);
	}
	
	
	
	
	
}
