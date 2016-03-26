package net.torocraft.bouncermod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod (modid = BouncerMod.MODID, name = BouncerMod.MODNAME, version = BouncerMod.VERSION)
public class BouncerMod {
	
	public static final String MODID = "bouncermod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "BouncerMod";
	
	
	public static Block bounceBlock;
	public static Block flubberBlock;
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		bounceBlock = new BounceBlock();
		flubberBlock = new FlubberBlock();
		
		GameRegistry.registerBlock(bounceBlock, BounceBlock.NAME);
		GameRegistry.registerTileEntity(BounceBlockTileEntity.class, "bounce_tile_entity");
		GameRegistry.registerBlock(flubberBlock, FlubberBlock.NAME);
		GameRegistry.registerTileEntity(BounceBlockTileEntity.class, "flubber_tile_entity");
				
		
		Item bounceBlockItem = GameRegistry.findItem(MODID, BounceBlock.NAME);
		Item flubberBlockItem = GameRegistry.findItem(MODID, FlubberBlock.NAME);
		
		ModelResourceLocation bounceBlockModel = new ModelResourceLocation("bouncermod:bounceBlock", "inventory");
		ModelResourceLocation flubberBlockModel = new ModelResourceLocation("bouncermod:flubberBlock", "inventory");

		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(bounceBlockItem, 0, bounceBlockModel);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(flubberBlockItem, 0, flubberBlockModel);
	}
	
	
	
	
	
}
