package net.torocraft.toroquest.civilization;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
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

		Civilization civ = getCurrentCivilization(player);

		if (civ == null) {
			return;
		}

		NBTTagCompound tag = getToroQuestPlayerDataTag(player);

		int rep = tag.getInteger("rep_" + civ);

		if (victum instanceof EntityVillager) {
			rep -= 10;
		} else if (victum instanceof EntityMob) {
			rep += 1;
		} else {
			rep -= 1;
		}

		tag.setInteger("rep_" + civ, rep);

		chat(player, "Your rep for [" + civ.getLocalizedName() + "] is now [" + rep + "]");
	}

	public static int getPlayerRep(EntityPlayer player, Civilization civ) {
		try {
			return getToroQuestPlayerDataTag(player).getInteger("rep_" + civ);
		} catch (Exception e) {
			return 0;
		}
	}

	private static NBTTagCompound getToroQuestPlayerDataTag(EntityPlayer player) {
		NBTTagCompound tag = (NBTTagCompound) player.getEntityData().getTag("toroquest");
		if (tag == null) {
			tag = new NBTTagCompound();
			player.getEntityData().setTag("toroquest", tag);
		}
		return tag;
	}

	public Civilization getCurrentCivilization(EntityPlayer player) {
		try {
			return Civilization.valueOf(getToroQuestPlayerDataTag(player).getString("inciv"));
		} catch (Exception e) {
			return null;
		}
	}

	public void updateCurrentCivilization(EntityPlayer player, Civilization civ) {
		String sCiv = null;
		if (civ != null) {
			sCiv = civ.toString();
		}
		try {
			if (sCiv == null) {
				getToroQuestPlayerDataTag(player).removeTag("inciv");
			} else {
				getToroQuestPlayerDataTag(player).setString("inciv", sCiv);
			}
		} catch (Exception e) {
			System.out.println("Unable to save current civilization location: " + e.getMessage());
		}
	}

	@SubscribeEvent
	public void handleEnteringBorder(EntityEvent.EnteringChunk event) {
		if (!(event.getEntity() instanceof EntityPlayerMP)) {
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();

		Civilization oldCiv = getCurrentCivilization(player);
		Civilization newCiv = queryCurrentCiv(event, player);

		if (newCiv == null && oldCiv != null) {
			chat(player, leavingMessage(player, oldCiv));
			updateCurrentCivilization(player, null);
		} else if (newCiv != null && !newCiv.equals(oldCiv)) {
			chat(player, enteringMessage(player, newCiv));
			updateCurrentCivilization(player, newCiv);
		}
	}

	private String leavingMessage(EntityPlayerMP player, Civilization civ) {
		int rep = getPlayerRep(player, civ);
		if (rep >= 10) {
			return civ.getFriendlyLeavingMessage();
		} else if (rep <= -10) {
			return civ.getHostileLeavingMessage();
		} else {
			return civ.getNeutralLeavingMessage();
		}
	}

	private String enteringMessage(EntityPlayerMP player, Civilization civ) {
		int rep = getPlayerRep(player, civ);
		if (rep >= 10) {
			return civ.getFriendlyEnteringMessage();
		} else if (rep <= -10) {
			return civ.getHostileEnteringMessage();
		} else {
			return civ.getNeutralEnteringMessage();
		}
	}

	private Civilization queryCurrentCiv(EntityEvent.EnteringChunk event, EntityPlayerMP player) {
		return getCivilizationAt(player.getEntityWorld(), event.getNewChunkX(), event.getNewChunkZ());
	}

	public static Civilization getCivilizationAt(World world, int chunkX, int chunkZ) {
		CivilizationsWorldSaveData civData = CivilizationsWorldSaveData.get(world);
		return civData.getCivilationAt(chunkX, chunkZ);
	}

	private void chat(EntityPlayer player, String message) {
		player.addChatMessage(new TextComponentString(message));
	}
}
