package net.torocraft.toroquest.network.message;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.CivilizationUtil;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.entities.EntityVillageLord;
import net.torocraft.toroquest.gui.VillageLordGuiHandler;
import net.torocraft.toroquest.inventory.IVillageLordInventory;
import net.torocraft.toroquest.item.ItemFireSword;
import net.torocraft.toroquest.item.ItemObsidianSword;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;

public class MessageQuestUpdate implements IMessage {

	public static enum Action {
		ACCEPT, REJECT, COMPLETE, DONATE
	}

	public Action action;
	public int lordEntityId;

	@Override
	public void fromBytes(ByteBuf buf) {
		action = Action.values()[buf.readInt()];
		lordEntityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(action.ordinal());
		buf.writeInt(lordEntityId);
	}

	public static class Worker {

		private final Action action;

		public Worker(Action action) {
			this.action = action;
		}

		void work(MessageQuestUpdate message, EntityPlayer player) {
			Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ);

			EntityVillageLord lord = (EntityVillageLord) player.world.getEntityByID(message.lordEntityId);
			IVillageLordInventory inventory = lord.getInventory(player.getUniqueID());

			switch (action) {
			case ACCEPT:
				processAccept(player, province, inventory);
				break;
			case COMPLETE:
				processComplete(player, province, inventory);
				break;

			case REJECT:
				processReject(player, province, inventory);
				break;

			case DONATE:
				processDonate(player, province, inventory);
				break;

			default:
				throw new IllegalArgumentException("invalid quest action [" + action + "]");
			}

		}

		private void processDonate(EntityPlayer player, Province province, IVillageLordInventory inventory) {
			ItemStack donation = inventory.getDonationItem();

			if (MessageSetItemReputationAmount.isNoteForLord(province, donation)) {
				writeReplyNote(inventory, donation);
				return;
			}

			if (MessageSetItemReputationAmount.isStolenItemForProvince(province, donation)) {
				handleReturnStolenItem(player, province, inventory, donation);
				return;
			}

			DonationReward reward = getRepForDonation(donation);
			if (reward != null) {
				PlayerCivilizationCapabilityImpl.get(player).adjustReputation(province.civilization, reward.rep);
				inventory.setDonationItem(ItemStack.EMPTY);
				inventory.setReturnItems(l(new ItemStack(reward.item)));
			}
		}

		private <T> List<T> l(T... items) {
			List<T> l = new ArrayList<T>();
			for (T item : items) {
				l.add(item);
			}
			return l;
		}

		private void handleReturnStolenItem(EntityPlayer player, Province province, IVillageLordInventory inventory, ItemStack stack) {
			inventory.setDonationItem(ItemStack.EMPTY);
			ItemStack emeralds = new ItemStack(Items.EMERALD, 2 + player.world.rand.nextInt(3));
			List<ItemStack> l = new ArrayList<ItemStack>(1);
			l.add(emeralds);
			inventory.setReturnItems(l);
			PlayerCivilizationCapabilityImpl.get(player).adjustReputation(province.civilization, 2 + player.world.rand.nextInt(3));
		}

		private void writeReplyNote(IVillageLordInventory inventory, ItemStack donation) {
			String sToProvinceId = donation.getTagCompound().getString("toProvince");
			String sQuestId = donation.getTagCompound().getString("questId");

			if (isEmpty(sToProvinceId) || isEmpty(sQuestId)) {
				return;
			}

			inventory.setDonationItem(ItemStack.EMPTY);
			donation.setStackDisplayName("Reply Note");
			donation.getTagCompound().setBoolean("reply", true);

			List<ItemStack> l = new ArrayList<ItemStack>(1);
			l.add(donation);
			inventory.setReturnItems(l);
		}

