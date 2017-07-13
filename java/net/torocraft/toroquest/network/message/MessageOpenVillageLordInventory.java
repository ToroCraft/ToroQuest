package net.torocraft.toroquest.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.entities.EntityVillageLord;
import net.torocraft.toroquest.gui.VillageLordGuiContainer;

public class MessageOpenVillageLordInventory implements IMessage {

    private EntityVillageLord villageLord;

    public static void init(int packetId) {
        ToroQuest.NETWORK.registerMessage(MessageOpenVillageLordInventory.Handler.class, MessageOpenVillageLordInventory.class, packetId, Side.CLIENT);
    }

    public MessageOpenVillageLordInventory() {

    }

    public MessageOpenVillageLordInventory(EntityVillageLord villageLord) {
        this.villageLord = villageLord;
    }

    public EntityVillageLord getVillageLord() {
        return villageLord;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<MessageOpenVillageLordInventory, IMessage> {
        @Override
        public IMessage onMessage(final MessageOpenVillageLordInventory message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> work(message));
            return null;
        }

        public static void work(MessageOpenVillageLordInventory message) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            FMLCommonHandler.instance().showGuiScreen(new VillageLordGuiContainer(player,
                    message.getVillageLord().getInventory(player.getUniqueID()),
                    player.getEntityWorld()));
        }
    }
}
