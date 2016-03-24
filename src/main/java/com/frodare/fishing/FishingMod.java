package com.frodare.fishing;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.frodare.fishing.events.EventHooks;
import com.frodare.fishing.items.Worm;

@Mod(modid=FishingMod.MODID, name=FishingMod.MODNAME, version=FishingMod.VERSION)
public class FishingMod {

	public static final String MODID = "fishing";
	public static final String MODNAME = "Fishing Mod";
	public static final String VERSION = "0.0.0";
	
	@Instance(value = FishingMod.MODID)
	public static FishingMod instance;
	
	public static Item worm;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		worm = new Worm();
		GameRegistry.registerItem(worm, "worm");
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		//Blocks.grass.onBlockDestroyedByPlayer(Forge, pos, state);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHooks());
	}
	
}
