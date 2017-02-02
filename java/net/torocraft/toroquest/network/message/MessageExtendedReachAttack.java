package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.torocraft.toroquest.item.IExtendedReach;

public class MessageExtendedReachAttack implements IMessage {
	private int entityId;

	public MessageExtendedReachAttack() {
		// need this constructor
	}

	public MessageExtendedReachAttack(int parEntityId) {
		entityId = parEntityId;
		// DEBUG
		System.out.println("Constructor");
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityId = ByteBufUtils.readVarInt(buf, 4);
		// DEBUG
		System.out.println("fromBytes");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, entityId, 4);
		// DEBUG
		System.out.println("toBytes encoded");
	}

	public static class Handler implements IMessageHandler<MessageExtendedReachAttack, IMessage> {
		@Override
		public IMessage onMessage(final MessageExtendedReachAttack message, MessageContext ctx) {
			// DEBUG
			System.out.println("Message received");

			final EntityPlayerMP thePlayer = ctx.getServerHandler().playerEntity;

			thePlayer.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					Entity theEntity = thePlayer.world.getEntityByID(message.entityId);
					// DEBUG
					System.out.println("Entity = " + theEntity);

					// Need to ensure that hackers can't cause trick kills,
					// so double check weapon type and reach
					// getCurrentEquippedItem()?
					if (thePlayer.getHeldItemMainhand() == null) {
						return;
					}
					if (thePlayer.getHeldItemMainhand().getItem() instanceof IExtendedReach) {
						IExtendedReach theExtendedReachWeapon = (IExtendedReach) thePlayer.getHeldItemMainhand().getItem();
						double distanceSq = thePlayer.getDistanceSqToEntity(theEntity);
						double reachSq = theExtendedReachWeapon.getReach() * theExtendedReachWeapon.getReach();
						if (reachSq >= distanceSq) {
							thePlayer.attackTargetEntityWithCurrentItem(theEntity);
						}
					}
				}
			});
			return null; // no response message
		}
	}
}
