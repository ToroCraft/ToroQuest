package net.torocraft.toroquest.civilization;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.ToroQuest;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapability;
import net.torocraft.toroquest.civilization.player.PlayerCivilizationCapabilityImpl;
import net.torocraft.toroquest.entities.EntityToroNpc;
import net.torocraft.toroquest.network.ToroQuestPacketHandler;
import net.torocraft.toroquest.network.message.MessageRequestPlayerCivilizationSync;

public class CivilizationHandlers {

	@SubscribeEvent
	public void handleReputationChange(CivilizationEvent.ReputationChange event) {

	}

	@SubscribeEvent
	public void handleEnterProvince(CivilizationEvent.Enter event) {

	}

	@SubscribeEvent
	public void handleLeaveProvince(CivilizationEvent.Leave event) {

	}

	@SubscribeEvent
	public void onDeath(PlayerEvent.Clone event) {
		if (event.getEntityPlayer().getEntityWorld().isRemote) {
			return;
		}
		if (!event.isWasDeath()) {
			return;
		}

		PlayerCivilizationCapability newCap = PlayerCivilizationCapabilityImpl.get(event.getEntityPlayer());
		PlayerCivilizationCapability oringialCap = PlayerCivilizationCapabilityImpl.get(event.getOriginal());

		if (newCap == null || oringialCap == null) {
			return;
		}

		newCap.readNBT(oringialCap.writeNBT());
	}

	@SubscribeEvent
	public void onSave(PlayerEvent.SaveToFile event) {
		if (event.getEntityPlayer().getEntityWorld().isRemote) {
			return;
		}
		PlayerCivilizationCapability cap = PlayerCivilizationCapabilityImpl.get(event.getEntityPlayer());
		if (cap == null) {
			return;
		}
		event.getEntityPlayer().getEntityData().setTag(ToroQuest.MODID + ".playerCivilization", cap.writeNBT());
	}

	@SubscribeEvent
	public void onLoad(PlayerEvent.LoadFromFile event) {
		if (event.getEntityPlayer().getEntityWorld().isRemote) {
			return;
		}

		PlayerCivilizationCapability cap = PlayerCivilizationCapabilityImpl.get(event.getEntityPlayer());
		if (cap == null) {
			return;
		}
		cap.readNBT((NBTTagCompound) event.getEntityPlayer().getEntityData().getTag(ToroQuest.MODID + ".playerCivilization"));
	}

	@SubscribeEvent
	public void onEntityLoad(final AttachCapabilitiesEvent.Entity event) {
		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}

		System.out.println("****** AttachCapabilitiesEvent ");

		EntityPlayer player = (EntityPlayer) event.getEntity();

		event.addCapability(new ResourceLocation(ToroQuest.MODID, "playerCivilization"), new PlayerCivilizationCapabilityProvider(player));

		if (player.getEntityWorld().isRemote) {
			System.out.println("*********************************** AttachCapabilitiesEvent send sync request from client");
			ToroQuestPacketHandler.INSTANCE.sendToServer(new MessageRequestPlayerCivilizationSync());
		}
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

		if (province == null || province.civilization == null) {
			return;
		}

		PlayerCivilizationCapabilityImpl.get(player).adjustPlayerReputation(province.civilization, getRepuationAdjustmentFor(victum, province));

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
		PlayerCivilizationCapabilityImpl.get(player).updatePlayerLocation(event.getNewChunkX(), event.getNewChunkZ());
	}

	private String leavingMessage(EntityPlayer player, CivilizationType civ) {
		int rep = PlayerCivilizationCapabilityImpl.get(player).getPlayerReputation(civ);
		if (rep >= 10) {
			return civ.getFriendlyLeavingMessage();
		} else if (rep <= -10) {
			return civ.getHostileLeavingMessage();
		} else {
			return civ.getNeutralLeavingMessage();
		}
	}

	private String enteringMessage(EntityPlayer player, CivilizationType civ) {
		int rep = PlayerCivilizationCapabilityImpl.get(player).getPlayerReputation(civ);
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