		protected void processAccept(EntityPlayer player, Province province, IVillageLordInventory inventory) {

			List<ItemStack> inputItems = inventory.getGivenItems();
			List<ItemStack> outputItems = PlayerCivilizationCapabilityImpl.get(player).acceptQuest(inputItems);

			if (outputItems == null) {
				inventory.setGivenItems(inputItems);
				return;
			}

			inventory.setReturnItems(outputItems);

			QuestData currentQuest = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, currentQuest, null), (EntityPlayerMP) player);
		}

		protected void processReject(EntityPlayer player, Province province, IVillageLordInventory inventory) {

			List<ItemStack> inputItems = inventory.getGivenItems();
			List<ItemStack> outputItems = PlayerCivilizationCapabilityImpl.get(player).rejectQuest(inputItems);

			if (outputItems == null) {
				inventory.setGivenItems(inputItems);
				return;
			}

			inventory.setReturnItems(outputItems);

			QuestData nextQuest = PlayerCivilizationCapabilityImpl.get(player).getNextQuestFor(province);
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, null, nextQuest), (EntityPlayerMP) player);
		}

		protected void processComplete(EntityPlayer player, Province province, IVillageLordInventory inventory) {

			List<ItemStack> inputItems = inventory.getGivenItems();
			List<ItemStack> outputItems = PlayerCivilizationCapabilityImpl.get(player).completeQuest(inputItems);

			if (outputItems == null) {
				inventory.setGivenItems(inputItems);
				return;
			}

			inventory.setReturnItems(outputItems);

			QuestData nextQuest = PlayerCivilizationCapabilityImpl.get(player).getNextQuestFor(province);
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, null, nextQuest), (EntityPlayerMP) player);
		}
	}

	public static class Handler implements IMessageHandler<MessageQuestUpdate, IMessage> {

		@Override
		public IMessage onMessage(final MessageQuestUpdate message, MessageContext ctx) {
			if (ctx.side != Side.SERVER) {
				return null;
			}

			final EntityPlayerMP player = ctx.getServerHandler().player;

			if (player == null) {
				return null;
			}

			final WorldServer worldServer = player.getServerWorld();

			worldServer.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					new Worker(message.action).work(message, player);
				}
			});

			return null;
		}
	}

	private static boolean isSet(String s) {
		return s != null && s.trim().length() > 0;
	}

	private static boolean isEmpty(String s) {
		return !isSet(s);
	}

	public static class DonationReward {
		public DonationReward() {

		}

		public DonationReward(int rep, Item item) {
			this.rep = rep;
			this.item = item;
		}

		public Item item;
		public int rep;
	}

	public static DonationReward getRepForDonation(ItemStack item) {

		if (item.isEmpty()) {
			return null;
		}

		if (item.getItem() instanceof ItemTool) {
			String material = ((ItemTool) item.getItem()).getToolMaterialName();
			switch (material) {
			case "DIAMOND":
				return new DonationReward(2, null);
			case "GOLD":
				return new DonationReward(1, null);
			default:
				return null;
			}
		}

		if (item.getItem() instanceof ItemSword) {
			String material = ((ItemSword) item.getItem()).getToolMaterialName();

			if (item.getItem() == ItemObsidianSword.INSTANCE || item.getItem() == ItemFireSword.INSTANCE) {
				return new DonationReward(3, null);
			}

			if ("DIAMOND".equals(material)) {
				return new DonationReward(2, null);
			} else if ("GOLD".equals(material)) {
				return new DonationReward(1, null);
			} else {
				return null;
			}

		}

		if (item.getItem() == Items.DIAMOND) {
			return new DonationReward(1 * item.getCount(), null);
		}

		if (item.getItem() == Items.EMERALD) {
			return new DonationReward(2 * item.getCount(), null);
		}

		if (item.getItem() instanceof ItemBlock) {
			Block block = ((ItemBlock) item.getItem()).getBlock();
			if (Blocks.DIAMOND_BLOCK == block) {
				return new DonationReward(9 * item.getCount(), null);
			}

			if (Blocks.EMERALD_BLOCK == block) {
				return new DonationReward(18 * item.getCount(), null);
			}
		}

		DonationReward reward = new DonationReward();

		if (is(item, Items.BREAD, Items.POTATO, Items.APPLE)) {

			reward.rep = 3 * MathHelper.floor(item.getCount() / 16);
		}

		if (is(item, Items.CARROT, Items.BEETROOT)) {
			reward.rep = 2 * MathHelper.floor(item.getCount() / 16);
		}

		if (is(item, Items.WHEAT)) {
			reward.rep = 1 * MathHelper.floor(item.getCount() / 16);
		}

		if (is(item, Items.CHICKEN, Items.COOKED_CHICKEN, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.MUTTON, Items.COOKED_MUTTON, Items.RABBIT,
				Items.COOKED_RABBIT, Items.FISH, Items.COOKED_FISH, Items.BEEF, Items.COOKED_BEEF)) {
			reward.rep = 4 * MathHelper.floor(item.getCount() / 16);
		}

		if (reward.rep == 0) {
			return null;
		}

		if (item.getCount() >= 64) {
			reward.item = Items.GOLD_NUGGET;
		}

		return reward;
	}

	private static boolean is(ItemStack stack, Item... items) {
		for (Item item : items) {
			if (stack.getItem() == item) {
				return true;
			}
		}
		return false;
	}

}
