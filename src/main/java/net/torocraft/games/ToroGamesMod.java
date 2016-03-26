package net.torocraft.games;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.torocraft.games.checkerboard.CheckboardCreateCommand;
import net.torocraft.games.chess.ChessCreateCommand;
import net.torocraft.games.chess.pieces.Rook;

@Mod(modid = ToroGamesMod.MODID, name = ToroGamesMod.MODNAME, version = ToroGamesMod.VERSION)
public class ToroGamesMod {

	public static final String MODID = "torogamesmod";
	public static final String MODNAME = "ToroGames Mod";
	public static final String VERSION = "0.0.0";

	@Mod.Instance(MODID)
	public static ToroGamesMod instance;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		EntityRegistry.registerModEntity(Rook.class, "Rook", 100, ToroGamesMod.instance, 20, 2, true);
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		ICommandManager comManager = event.getServer().getCommandManager();
		((ServerCommandManager) comManager).registerCommand(new CheckboardCreateCommand());
		((ServerCommandManager) comManager).registerCommand(new ChessCreateCommand());
	}

}
