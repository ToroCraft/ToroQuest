package net.torocraft.toroquest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.QuestDelegator;
import net.torocraft.toroquest.generation.BastionsLairGenerator;
import net.torocraft.toroquest.generation.MageTowerGenerator;
import net.torocraft.toroquest.generation.MonolithGenerator;
import net.torocraft.toroquest.generation.ThroneRoomGenerator;
import net.torocraft.toroquest.gui.VillageLordGuiHandler;

public class ToroQuestCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "tq";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.torogen.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if (!(sender instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) sender;

		if (args.length < 1) {
			throw new WrongUsageException("commands.tq.usage", new Object[0]);
		}

		String command = args[0];

		System.out.println("command " + command);

		if ("rep".equals(command)) {
			adjustRep(player, args);
		} else if ("gen".equals(command)) {
			genCommand(player, args);
		} else if ("gui".equals(command)) {
			guiCommand(player, args);
		} else if ("quest".equals(command)) {
			questCommand(player, args);
		} else {
			throw new WrongUsageException("commands.tq.usage", new Object[0]);
		}
	}


	private void adjustRep(EntityPlayer player, String[] args) throws CommandException {
		int amount;
		if (args.length < 2) {
			throw new WrongUsageException("commands.tq.usage", new Object[0]);
		} else {
			try {
				amount = Integer.parseInt(args[1], 10);
			} catch (Exception e) {
				throw new WrongUsageException("commands.tq.usage", new Object[0], e);
			}
		}
		Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ);
		if (province == null || province.civilization == null) {
			throw new WrongUsageException("commands.tq.not_in_civ", new Object[0]);
		}
		PlayerCivilizationCapabilityImpl.get(player).setPlayerReputation(province.civilization, amount);
	}

	private void genCommand(EntityPlayer player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commands.tq.usage", new Object[0]);
		}
		String structure = args[1];

		if ("throne_room".equals(structure)) {
			new ThroneRoomGenerator().generate(player.getEntityWorld(), player.getEntityWorld().rand, player.getPosition());
			return;
		}

		if ("mage_tower".equals(structure)) {
			new MageTowerGenerator().generate(player.getEntityWorld(), player.getEntityWorld().rand, player.getPosition());
			return;
		}

		if ("bastions_lair".equals(structure)) {
			new BastionsLairGenerator().generate(player.getEntityWorld(), player.getEntityWorld().rand, player.getPosition());
			return;
		}

		if ("monolith".equals(structure)) {
			new MonolithGenerator().generate(player.getEntityWorld(), player.getEntityWorld().rand, player.getPosition());
			return;
		}

		throw new WrongUsageException("commands.tq.usage", new Object[0]);
	}
	
	private void guiCommand(EntityPlayer player, String[] args) throws CommandException {
		if(args.length < 2) {
			throw new WrongUsageException("commans.tq.usage", new Object[0]);
		}
		
		String type = args[1];
		
		if("lord".equals(type)) {
			player.openGui(ToroQuest.INSTANCE, VillageLordGuiHandler.getGuiID(), player.worldObj, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			return;
		}
		
		throw new WrongUsageException("commands.tq.usage", new Object[0]);
	}

	private void questCommand(EntityPlayer player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commans.tq.usage", new Object[0]);
		}

		String sub = args[1];

		if ("list".equals(sub)) {
			Set<QuestData> quests = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuests();
			QuestDelegator quest = new QuestDelegator(new QuestData());
			for (QuestData data : quests) {
				quest.setData(data);
				player.addChatMessage(new TextComponentString("----"));
				player.addChatMessage(new TextComponentString(quest.getTitle()));
				player.addChatMessage(new TextComponentString(quest.getDescription()));
			}

			if (quests.size() < 1) {
				player.addChatMessage(new TextComponentString("No accepted quests"));
			}

			return;
		}

		Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ);
		if (province == null || province.civilization == null) {
			throw new WrongUsageException("commands.tq.not_in_civ", new Object[0]);
		}

		if ("next".equals(sub)) {
			QuestDelegator quest = new QuestDelegator(PlayerCivilizationCapabilityImpl.get(player).getNextQuestFor(province));
			if (quest.getData() == null) {
				throw new NullPointerException("next quest should never be null");
			}
			player.addChatMessage(new TextComponentString(quest.getTitle()));

		} else if ("current".equals(sub)) {
			QuestData data = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
			if (data == null) {
				player.addChatMessage(new TextComponentString("No quest has been accepted"));
			} else {
				QuestDelegator quest = new QuestDelegator(data);
				player.addChatMessage(new TextComponentString(quest.getTitle()));
				player.addChatMessage(new TextComponentString(quest.getDescription()));
			}
		} else if ("accept".equals(sub)) {
			QuestData data = PlayerCivilizationCapabilityImpl.get(player).acceptQuest();
			if (data == null) {
				throw new WrongUsageException("commands.tq.quest_could_not_be_accepted", new Object[0]);
			} else {
				QuestDelegator quest = new QuestDelegator(data);
				player.addChatMessage(new TextComponentString("Accepted: " + quest.getTitle()));
				player.addChatMessage(new TextComponentString(quest.getDescription()));
			}
		} else if ("reject".equals(sub)) {
			QuestData data = PlayerCivilizationCapabilityImpl.get(player).rejectQuest();
			if (data == null) {
				throw new WrongUsageException("commands.tq.quest_could_not_be_rejected", new Object[0]);
			} else {
				QuestDelegator quest = new QuestDelegator(data);
				player.addChatMessage(new TextComponentString("Rejected: " + quest.getTitle()));
			}
		} else {
			throw new WrongUsageException("commands.tq.usage", new Object[0]);
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		List<String> tabOptions = new ArrayList<String>();

		if (args.length == 0) {
			tabOptions.add("tq");
		} else if (args.length == 1) {
			String command = args[0];

			if (command == null || command.trim().length() == 0) {
				tabOptions.add("rep");
				tabOptions.add("gen");
				tabOptions.add("gui");
				tabOptions.add("quest");
			} else {
				if (command.startsWith("r")) {
					tabOptions.add("rep");
				} else if (command.startsWith("g")) {
					tabOptions.add("gen");
					tabOptions.add("gui");
				}
			}
		} else if (args.length == 2) {
			if ("gen".equals(args[0])) {
				tabOptions.add("throne_room");
				tabOptions.add("mage_tower");
				tabOptions.add("bastions_lair");
				tabOptions.add("monolith");
			} else if ("gui".equals(args[0])) {
				tabOptions.add("lord");
			} else if ("quest".equals(args[0])) {
				tabOptions.add("current");
				tabOptions.add("list");
				tabOptions.add("next");
				tabOptions.add("accept");
				tabOptions.add("reject");
			}
		}
		return tabOptions;
	}

}
