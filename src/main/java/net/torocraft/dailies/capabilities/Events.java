package net.torocraft.dailies.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events {

	@SubscribeEvent
	public void harvestDrops(HarvestDropsEvent event) {
		IDailiesCapability dailes = getCapability(event.getHarvester());
		if (dailes == null) {
			return;
		}

		dailes.gather(1);
		System.out.println(dailes.statusMessage());
		event.getHarvester().addChatMessage(new TextComponentString(TextFormatting.RED + "" + dailes.statusMessage()));
	}

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
		
		newDailes.setGatherCount(originalDailes.getGatherCount());
		newDailes.setHuntCount(originalDailes.getHuntCount());


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
	public void onInteract(PlayerInteractEvent.LeftClickBlock event) {

		if (event.getItemStack() == null) {
			return;
		}

		if (event.getItemStack().getItem() != Items.stick) {
			return;
		}



		TileEntity te = event.getWorld().getTileEntity(event.getPos());

		if (te != null && te.hasCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null)) {

			event.setCanceled(true);
			IDailiesCapability inv = te.getCapability(CapabilityDailiesHandler.DAILIES_CAPABILITY, null);

			System.out.println("left click, has cap");

			// System.out.println("Hi I'm a " + inv.getOwnerType());
		}

		if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.dirt) {
			event.getEntityPlayer().addChatMessage(new TextComponentString(TextFormatting.RED + "" + TextFormatting.ITALIC + "TEST TEST"));
			event.setCanceled(true);
		}
	}

	// Example of having this annotation on a method, this will be called
	// when the capability is present.
	// You could do something like register event handlers to attach these
	// capabilities to objects, or
	// setup your factory, who knows. Just figured i'd give you the power.

	@CapabilityInject(IDailiesCapability.class)
	private static void capRegistered(Capability<IDailiesCapability> cap) {
		System.out.println("IDailiesCapability was registered wheeeeee!");
	}

	// An example of how to attach a capability to an arbitrary Tile entity.
	// Note: Doing this IS NOT recommended for normal implementations.
	// If you control the TE it is HIGHLY recommend that you implement a
	// fast version of the has/getCapability functions yourself. So you have
	// control over everything yours being called first.

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

	/*
	 * @SubscribeEvent public void
	 * onTELoad(AttachCapabilitiesEvent.TileEntity event) {
	 * event.addCapability(new ResourceLocation(NAME), new
	 * Provider(event.getTileEntity())); }
	 */

}