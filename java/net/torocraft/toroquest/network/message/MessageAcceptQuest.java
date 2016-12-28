package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAcceptQuest implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	public static class Handler implements IMessageHandler<MessageAcceptQuest,IMessage> {

		@Override
		public IMessage onMessage(MessageAcceptQuest message, MessageContext ctx) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
