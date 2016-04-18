package net.torocraft.autorunmod;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod (modid = AutoRunMod.MODID, name = AutoRunMod.MODNAME, version = AutoRunMod.VERSION)
public class AutoRunMod {
	
	public static final String MODID = "autorunmod";
	public static final String VERSION = "1.0";
	public static final String MODNAME = "AutoRunMod";
	
	
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		//ICommandManager comManager = event.getServer().getCommandManager();
		//((ServerCommandManager)comManager).registerCommand(new AutoRunCommand());
	}
	
	
	
	
	
}
