package net.torocraft.toroquest;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.torocraft.toroquest.util.generation.ToroGenCommand;

@Mod (modid = ToroQuestMod.MODID, name = ToroQuestMod.MODNAME, version = ToroQuestMod.VERSION)
public class ToroQuestMod {

	
	public static final String MODID = "toroquest";
	public static final String VERSION = "1.10.2-1";
	public static final String MODNAME = "ToroQuest";
	
	@SidedProxy(clientSide = "net.torocraft.toroquest.ClientProxy", serverSide = "net.torocraft.toroquest.ServerProxy")
	public static CommonProxy proxy;
	
	@Instance(value = ToroQuestMod.MODID)
	public static ToroQuestMod instance;
	
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
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent e) {
		e.registerServerCommand(new ToroGenCommand());
	}
	
	
}
