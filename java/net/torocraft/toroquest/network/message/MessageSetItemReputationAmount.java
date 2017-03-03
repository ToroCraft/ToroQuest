package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.gui.VillageLordGuiContainer;
import net.torocraft.toroquest.inventory.IVillageLordInventory;
import net.torocraft.toroquest.network.message.MessageQuestUpdate.DonationReward;

public class MessageSetItemReputationAmount implements IMessage {

	public static enum MessageCode {
		EMPTY, NOTE, STOLEN_ITEM, DONATION
	};

	public int reputation = 0;
	public MessageCode messageCode = MessageCode.EMPTY;

	public MessageSetItemReputationAmount() {

	}

	public MessageSetItemReputationAmount(IVillageLordInventory inventory) {
		ItemStack item = inventory.getDonationItem();
		if (item == null || item.stackSize < 1) {
			reputation = 0;
			messageCode = MessageCode.EMPTY;
			return;
		}

		if (isNoteForLord(inventory.getProvince(), item)) {
			reputation = 0;
			messageCode = MessageCode.NOTE;
			return;
		}

		if (isStolenItemForProvince(inventory.getProvince(), item)) {
			reputation = 0;
			messageCode = MessageCode.STOLEN_ITEM;
			return;
		}

		DonationReward reward = MessageQuestUpdate.getRepForDonation(item);

		if (reward != null) {
			reputation = reward.rep;
			messageCode = MessageCode.DONATION;
			return;
		}

		reputation = 0;
		messageCode = MessageCode.EMPTY;
	}

	public static boolean isStolenItemForProvince(Province inProvince, ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return false;
		}

		if (inProvince == null) {
			return false;

		}

		String sToProvinceId = stack.getTagCompound().getString("provinceId");
		Boolean isStolen = stack.getTagCompound().getBoolean("isStolen");

		if (isEmpty(sToProvinceId)) {
			return false;
		}

		if (!isStolen) {
			return false;
		}

		if (inProvince.id.toString().equals(sToProvinceId)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNoteForLord(Province inProvince, ItemStack stack) {

		if (stack.getItem() != Items.PAPER || !stack.hasTagCompound()) {
			return false;
		}

		if (inProvince == null) {
			return false;
		}

		String sToProvinceId = stack.getTagCompound().getString("toProvince");
		String sQuestId = stack.getTagCompound().getString("questId");
		Boolean isReply = stack.getTagCompound().getBoolean("reply");

		if (isEmpty(sToProvinceId) || isEmpty(sQuestId) || isReply) {
			return false;
		}

		return inProvince.id.toString().equals(sToProvinceId);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		reputation = buf.readInt();
		messageCode = e(buf.readInt());
	}

	private MessageCode e(int i) {
		try {
			return MessageCode.values()[i];
		} catch (Exception e) {
			return MessageCode.EMPTY;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(reputation);
		buf.writeInt(messageCode.ordinal());
	}

	public static class Worker {
		public void work(MessageSetItemReputationAmount message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			final EntityPlayer player = minecraft.thePlayer;

			if (player == null) {
				return;
			}

			VillageLordGuiContainer.setDonateInfo(message);
		}
	}

	public static class Handler implements IMessageHandler<MessageSetItemReputationAmount, IMessage> {

		@Override
		public IMessage onMessage(final MessageSetItemReputationAmount message, MessageContext ctx) {
			if (ctx.side != Side.CLIENT) {
				return null;
			}

			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					new Worker().work(message);
				}
			});

			return null;
		}
	}

	public static boolean isSet(String s) {
		return s != null && s.trim().length() > 0;
	}

	public static boolean isEmpty(String s) {
		return !isSet(s);
	}
}
