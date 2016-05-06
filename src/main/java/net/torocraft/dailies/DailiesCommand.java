package net.torocraft.dailies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.torocraft.dailies.capabilities.CapabilityDailiesHandler;
import net.torocraft.dailies.capabilities.IDailiesCapability;
import net.torocraft.dailies.quests.DailyQuest;

public class DailiesCommand implements ICommand {

	private List<String> aliases = new ArrayList<String>();

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "dailies";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "dailies <command> <id>";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	public static class PlayerDailyQuests {
		public EntityPlayer player = null;
		public IDailiesCapability playerDailiesCapability;
		public List<DailyQuest> openDailyQuests = null;
		public List<DailyQuest> acceptedDailyQuests = null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		PlayerDailyQuests questsData = setupQuestsData(server, sender);

		if (args.length == 0) {
			listDailyQuests(questsData);
		} else if (args.length == 2) {
			handleSubCommand(questsData, args);
		} else {
			sender.addChatMessage(new TextComponentString("Invalid Command"));
		}
	}

	private void handleSubCommand(PlayerDailyQuests d, String[] args) {
		String command = args[0];
		int index = toIndex(args[1]);

		if (!validCommand(command)) {
			d.player.addChatMessage(new TextComponentString("Invalid Command"));
			return;
		}

		if (command.equalsIgnoreCase("abandon")) {
			DailyQuest quest = d.acceptedDailyQuests.get(index);
			if (quest == null) {
				d.player.addChatMessage(new TextComponentString("quest not found"));
			} else {
				d.playerDailiesCapability.abandonQuest(quest);
				d.player.addChatMessage(new TextComponentString("Quest " + index + " abandoned"));
			}

		} else if (command.equalsIgnoreCase("accept")) {
			DailyQuest quest = d.openDailyQuests.get(index);
			if (quest == null) {
				d.player.addChatMessage(new TextComponentString("quest not found"));
			} else {
				d.playerDailiesCapability.acceptQuest(quest);
				d.player.addChatMessage(new TextComponentString("Quest " + index + " accepted"));
			}
		}
	}

	private PlayerDailyQuests setupQuestsData(MinecraftServer server, ICommandSender sender) {

		if (!(sender instanceof EntityPlayer)) {
			return null;
		}

		PlayerDailyQuests d = new PlayerDailyQuests();

		d.player = (EntityPlayer) sender;
		d.playerDailiesCapability = d.player.getCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null);
		Set<DailyQuest> completedQuestSet = d.playerDailiesCapability.getCompletedQuests();
		Set<DailyQuest> acceptedQuestSet = d.playerDailiesCapability.getAcceptedQuests();
		Set<DailyQuest> serversDailyQuests = getDailyQuests(server);

		if (completedQuestSet == null) {
			completedQuestSet = new HashSet<DailyQuest>();
		}

		if (acceptedQuestSet == null) {
			acceptedQuestSet = new HashSet<DailyQuest>();
		}

		d.openDailyQuests = new ArrayList<DailyQuest>();
		d.acceptedDailyQuests = new ArrayList<DailyQuest>();

		for (DailyQuest quest : serversDailyQuests) {
			if (!acceptedQuestSet.contains(quest) && !completedQuestSet.contains(quest)) {
				d.openDailyQuests.add(quest);
			}
		}

		for (DailyQuest quest : acceptedQuestSet) {
			d.acceptedDailyQuests.add(quest);
		}

		return d;
	}

	private void listDailyQuests(PlayerDailyQuests questsData) {
		String dailiesList = buildDailiesListText(questsData);
		questsData.player.addChatMessage(new TextComponentString(dailiesList));
	}

	private Set<DailyQuest> getDailyQuests(MinecraftServer server) {
		Set<DailyQuest> dailyQuests;
		DailiesWorldData worldData = DailiesWorldData.get(server.getEntityWorld());
		dailyQuests = worldData.getDailyQuests();
		return dailyQuests;
	}

	private int toIndex(String string) {
		try {
			return Integer.valueOf(string) - 1;
		} catch (Exception e) {
			return 0;
		}
	}

	private boolean validCommand(String command) {
		if (command.equals("abandon") || command.equals("accept")) {
			return true;
		}
		return false;
	}

	private String buildDailiesListText(PlayerDailyQuests d) {

		StringBuilder builder = new StringBuilder();

		if (d.openDailyQuests.size() < 1) {
			builder.append("No new daily quests found.\n");
		} else {
			for (int i = 0; i < d.openDailyQuests.size(); i++) {
				builder.append("(").append(i + 1).append(") OPEN :: ");
				builder.append(d.openDailyQuests.get(i).getDisplayName());
				builder.append("\n");
			}
		}

		builder.append("\n");

		if (d.openDailyQuests.size() < 1) {
			builder.append("You have no accepted quests.\n");
		} else {
			for (int i = 0; i < d.acceptedDailyQuests.size(); i++) {
				builder.append("(").append(i + 1).append(") ACCEPTED :: ");
				builder.append(d.acceptedDailyQuests.get(i).getDisplayName());
				builder.append("\n");
			}
		}

		return builder.toString();
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		List<String> tabOptions = new ArrayList<String>();

		if (args.length == 0) {
			tabOptions.add("dailies");
		} else if (args.length == 1) {
			String command = args[0];

			if (command.length() > 2) {
				tabOptions.add(getTabbedCommand(command));
			}
		}

		return tabOptions;
	}

	private String getTabbedCommand(String command) {
		if (command.startsWith("ac")) {
			return "accept";
		} else if (command.startsWith("ab"))
			return "abandon";

		return "";
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
