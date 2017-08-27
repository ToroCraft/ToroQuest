package net.torocraft.toroquest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.QuestDelegator;
import net.torocraft.toroquest.generation.BastionsLairGenerator;
import net.torocraft.toroquest.generation.GraveyardGenerator;
import net.torocraft.toroquest.generation.MageTowerGenerator;
import net.torocraft.toroquest.generation.MonolithGenerator;
import net.torocraft.toroquest.generation.ThroneRoomGenerator;
import net.torocraft.toroquest.gui.VillageLordGuiHandler;
import net.torocraft.toroquest.util.BookCreator;
import net.torocraft.toroquest.util.BookCreator.BookTypes;


public class ToroQuestCommand extends CommandBase {

	@Override
	public String getName() {
		return "tq";
	}

	@Override
	public String getUsage(ICommandSender sender) {
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
			adjustRep(server, player, args);
		} else if ("list".equals(command)) {
			listCommand(player, args);
		} else if ("gen".equals(command)) {
			genCommand(player, args);
		} else if ("gui".equals(command)) {
			guiCommand(player, args);
		} else if ("quest".equals(command)) {
			questCommand(player, args);
		} else if ("book".equals(command)) {
			bookCommand(player, args);
		} else {
			throw new WrongUsageException("commands.tq.usage", new Object[0]);
		}
	}

	private void bookCommand(EntityPlayer player, String[] args) throws CommandException {
		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(BookCreator.createBook(BookTypes.CIV_LORE, "pantheon_unbiased_book_1"));
		dropItems(player, items);
	}

	private void listCommand(EntityPlayer player, String[] args) throws CommandException {
		List<Province> provinces = CivilizationsWorldSaveData.get(player.world).getProvinces();
		StringBuilder sb = new StringBuilder();
		for (Province province : provinces) {
			sb.append(province.toString()).append("\n");
		}
		player.sendMessage(new TextComponentString(sb.toString()));
	}

	private void adjustRep(MinecraftServer server, EntityPlayer player, String[] args) throws CommandException {
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

		Entity entity = null;
		EntityPlayer playerToSet = player;

		if (args.length >= 3) {
			entity = getEntity(server, player, args[2]);
		}

		if (entity != null && entity instanceof EntityPlayer) {
			playerToSet = (EntityPlayer) entity;
		}

		CivilizationType civ;
		if (args.length >= 4) {
			civ = CivilizationType.valueOf(args[3]);
		} else {
			Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ);
			if (province == null || province.civilization == null) {
				throw new WrongUsageException("commands.tq.not_in_civ", new Object[0]);
			}
			civ = province.civilization;
		}

		PlayerCivilizationCapabilityImpl.get(playerToSet).setReputation(civ, amount);
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

		if ("graveyard".equals(structure)) {
			new GraveyardGenerator().generate(player.getEntityWorld(), player.getEntityWorld().rand, player.getPosition());
			return;
		}

		throw new WrongUsageException("commands.tq.usage", new Object[0]);
	}

	private void guiCommand(EntityPlayer player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commans.tq.usage", new Object[0]);
		}

		String type = args[1];

		if ("lord".equals(type)) {
			player.openGui(ToroQuest.INSTANCE, VillageLordGuiHandler.getGuiID(), player.world, player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ());
			return;
		}

		throw new WrongUsageException("commands.tq.usage", new Object[0]);
	}

	private void questCommand(EntityPlayer player, String[] args) throws CommandException {
		if (args.length < 2) {
			throw new WrongUsageException("commans.tq.usage", new Object[0]);
		}

		String sub = args[1];

		if ("test".equals(sub)) {
			List<ItemStack> items = pullHotbarItems(player);

			dropItems(player, items);

			return;
		}

		if ("list".equals(sub)) {
			Set<QuestData> quests = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuests();
			QuestDelegator quest = new QuestDelegator(new QuestData());
			for (QuestData data : quests) {
				quest.setData(data);
				player.sendMessage(new TextComponentString("----"));
				player.sendMessage(new TextComponentString(quest.getTitle()));
				player.sendMessage(new TextComponentString(quest.getDescription()));
			}

			if (quests.size() < 1) {
				player.sendMessage(new TextComponentString("No accepted quests"));
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
			player.sendMessage(new TextComponentString(quest.getTitle()));

		} else if ("current".equals(sub)) {
			QuestData data = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
			if (data == null) {
				player.sendMessage(new TextComponentString("No quest has been accepted"));
			} else {
				QuestDelegator quest = new QuestDelegator(data);
				player.sendMessage(new TextComponentString(quest.getTitle()));
				player.sendMessage(new TextComponentString(quest.getDescription()));
			}
		} else if ("accept".equals(sub)) {
			List<ItemStack> startItems = pullHotbarItems(player);
			List<ItemStack> returnItems = PlayerCivilizationCapabilityImpl.get(player).acceptQuest(startItems);

			if (returnItems == null) {
				player.sendMessage(new TextComponentString("Quest not Accepted"));
				dropItems(player, startItems);
			} else {
				QuestDelegator quest = new QuestDelegator(PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province));
				player.sendMessage(new TextComponentString("Accepted: " + quest.getTitle()));
				player.sendMessage(new TextComponentString(quest.getDescription()));
				dropItems(player, returnItems);
			}

		} else if ("complete".equals(sub)) {

			QuestData data = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);

			if (data == null) {
				player.sendMessage(new TextComponentString("No accepted quest to complete"));
				return;
			}

			QuestDelegator curQuest = new QuestDelegator(data);

			List<ItemStack> beforeItems = pullHotbarItems(player);
			List<ItemStack> afterItems = PlayerCivilizationCapabilityImpl.get(player).completeQuest(beforeItems);

			if (afterItems == null) {
				player.sendMessage(new TextComponentString("Quest not Completed"));
				dropItems(player, beforeItems);
			} else {
				player.sendMessage(new TextComponentString("Completed: " + curQuest.getTitle()));
				dropItems(player, afterItems);
			}

		} else if ("reject".equals(sub)) {

			QuestData data = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);

			if (data == null) {
				player.sendMessage(new TextComponentString("No accepted quest to complete"));
				return;
			}

			QuestDelegator curQuest = new QuestDelegator(data);

			List<ItemStack> beforeItems = pullHotbarItems(player);
			List<ItemStack> afterItems = PlayerCivilizationCapabilityImpl.get(player).rejectQuest(beforeItems);

			if (afterItems == null) {
				player.sendMessage(new TextComponentString("Unable to reject quest"));
				dropItems(player, beforeItems);
			} else {
				player.sendMessage(new TextComponentString("Rejected: " + curQuest.getTitle()));
				dropItems(player, afterItems);
			}
		} else {
			throw new WrongUsageException("commands.tq.usage", new Object[0]);
		}
	}

	private List<ItemStack> pullHotbarItems(EntityPlayer player) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		InventoryPlayer inv = player.inventory;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null && InventoryPlayer.isHotbar(i)) {
				ItemStack stack = inv.getStackInSlot(i);
				inv.setInventorySlotContents(i, ItemStack.EMPTY);
				items.add(stack);
			}
		}

		return items;
	}

	private void dropItems(EntityPlayer player, List<ItemStack> items) {
		for (ItemStack stack : items) {
			EntityItem dropItem = new EntityItem(player.world, player.posX, player.posY, player.posZ, stack);
			dropItem.setNoPickupDelay();
			player.world.spawnEntity(dropItem);
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {

		if (args.length == 0) {
			return l("tq");
		} else if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, "rep", "gen", "gui", "quest", "list", "book");

		} else if (args.length == 2) {
			if ("gen".equals(args[0])) {

				return getListOfStringsMatchingLastWord(args, "throne_room", "mage_tower", "bastions_lair", "monolith", "graveyard");

			} else if ("gui".equals(args[0])) {
				return getListOfStringsMatchingLastWord(args, "lord");

			} else if ("quest".equals(args[0])) {

				return getListOfStringsMatchingLastWord(args, "current", "complete", "list", "next", "accept", "reject");

			} else if ("rep".equals(args[0])) {
				return l("10");
			}
		} else if (args.length == 3) {
			if ("rep".equals(args[0])) {
				return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
			}
		} else if (args.length == 4) {
			if ("rep".equals(args[0])) {
				return getListOfStringsMatchingLastWord(args, Arrays.asList(CivilizationType.values()));
			}
		}

		return l();
	}

	private <T> List<T> l(T... items) {
		List<T> l = new ArrayList<T>();
		for (T item : items) {
			l.add(item);
		}
		return l;
	}

}
