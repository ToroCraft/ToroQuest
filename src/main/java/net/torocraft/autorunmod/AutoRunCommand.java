package net.torocraft.autorunmod;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class AutoRunCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "autorun";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.autorun.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		new AutoRun(sender.getEntityWorld(),Minecraft.getMinecraft().thePlayer).startRunning();
	}
	

}
