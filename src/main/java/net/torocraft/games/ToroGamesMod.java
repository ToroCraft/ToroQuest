package net.torocraft.games;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.torocraft.games.checkerboard.CheckboardCreateCommand;

@Mod(modid = ToroGamesMod.MODID, name = ToroGamesMod.MODNAME, version = ToroGamesMod.VERSION)
public class ToroGamesMod {

	public static final String MODID = "torogamesmod";
	public static final String MODNAME = "ToroGames Mod";
	public static final String VERSION = "0.0.0";

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		ICommandManager comManager = event.getServer().getCommandManager();
		((ServerCommandManager)comManager).registerCommand(new CheckboardCreateCommand());
	}

}
