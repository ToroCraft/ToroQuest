package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.quests.util.QuestData;

public class MessageSetQuestInfo implements IMessage {

	private boolean accepted;
	private String title;
	private String description;
	
	public MessageSetQuestInfo() {

	}

	public MessageSetQuestInfo(QuestData current, QuestData next) {
		// TODO map from questData here?
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO
		ByteBufUtils.writeUTF8String(buf, "");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO
	}
	
	public static class Worker {
		public void work(MessageSetQuestInfo message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			final EntityPlayer player = minecraft.thePlayer;

			if (player == null) {
				return;
			}
			
			// FIXME
			// VillageLordGuiContainer.setAvailableReputation(message.reputation);
		}
	}

	public static class Handler implements IMessageHandler<MessageSetQuestInfo, IMessage> {

		@Override
		public IMessage onMessage(final MessageSetQuestInfo message, MessageContext ctx) {
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
}
