package net.torocraft.toroquest.network.message;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.quests.util.QuestData;
import net.torocraft.toroquest.civilization.quests.util.QuestDelegator;
import net.torocraft.toroquest.gui.VillageLordGuiContainer;

public class MessageSetQuestInfo implements IMessage {

	private QuestMessage questMessage;
	private String questMessageJson;
	
	private Province civ;
	private QuestData currentQuest;
	private QuestData nextQuest;
	
	public MessageSetQuestInfo() {

	}

	public MessageSetQuestInfo(Province civ, QuestData current, QuestData next) {
		this.civ = civ;
		this.currentQuest = current;
		this.nextQuest = next;
		createMessage();
		serializeData();
	}
	
	private void createMessage() {
		questMessage = new QuestMessage();
		questMessage.provinceName = civ.name;
		
		if(currentQuest != null) {
			QuestDelegator quest = new QuestDelegator(currentQuest);
			questMessage.questTitle = quest.getTitle();
			questMessage.questDescription = quest.getDescription();
			questMessage.accepted = true;
		} else {
			QuestDelegator quest = new QuestDelegator(nextQuest);
			questMessage.questTitle = quest.getTitle();
			questMessage.questDescription = quest.getDescription();
			questMessage.accepted = false;
		}
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
		if(questMessage != null) {
			questMessageJson = new Gson().toJson(questMessage, QuestMessage.class);
		} else {
			questMessageJson = "";
		}
	}
	
	private void deserializeData() {
		questMessage = new Gson().fromJson(questMessageJson, QuestMessage.class);
	}
	
	public static class Worker {
		public void work(MessageSetQuestInfo message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			final EntityPlayer player = minecraft.player;

			if (player == null) {
				return;
			}
			
			VillageLordGuiContainer.setProvinceName(message.questMessage.provinceName);
			VillageLordGuiContainer.setQuestData(message.questMessage.questTitle, message.questMessage.questDescription, message.questMessage.accepted);
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
		public String provinceName;
		public String questTitle;
		public String questDescription;
		public boolean accepted;
	}
}
