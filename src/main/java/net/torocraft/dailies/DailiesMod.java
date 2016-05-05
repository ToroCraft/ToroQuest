package net.torocraft.dailies;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = DailiesMod.MODID, name = DailiesMod.MODNAME, version = DailiesMod.VERSION)
public class DailiesMod {

	public static final String MODID = "dailies";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "DailiesMod";

	// public static Achievement achievementDailyQuestComplete = new
	// Achievement("achievement.dailyquestcompleted", "dailyquestcompleted", 0,
	// 0, Items.diamond_sword, (Achievement) null);

	@SidedProxy(clientSide = "net.torocraft.dailies.ClientProxy", serverSide = "net.torocraft.dailies.ServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void serverLoad(FMLServerStartingEvent e) {
		e.registerServerCommand(new DailiesCommand());
	}
	
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
		/*
		 * DailiesRequester requester = new DailiesRequester(); List<DailyQuest>
		 * dailies = requester.getDailies();
		 * 
		 * if (dailies == null) {
		 * System.out.println("******************* No dailies found, lame!"); }
		 * else {
		 * System.out.println("********************** Dailies found COUNT[" +
		 * dailies.size() + "]"); }
		 * 
		 * World world =
		 * FMLCommonHandler.instance().getMinecraftServerInstance().
		 * getEntityWorld(); if (dailies != null) { DailiesWorldData worldData =
		 * DailiesWorldData.get(world); worldData.setDailies(dailies); }
		 */
	}

}
