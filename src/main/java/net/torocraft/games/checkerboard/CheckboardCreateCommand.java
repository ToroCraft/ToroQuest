package net.torocraft.games.checkerboard;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CheckboardCreateCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "checkerboardcreate";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.checkerboardcreate.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		BlockPos pos = sender.getPosition();
		new CheckerBoard().generate(sender.getEntityWorld(), pos);
	}
	

}
