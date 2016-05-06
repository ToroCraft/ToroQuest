package net.torocraft.dailies.capabilities;

import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.dailies.DailiesRequester;
import net.torocraft.dailies.DailiesWorldData;
import net.torocraft.dailies.quests.DailyQuest;

public class Events {

	/*
	 * SubscribeEvent public void harvestDrops(HarvestDropsEvent event) {
	 * IDailiesCapability dailes = getCapability(event.getHarvester()); if
	 * (dailes == null) { return; }
	 * 
	 * dailes.gather(1); System.out.println(dailes.statusMessage());
	 * event.getHarvester().addChatMessage(new
	 * TextComponentString(TextFormatting.RED + "" + dailes.statusMessage())); }
	 */

	// @SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {

		if (event.getWorld().isRemote) {
			return;
		}

		DailiesRequester requester = new DailiesRequester();
		Set<DailyQuest> dailies = requester.getDailies();

		if (dailies == null) {
			System.out.println("******************* No dailies found, lame!");
		} else {
			System.out.println("********************** Dailies found COUNT[" + dailies.size() + "]");
		}

		if (dailies != null) {
			DailiesWorldData worldData = DailiesWorldData.get(event.getWorld());
			worldData.setDailyQuests(dailies);
		}

	}

	@SubscribeEvent
	public void onGather(EntityItemPickupEvent event) {
		IDailiesCapability dailes = getCapability(event.getEntityPlayer());
		if (dailes == null) {
			return;
		}

		boolean hit = dailes.gather(event.getEntityPlayer(), event.getItem());

		if (hit) {
			event.setCanceled(true);
			event.getItem().setDead();
		}

	}
	/*
	 * @SubscribeEvent public void onPlayerJoin(EntityJoinWorldEvent event) {
	 * if(!(event.getEntity() instanceof EntityPlayer)){ return; }
	 * 
	 * IDailiesCapability dailes =
	 * getCapability((EntityPlayer)event.getEntity()); if (dailes == null) {
	 * return; }
	 * 
	 * }
	 */

	@SubscribeEvent
	public void onHunt(LivingDeathEvent event) {

		EntityPlayer player = null;

		EntityLivingBase e = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (source.getSourceOfDamage() instanceof EntityPlayer) {
			player = (EntityPlayer) source.getSourceOfDamage();
		}

		if (player == null) {
			return;
		}

		IDailiesCapability dailes = getCapability(player);
		if (dailes == null) {
			return;
		}

		dailes.hunt(player, e);
	}

	@SubscribeEvent
	public void onDeath(PlayerEvent.Clone event) {
		if (!event.isWasDeath()) {
			return;
		}

		IDailiesCapability newDailes = getCapability(event.getEntityPlayer());
		IDailiesCapability originalDailes = getCapability(event.getOriginal());

		if (newDailes == null || originalDailes == null) {
			return;
		}

		newDailes.readNBT(originalDailes.writeNBT());
	}

	@SubscribeEvent
	public void onSave(PlayerEvent.SaveToFile event) {
		IDailiesCapability dailes = getCapability(event.getEntityPlayer());
		if (dailes == null) {
			return;
		}
		event.getEntityPlayer().getEntityData().setTag(CapabilityDailiesHandler.NAME, dailes.writeNBT());
	}

	@SubscribeEvent
	public void onLoad(PlayerEvent.LoadFromFile event) {
		IDailiesCapability dailes = getCapability(event.getEntityPlayer());
		if (dailes == null) {
			return;
		}
		dailes.readNBT((NBTTagCompound) event.getEntityPlayer().getEntityData().getTag(CapabilityDailiesHandler.NAME));
	}

	private IDailiesCapability getCapability(EntityPlayer player) {
		if (isMissingCapability(player)) {
			return null;
		}
		return player.getCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null);
	}

	private boolean isMissingCapability(EntityPlayer player) {
		return player == null || !player.hasCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null);
	}

	@SubscribeEvent
	public void onEntityLoad(AttachCapabilitiesEvent.Entity event) {

		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}

		try {
			System.out.println("loading cap to player: " + ((EntityPlayer) event.getEntity()).getName());
		} catch (Exception e) {
			System.out.println("loading cap to player [" + event.getEntity().getClass().getName() + "]");
		}

		event.addCapability(new ResourceLocation(CapabilityDailiesHandler.NAME), new Provider());

	}

}