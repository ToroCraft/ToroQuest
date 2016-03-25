package net.torocraft.baitmod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod (modid = BaitMod.MODID, name = BaitMod.MODNAME, version = BaitMod.VERSION)
public class BaitMod {
	
	public static final String MODID = "baitmod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "BaitMod";
	
	
	public static Block sugarBlock;
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		sugarBlock = new SugarBlock();
		
		GameRegistry.registerBlock(sugarBlock, SugarBlock.NAME);
		GameRegistry.registerTileEntity(SugarBlockTileEntity.class, "sugar_tile_entity");
		
		
		GameRegistry.addRecipe(new ItemStack(sugarBlock), "sss", "sss", "sss", 's', new ItemStack(Items.sugar));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.sugar, 9), sugarBlock);
		
		
		//1
		Item sugarBlockItem = GameRegistry.findItem(MODID, SugarBlock.NAME);
		
		
		
		
		//2
		ModelResourceLocation sugarBlockModel = new ModelResourceLocation("baitmod:sugarBlock", "inventory");
		//3
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(sugarBlockItem, 0, sugarBlockModel);
	}
	
	
	
	
	
}
