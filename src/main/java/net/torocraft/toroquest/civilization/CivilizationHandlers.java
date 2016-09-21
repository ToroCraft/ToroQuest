package net.torocraft.toroquest.civilization;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapability;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.entities.EntityToroNpc;

public class CivilizationHandlers {
	
	@SubscribeEvent
	public void onEntityLoad(final AttachCapabilitiesEvent.Entity event) {

		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}

		try {
			System.out.println("loading cap to player: " + ((EntityPlayer) event.getEntity()).getName());
		} catch (Exception e) {
			System.out.println("loading cap to player [" + event.getEntity().getClass().getName() + "]");
		}

		event.addCapability(new ResourceLocation(ToroQuest.MODID, "playerCivilization"), new PlayerCivilizationCapabilityProvider((EntityPlayer) event.getEntity()));
	}

	public static class PlayerCivilizationCapabilityProvider implements ICapabilityProvider {

		@CapabilityInject(PlayerCivilizationCapability.class)
		public static final Capability<PlayerCivilizationCapability> CAP = null;

		private final EntityPlayer player;
		private PlayerCivilizationCapability instance;

		public PlayerCivilizationCapabilityProvider(EntityPlayer player) {
			this.player = player;
			instance = new PlayerCivilizationCapabilityImpl(player);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CAP;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return PlayerCivilizationCapabilityImpl.INSTANCE.cast(instance);
		}
	}

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

		Province province = PlayerCivilizationCapabilityImpl.get(player).getPlayerInCivilization();
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