package net.torocraft.toroquest.civilization;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData.Civilization;

public class CivilizationHandlers {

	@SubscribeEvent
	public void onHunt(LivingDeathEvent event) {
		EntityPlayer player = null;
		EntityLivingBase victum = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (source.getEntity() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getEntity();
		}

		if (player == null) {
			return;
		}

		adjustRep(player, victum);
	}

	protected void adjustRep(EntityPlayer player, EntityLivingBase victum) {

		Civilization civ = CivilizationsWorldSaveData.get(victum.worldObj).getCivilationAt(victum.chunkCoordX, victum.chunkCoordZ);

		if (civ == null) {
			return;
		}

		NBTTagCompound tag = (NBTTagCompound) player.getEntityData().getTag("toroquest");
		if (tag == null) {
			tag = new NBTTagCompound();
			player.getEntityData().setTag("toroquest", tag);
		}

		int rep = tag.getInteger("rep_" + civ);

		if (victum instanceof EntityVillager) {
			rep -= 10;
		} else if (victum instanceof EntityMob) {
			rep += 1;
		} else {
			rep -= 1;
		}

		tag.setInteger("rep_" + civ, rep);

		chat(player, "Your rep for [" + civ + "] is now [" + rep + "]");
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
			chat(player, "Entering Civilization of the " + civ);
		}
	}

	private void chat(EntityPlayer player, String message) {
		player.addChatMessage(new TextComponentString(message));
	}
}
