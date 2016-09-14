package net.torocraft.toroquest.civilization;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData.Civilization;

public class CivilizationHandlers {
	
	@SubscribeEvent
	public void onHunt(LivingDeathEvent event) {

		System.out.println("LivingDeathEvent");

		EntityPlayer player = null;

		EntityLivingBase e = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (source.getEntity() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getEntity();
		}

		if (player == null) {
			return;
		}

		int rep = player.getEntityData().getInteger("CivilizationRep");
		rep += 1;
		player.getEntityData().setInteger("CivilizationRep", rep);

		System.out.println("Your rep is now [" + rep + "]");

	}

	@SubscribeEvent
	public void onSave(PlayerEvent.SaveToFile event) {

		/*
		 * IDailiesCapability dailies = getCapability(event.getEntityPlayer());
		 * if (dailies == null) { return; }
		 * event.getEntityPlayer().getEntityData().setTag(
		 * CapabilityDailiesHandler.NAME, dailies.writeNBT());
		 */
	}

	@SubscribeEvent
	public void onLoad(PlayerEvent.LoadFromFile event) {
		/*
		 * (NBTTagCompound) event.getEntityPlayer().getEntityData().getTag(
		 * CapabilityDailiesHandler.NAME)
		 * 
		 * dailies.readNBT();
		 */
	}

	@SubscribeEvent
	public void handleEnteringBorder(EntityEvent.EnteringChunk event) {
		if (!(event.getEntity() instanceof EntityPlayerMP)) {
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
		CivilizationsWorldSaveData civData = CivilizationsWorldSaveData.get(player.getEntityWorld());

		Civilization civ = civData.getCivilationAt(event.getNewChunkX(), event.getNewChunkZ());

		if (civ != null) {
			player.addChatMessage(new TextComponentString("Entering Civilization of the " + civ));
		}

	}
}
