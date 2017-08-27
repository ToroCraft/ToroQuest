package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.CivilizationType;
import net.torocraft.toroquest.civilization.Province;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;

public class MessageSetPlayerReputation implements IMessage {

	private CivilizationType civ;
	private int amount;

	public MessageSetPlayerReputation() {

	}

	public MessageSetPlayerReputation(CivilizationType civ, int amount) {
		this.civ = civ;
		this.amount = amount;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		amount = buf.readInt();
		civ = e(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(amount);
		ByteBufUtils.writeUTF8String(buf, s(civ));

	}

	public static class Worker {
		public void work(MessageSetPlayerReputation message) {
			Minecraft minecraft = Minecraft.getMinecraft();
			final EntityPlayer player = minecraft.player;

			if (player == null) {
				return;
			}

			PlayerCivilizationCapabilityImpl.get(player).setReputation(message.civ, message.amount);
		}

		private String s(Province civ) {
			if (civ == null) {
				return null;
			}
			return civ.toString();
		}
	}

	public static class Handler implements IMessageHandler<MessageSetPlayerReputation, IMessage> {
		@Override
		public IMessage onMessage(final MessageSetPlayerReputation message, MessageContext ctx) {
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

	private static String s(CivilizationType civ) {
		try {
			return civ.toString();
		} catch (Exception e) {
			return "";
		}
	}

	private int i(Integer integer) {
		if (integer == null) {
			return 0;
		}
		return integer;
	}

	private CivilizationType e(String s) {
		try {
			return CivilizationType.valueOf(s);
		} catch (Exception e) {
			return null;
		}
	}
}
