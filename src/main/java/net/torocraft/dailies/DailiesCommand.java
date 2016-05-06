package net.torocraft.dailies;

import java.util.ArrayList;
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
import net.torocraft.dailies.quests.IDailyQuest;

public class DailiesCommand implements ICommand {

	private List<String> aliases;
	private List<DailyQuest> dailies;
	private EntityPlayer player;

	public DailiesCommand() {
		buildAliases();
	}
	
	private void buildAliases() {
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
		return "dailies <command> <id>";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(!(sender instanceof EntityPlayer)) {
			return;
		}
		

		player = (EntityPlayer)sender;
		dailies = null;
		
		DailiesWorldData worldData = DailiesWorldData.get(server.getEntityWorld());
		dailies = worldData.getDailies();
		
		if (dailies == null) {
			System.out.println("No dailies found, lame!");
		} else {
			System.out.println("Dailies found COUNT[" + dailies.size() + "]");
		}
		
		if (dailies == null) {
			sender.addChatMessage(new TextComponentString("No Dailies Found"));
		}
		

		if(args.length == 0) {
			String dailiesList = buildDailiesListText(dailies, player);
			sender.addChatMessage(new TextComponentString(dailiesList));
		} else if(args.length == 2) {
			String command = args[0];
			int index = toIndex(args[1]);

			DailyQuest quest = dailies.get(index);
			
			if(!validCommand(command)) {
				sender.addChatMessage(new TextComponentString("Invalid Command"));
				return;
			}
			
			if (quest == null) {
				sender.addChatMessage(new TextComponentString("Invalid Quest"));
				return;
			}
			
			if (command.equalsIgnoreCase("abandon")) {
				abandonQuest(quest);
				sender.addChatMessage(new TextComponentString("Quest " + index + " abandoned"));
			} else if (command.equalsIgnoreCase("accept")) {
				acceptQuest(quest);
				sender.addChatMessage(new TextComponentString("Quest " + index + " accepted"));
			}
			
		} else {
			sender.addChatMessage(new TextComponentString("Invalid Command"));
		}

		
	}
	
	private int toIndex(String string) {
		try {
			return Integer.valueOf(string) - 1;
		} catch (Exception e) {
			return 0;
		}
	}

	private boolean validCommand(String command) {
		if(command.equals("abandon") || command.equals("accept")) {
			return true;
		}
		return false;
	}

	private String buildDailiesListText(List<DailyQuest> dailies, EntityPlayer player) {
		if (dailies == null || dailies.size() < 1) {
			return "No dailies found";
		}

		IDailiesCapability playerDailiesCapability = player.getCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null);
		Set<IDailyQuest> playerDailies = playerDailiesCapability.getAcceptedQuests();

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < dailies.size(); i++) {

			if (playerDailies.contains(dailies.get(i))) {
				continue;
			}

			builder.append("(").append(i + 1).append(") NEW ");
			builder.append(dailies.get(i).getDisplayName());
			builder.append("\n");
		}
		


		for (IDailyQuest quest : playerDailies) {
			builder.append("ACCEPTED :: ");
			builder.append(quest.getStatusMessage());
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	private void abandonQuest(DailyQuest quest) {
		IDailiesCapability dailies = player.getCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null);
		dailies.abandonQuest(quest);
	}
	
	private void acceptQuest(DailyQuest quest) {
		IDailiesCapability dailies = player.getCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null);
		dailies.acceptQuest(quest);
	}
	

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		List<String> tabOptions = new ArrayList<String>();
		
		if(args.length == 0) {
			tabOptions.add("dailies");
		} else if(args.length == 1) {
			String command = args[0];
			
			if(command.length() > 2) {
				tabOptions.add(getTabbedCommand(command));
			}
		}
		
		return tabOptions;
	}
	
	private String getTabbedCommand(String command) {
		if(command.startsWith("ac")) {
			return "accept";
		} else if(command.startsWith("ab"))
			return "abandon";
		
		return "";
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
