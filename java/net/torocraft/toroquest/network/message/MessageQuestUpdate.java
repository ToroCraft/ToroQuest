package net.torocraft.toroquest.network.message;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
import net.torocraft.toroquest.network.ToroQuestPacketHandler;

public class MessageQuestUpdate implements IMessage {

	public static enum Action {
		ACCEPT, REJECT, COMPLETE, DONATE
	}

	public Action action;

	@Override
	public void fromBytes(ByteBuf buf) {
		action = Action.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(action.ordinal());
	}
	
	public static class Worker {

		private final Action action;

		public Worker(Action action) {
			this.action = action;
		}

		void work(MessageQuestUpdate message, EntityPlayer player) {
			Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ);
			QuestData currentQuestData = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
			
			EntityVillageLord lord = VillageLordGuiHandler.getVillageLord(player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
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
			System.out.println("processing donate");
			ItemStack donation = inventory.getDonationItem();
			inventory.setDonationItem(ItemStack.EMPTY);

			List<ItemStack> returns = new ArrayList<ItemStack>();
			returns.add(donation);
			inventory.setReturnItems(returns);
		}



		protected void processAccept(EntityPlayer player, Province province, IVillageLordInventory inventory) {
			System.out.println("processing accept");

			List<ItemStack> inputItems = inventory.getGivenItems();
			List<ItemStack> outputItems = PlayerCivilizationCapabilityImpl.get(player).acceptQuest(inputItems);

			if(outputItems == null) {
				inventory.setGivenItems(inputItems);
				return;
			}

			// TODO drop items already in the output slots
			inventory.setReturnItems(outputItems);

			QuestData currentQuest = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, currentQuest, null), (EntityPlayerMP) player);
		}

		protected void processReject(EntityPlayer player, Province province, IVillageLordInventory inventory) {
			System.out.println("processing reject");

			List<ItemStack> inputItems = inventory.getGivenItems();
			List<ItemStack> outputItems = PlayerCivilizationCapabilityImpl.get(player).rejectQuest(inputItems);

			if (outputItems == null) {
				inventory.setGivenItems(inputItems);
				return;
			}

			// TODO drop items already in the output slots
			inventory.setReturnItems(outputItems);

			QuestData nextQuest = PlayerCivilizationCapabilityImpl.get(player).getNextQuestFor(province);
			ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, null, nextQuest), (EntityPlayerMP) player);
		}

		protected void processComplete(EntityPlayer player, Province province, IVillageLordInventory inventory) {
			System.out.println("processing complete");

			List<ItemStack> inputItems = inventory.getGivenItems();
			List<ItemStack> outputItems = PlayerCivilizationCapabilityImpl.get(player).completeQuest(inputItems);

			if (outputItems == null) {
				inventory.setGivenItems(inputItems);
				return;
			}

			// TODO drop items already in the output slots
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

			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;

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

	private boolean isSet(String s) {
		return s != null && s.trim().length() > 0;
	}

	private boolean isEmpty(String s) {
		return !isSet(s);
	}

}
