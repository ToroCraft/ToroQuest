package net.torocraft.toroquest.network.message;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.gui.VillageLordGuiContainer;

public class MessageSetQuestInfo implements IMessage {

	private QuestMessage questMessage;
	private String questMessageJson;
	
	public MessageSetQuestInfo() {

	}

	public MessageSetQuestInfo(Province civ, QuestData current, QuestData next) {
		questMessage = new QuestMessage();
		questMessage.civ = civ;
		questMessage.current = current;
		questMessage.next = next;
		serializeData();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		questMessageJson = ByteBufUtils.readUTF8String(buf);
		deserializeData();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, questMessageJson);
	}
	
	private void serializeData() {
		// FIXME look at the other fixme
		if(questMessage != null) {
			questMessageJson = new Gson().toJson(questMessage, QuestMessage.class);
		} else {
			questMessageJson = "";
		}
	}
	
	private void deserializeData() {
		// FIXME will crash on NBT tags which I just added and broke your stuff
		// (your welcome, hehehehe), you might want to manually
		// serialize each field in QuestMessage, it would be more efficient that
		// way too. you
		// can use ByteBufUtils.writeTag(buff, data.getCustom());

		questMessage = new Gson().fromJson(questMessageJson, QuestMessage.class);
	}
	
	public static class Worker {
		public void work(MessageSetQuestInfo message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			final EntityPlayer player = minecraft.player;

			if (player == null) {
				return;
			}
			
			VillageLordGuiContainer.setProvince(message.questMessage.civ);
			VillageLordGuiContainer.setCurrentQuest(message.questMessage.current);
			VillageLordGuiContainer.setNextQuest(message.questMessage.next);
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
	
	public static class QuestMessage {
		public Province civ;
		public QuestData current;
		public QuestData next;
	}
}
