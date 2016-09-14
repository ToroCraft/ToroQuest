package net.torocraft.toroquest.civilization;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.civilization.CivilizationsWorldSaveData.Civilization;

public class CivilizationHandlers {
	
	@SubscribeEvent
	public void registerNewCiviliationBorder(PopulateChunkEvent.Populate event) {
		if (!event.isHasVillageGenerated()) {
			return;
		}

		System.out.println("*** PopulateChunkEvent.Populate in EVENT_BUS");

		CivilizationsWorldSaveData civData = CivilizationsWorldSaveData.get(event.getWorld());

		Civilization civ = civData.getCivilationAt(event.getChunkX(), event.getChunkZ());

		if (civ != null) {
			return;
		}

		civ = Civilization.values()[event.getRand().nextInt(Civilization.values().length)];
		civData.registerBorder(event.getChunkX(), event.getChunkZ(), civ);
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
