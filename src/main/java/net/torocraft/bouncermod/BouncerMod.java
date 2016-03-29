package net.torocraft.bouncermod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod (modid = BouncerMod.MODID, name = BouncerMod.MODNAME, version = BouncerMod.VERSION)
public class BouncerMod {

	
	public static final String MODID = "bouncermod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "BouncerMod";
	
	@SidedProxy(clientSide="net.torocraft.bouncermod.ClientProxy", serverSide="net.torocraft.bouncermod.ServerProxy")
	public static CommonProxy proxy;
		
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	    proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
	    proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	    proxy.postInit(e);
	}
	
	
	
	
}
