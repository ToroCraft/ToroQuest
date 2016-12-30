package net.torocraft.toroquest.network.message;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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
import net.torocraft.toroquest.network.ToroQuestPacketHandler;

public class MessageQuestUpdate implements IMessage {

	public MessageQuestUpdate() {
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(1);
		
	}
	
	public static class Worker {

		void work(MessageQuestUpdate message, EntityPlayer player) {
			Province province = CivilizationUtil.getProvinceAt(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ);
			QuestData currentQuestData = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
			
			if(currentQuestData != null) {
				PlayerCivilizationCapabilityImpl.get(player).rejectQuest();
				QuestData nextQuest = PlayerCivilizationCapabilityImpl.get(player).getNextQuestFor(province);
				ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, null, nextQuest), (EntityPlayerMP) player);
			} else {
				List<ItemStack> outputItems = PlayerCivilizationCapabilityImpl.get(player).acceptQuest(new ArrayList<ItemStack>());
				if(outputItems == null) {
					
				}
				QuestData currentQuest = PlayerCivilizationCapabilityImpl.get(player).getCurrentQuestFor(province);
				ToroQuestPacketHandler.INSTANCE.sendTo(new MessageSetQuestInfo(province, currentQuest, null), (EntityPlayerMP) player);
			}
		}
	}

	public static class Handler implements IMessageHandler<MessageQuestUpdate,IMessage> {

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
					new Worker().work(message, player);
				}
			});

			return null;
		}
	}
}
