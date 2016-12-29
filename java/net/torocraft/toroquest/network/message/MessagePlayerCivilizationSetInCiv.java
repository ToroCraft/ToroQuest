package net.torocraft.toroquest.network.message;

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
		if (province == null) {
			ByteBufUtils.writeTag(buf, new NBTTagCompound());
		} else {
			ByteBufUtils.writeTag(buf, province.writeNBT());
		}
	}

	public static class Worker {

		void work(MessagePlayerCivilizationSetInCiv message) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			if (player == null) {
				return;
			}
			PlayerCivilizationCapabilityImpl.get(player).setInCivilization(message.province);
		}

		private String s(Province civ) {
			if (civ == null) {
				return null;
			}
			return civ.toString();
		}
	}

	public static class Handler implements IMessageHandler<MessagePlayerCivilizationSetInCiv, IMessage> {

		@Override
		public IMessage onMessage(final MessagePlayerCivilizationSetInCiv message, MessageContext ctx) {
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
