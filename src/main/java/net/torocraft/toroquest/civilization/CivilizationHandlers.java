package net.torocraft.toroquest.civilization;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.entities.EntityToroNpc;

public class CivilizationHandlers {


	@SubscribeEvent
	public void checkKillsInCivilization(LivingDeathEvent event) {
		EntityPlayer player = null;
		EntityLivingBase victum = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (source.getEntity() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getEntity();
		}

		if (player == null) {
			return;
		}

		Province province = CivilizationUtil.getPlayerCurrentProvince(player);
		CivilizationUtil.adjustPlayerReputation(player, province, getRepuationAdjustmentFor(victum, province));

	}

	private int getRepuationAdjustmentFor(EntityLivingBase victum, Province province) {

		if (province == null || province.civilization == null) {
			return 0;
		}

		if (victum instanceof EntityVillager) {
			return -10;
		}

		if (victum instanceof EntityMob) {
			return 1;
		}

		if (victum instanceof EntityToroNpc) {
			CivilizationType npcCiv = ((EntityToroNpc) victum).getCivilization();

			if (npcCiv == null) {
				return -1;
			}

			if (npcCiv.equals(province.civilization)) {
				return -10;
			} else {
				return 10;
			}
		}

		return -1;
	}

	@SubscribeEvent
	public void handleEnteringProvince(EntityEvent.EnteringChunk event) {
		if (!(event.getEntity() instanceof EntityPlayerMP)) {
			return;
		}
		EntityPlayerMP player = (EntityPlayerMP) event.getEntity();

		CivilizationUtil.setPlayerChunk(player, player.chunkCoordX, player.chunkCoordZ);
	}

	@SubscribeEvent
	public void handleLeaveProvince(ProvinceEvent.ReputationChange event) {
		chat(event.getEntityPlayer(), "Reputation with " + event.getProvince().civilization.getLocalizedName() + " is now " + event.getReputation());
	}

	@SubscribeEvent
	public void handleEnterProvince(ProvinceEvent.Enter event) {
		chat(event.getEntityPlayer(), enteringMessage(event.getEntityPlayer(), event.getProvince().civilization));
	}

	@SubscribeEvent
	public void handleLeaveProvince(ProvinceEvent.Leave event) {
		chat(event.getEntityPlayer(), leavingMessage(event.getEntityPlayer(), event.getProvince().civilization));
	}

	private String leavingMessage(EntityPlayer player, CivilizationType civ) {
		int rep = CivilizationUtil.getPlayerReputation(player, civ);
		if (rep >= 10) {
			return civ.getFriendlyLeavingMessage();
		} else if (rep <= -10) {
			return civ.getHostileLeavingMessage();
		} else {
			return civ.getNeutralLeavingMessage();
		}
	}

	private String enteringMessage(EntityPlayer player, CivilizationType civ) {
		int rep = CivilizationUtil.getPlayerReputation(player, civ);
		if (rep >= 10) {
			return civ.getFriendlyEnteringMessage();
		} else if (rep <= -10) {
			return civ.getHostileEnteringMessage();
		} else {
			return civ.getNeutralEnteringMessage();
		}
	}

	private void chat(EntityPlayer player, String message) {
		player.addChatMessage(new TextComponentString(message));
	}
}
