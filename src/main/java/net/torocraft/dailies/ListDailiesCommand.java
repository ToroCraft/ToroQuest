package net.torocraft.dailies;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.torocraft.dailies.quests.DailyQuest;

public class ListDailiesCommand implements ICommand {

	private List<String> aliases;

	public ListDailiesCommand() {
		this.aliases = new ArrayList<String>();
		this.aliases.add("dailies");
		this.aliases.add("listdailies");
	}

	@Override
	public int compareTo(ICommand arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "dailies";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Displays list of the daily quests";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		DailiesWorldData worldData = DailiesWorldData.get(server.getEntityWorld());
		List<DailyQuest> dailies = worldData.getDailies();

		if (dailies == null) {
			System.out.println("No dailies found, lame!");
		} else {
			System.out.println("Dailies found COUNT[" + dailies.size() + "]");
		}

		if (dailies != null) {
			String dailiesList = buildDailiesListText(dailies);
			sender.addChatMessage(new TextComponentString(dailiesList));
		}
	}

	private String buildDailiesListText(List<DailyQuest> dailies) {
		StringBuilder builder = new StringBuilder();
		for (DailyQuest quest : dailies) {
			builder.append(quest.getName() + ": " + quest.getType());
			builder.append("\n");
		}

		return builder.toString();
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
