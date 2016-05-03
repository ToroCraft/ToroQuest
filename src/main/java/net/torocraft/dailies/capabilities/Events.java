package net.torocraft.dailies.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {

	/*
	SubscribeEvent
	public void harvestDrops(HarvestDropsEvent event) {
		IDailiesCapability dailes = getCapability(event.getHarvester());
		if (dailes == null) {
			return;
		}

		dailes.gather(1);
		System.out.println(dailes.statusMessage());
		event.getHarvester().addChatMessage(new TextComponentString(TextFormatting.RED + "" + dailes.statusMessage()));
	}*/
	
	@SubscribeEvent
	public void onGather(EntityItemPickupEvent event) {
		IDailiesCapability dailes = getCapability(event.getEntityPlayer());
		if (dailes == null) {
			return;
		}

		boolean hit = dailes.gather(event.getEntityPlayer(), event.getItem(), 1);

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
	public void onDeath(PlayerEvent.Clone event) {
		if(!event.isWasDeath()){
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
		event.getEntityPlayer().getEntityData().setTag(CapabilityDailiesHandler.NAME, CapabilityDailiesHandler.DAILIES_CAPABILITY.writeNBT(dailes, null));
	}

	@SubscribeEvent
	public void onLoad(PlayerEvent.LoadFromFile event) {
		IDailiesCapability dailes = getCapability(event.getEntityPlayer());
		if (dailes == null) {
			return;
		}
		CapabilityDailiesHandler.DAILIES_CAPABILITY.readNBT(dailes, null, event.getEntityPlayer().getEntityData().getTag(CapabilityDailiesHandler.NAME));
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