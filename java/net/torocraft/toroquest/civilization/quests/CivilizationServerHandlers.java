package net.torocraft.toroquest.civilization.quests;

import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.internal.FMLMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.torocraft.toroquest.civilization.CivilizationEvent;
import net.torocraft.toroquest.gui.VillageLordContainer;

public class CivilizationServerHandlers {

    @SubscribeEvent
    public void onVillageLordInteract(CivilizationEvent.VillageLordInteract event) {
        //stolen from FMLNetworkHandler.openGui
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getEntityPlayer();
            Container remoteGuiContainer = new VillageLordContainer(entityPlayerMP,
                    event.getVillageLord().getInventory(entityPlayerMP.getUniqueID()), entityPlayerMP.getServerWorld());
            if (remoteGuiContainer != null) {
                entityPlayerMP.getNextWindowId();
                entityPlayerMP.closeContainer();
                int windowId = entityPlayerMP.currentWindowId;
                /* is this necessary if we aren't using the rest of the FML logic?

                FMLMessage.OpenGui openGui = new FMLMessage.OpenGui(windowId, mc.getModId(), modGuiId, x, y, z);
                EmbeddedChannel embeddedChannel = channelPair.get(Side.SERVER);
                embeddedChannel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
                embeddedChannel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(entityPlayerMP);
                embeddedChannel.writeOutbound(openGui);
                */
                entityPlayerMP.openContainer = remoteGuiContainer;
                entityPlayerMP.openContainer.windowId = windowId;
                entityPlayerMP.openContainer.addListener(entityPlayerMP);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(event.getEntityPlayer(), event.getEntityPlayer().openContainer));
            }
        }
    }
}
