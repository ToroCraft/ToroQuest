package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;

public class MessageRequestPlayerCivilizationSync implements IMessage {

	public MessageRequestPlayerCivilizationSync() {

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(1);
	}

	public static class Handler implements IMessageHandler<MessageRequestPlayerCivilizationSync, IMessage> {

		public Handler() {
		}

		@Override
		public IMessage onMessage(final MessageRequestPlayerCivilizationSync message, MessageContext ctx) {
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
					PlayerCivilizationCapabilityImpl.get(player).syncClient();
				}
			});

			return null;
		}

	}
}
