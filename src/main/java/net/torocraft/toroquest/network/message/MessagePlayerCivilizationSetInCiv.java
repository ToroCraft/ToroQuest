package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;

public class MessagePlayerCivilizationSetInCiv implements IMessage {

	public Province province;

	public MessagePlayerCivilizationSetInCiv() {

	}

	public MessagePlayerCivilizationSetInCiv(Province province) {
		this.province = province;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		province = new Province();
		province.readNBT(ByteBufUtils.readTag(buf));
	}


	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, province.writeNBT());
	}

	public static class Handler implements IMessageHandler<MessagePlayerCivilizationSetInCiv, IMessage> {

		@Override
		public IMessage onMessage(final MessagePlayerCivilizationSetInCiv message, MessageContext ctx) {
			if (ctx.side != Side.CLIENT) {
				return null;
			}

			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			if (player == null) {
				return null;
			}


			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					processMessage(message, null);
				}
			});

			return null;
		}

		void processMessage(MessagePlayerCivilizationSetInCiv message, EntityPlayerMP player) {
			PlayerCivilizationCapabilityImpl.get(player).setPlayerInCivilization(message.province);
			System.out.println("got packet civ: " + s(message.province));
			return;
		}

		private String s(Province civ) {
			if (civ == null) {
				return null;
			}
			return civ.toString();
		}
	}

}
